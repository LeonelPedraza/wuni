package com.app.audiofocus.player

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.app.audiofocus.domain.model.Audiobook
import com.app.audiofocus.domain.model.PlaybackProgress
import com.app.audiofocus.domain.repository.PlaybackProgressRepository
import com.app.audiofocus.util.nowIsoString
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs

@Singleton
class AudioFocusPlayerController @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val playbackProgressRepository: PlaybackProgressRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var controller: MediaController? = null
    private var progressJob: Job? = null
    private var lastSavedPositionMs: Long = -1L
    private var currentAudiobook: Audiobook? = null

    private val _playerState = MutableStateFlow(AudioFocusPlaybackState())
    val playerState: StateFlow<AudioFocusPlaybackState> = _playerState.asStateFlow()

    private val playerListener = object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            updateState(player)
            if (events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED) && player.playbackState == Player.STATE_ENDED) {
                scope.launch { saveCurrentProgress(force = true) }
            }
            if (events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)) {
                scope.launch { saveCurrentProgress(force = true) }
            }
        }
    }

    private val appLifecycleObserver = object : DefaultLifecycleObserver {
        override fun onStop(owner: LifecycleOwner) {
            scope.launch { saveCurrentProgress(force = true) }
        }
    }

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)
    }

    suspend fun playAudiobook(audiobook: Audiobook) {
        val mediaController = getController()
        val sameItem = mediaController.currentMediaItem?.mediaId == audiobook.id
        currentAudiobook = audiobook
        val savedProgress = playbackProgressRepository.getProgress(audiobook.id)
        val savedPositionMs = savedProgress?.positionMs
            ?.coerceAtLeast(0L)
            ?.coerceAtMost(audiobook.durationMs.coerceAtLeast(0L))
            ?: 0L
        if (!sameItem) {
            mediaController.setMediaItem(audiobook.toMediaItem())
            mediaController.prepare()
        }
        val currentPositionMs = mediaController.currentPosition.coerceAtLeast(0L)
        val shouldRestoreSavedPosition = savedPositionMs > 0L && (
            !sameItem ||
                currentPositionMs <= RESTORE_POSITION_TOLERANCE_MS ||
                (!mediaController.isPlaying && abs(currentPositionMs - savedPositionMs) > RESTORE_POSITION_TOLERANCE_MS)
            )
        if (shouldRestoreSavedPosition) {
            mediaController.seekTo(savedPositionMs)
        }
        lastSavedPositionMs = savedPositionMs.takeIf { it > 0L } ?: -1L
        mediaController.play()
        ensureProgressUpdates()
        updateState(mediaController)
    }

    suspend fun togglePlayPause() {
        val mediaController = getController()
        if (mediaController.isPlaying) {
            mediaController.pause()
            saveCurrentProgress(force = true)
        } else {
            mediaController.play()
        }
        ensureProgressUpdates()
        updateState(mediaController)
    }

    suspend fun seekTo(positionMs: Long) {
        val mediaController = getController()
        mediaController.seekTo(positionMs.coerceAtLeast(0L))
        saveCurrentProgress(force = true)
        updateState(mediaController)
    }

    suspend fun seekForward() {
        val mediaController = getController()
        mediaController.seekTo((mediaController.currentPosition + SEEK_INCREMENT_MS).coerceAtMost(mediaController.duration.takeIf { it > 0 } ?: Long.MAX_VALUE))
        saveCurrentProgress(force = true)
        updateState(mediaController)
    }

    suspend fun seekBack() {
        val mediaController = getController()
        mediaController.seekTo((mediaController.currentPosition - SEEK_INCREMENT_MS).coerceAtLeast(0L))
        saveCurrentProgress(force = true)
        updateState(mediaController)
    }

    suspend fun stopPlaybackForAudiobook(audiobookId: String) {
        val mediaController = controller ?: return
        if (mediaController.currentMediaItem?.mediaId != audiobookId) return
        saveCurrentProgress(force = true)
        mediaController.pause()
        mediaController.stop()
        mediaController.clearMediaItems()
        currentAudiobook = null
        lastSavedPositionMs = -1L
        _playerState.value = AudioFocusPlaybackState()
    }

    private suspend fun getController(): MediaController {
        controller?.let { return it }
        _playerState.value = _playerState.value.copy(isConnecting = true)
        val createdController = withContext(Dispatchers.IO) {
            MediaController.Builder(
                appContext,
                SessionToken(appContext, ComponentName(appContext, AudioFocusPlaybackService::class.java)),
            ).buildAsync().get()
        }
        createdController.addListener(playerListener)
        controller = createdController
        updateState(createdController)
        ensureProgressUpdates()
        return createdController
    }

    private fun ensureProgressUpdates() {
        if (progressJob != null) return
        progressJob = scope.launch {
            while (isActive) {
                controller?.let {
                    updateState(it)
                    if (it.isPlaying) {
                        saveCurrentProgress()
                    }
                }
                delay(if (controller?.isPlaying == true) 500L else 1_000L)
            }
        }
    }

    private suspend fun saveCurrentProgress(force: Boolean = false) {
        val mediaController = controller ?: return
        val audiobook = currentAudiobook ?: return
        val durationMs = mediaController.duration.takeIf { it > 0 } ?: audiobook.durationMs
        val positionMs = mediaController.currentPosition.coerceAtLeast(0L)
        if (!force && shouldSkipPeriodicSave(positionMs)) return

        val progressPercent = if (durationMs > 0) {
            ((positionMs * 100) / durationMs).toInt().coerceIn(0, 100)
        } else {
            0
        }
        val status = when {
            durationMs > 0 && durationMs - positionMs <= COMPLETION_THRESHOLD_MS -> "completed"
            positionMs > 0L -> "in_progress"
            else -> "not_started"
        }

        playbackProgressRepository.saveProgress(
            PlaybackProgress(
                audiobookId = audiobook.id,
                positionMs = positionMs,
                durationMs = durationMs,
                progressPercent = progressPercent,
                status = status,
                lastPlayedAt = nowIsoString(),
            ),
        )
        lastSavedPositionMs = positionMs
        _playerState.value = _playerState.value.copy(playbackStatus = status)
    }

    private fun shouldSkipPeriodicSave(positionMs: Long): Boolean {
        if (lastSavedPositionMs < 0) return false
        return positionMs < lastSavedPositionMs || positionMs - lastSavedPositionMs < PERIODIC_SAVE_INCREMENT_MS
    }

    private fun updateState(player: Player) {
        val durationMs = player.duration.takeIf { it > 0 } ?: 0L
        val positionMs = player.currentPosition.coerceAtLeast(0L)
        _playerState.value = AudioFocusPlaybackState(
            mediaId = player.currentMediaItem?.mediaId,
            title = player.mediaMetadata.title?.toString(),
            author = player.mediaMetadata.artist?.toString(),
            positionMs = positionMs,
            durationMs = durationMs,
            isPlaying = player.isPlaying,
            isConnecting = false,
            playbackStatus = when {
                durationMs > 0 && durationMs - positionMs <= COMPLETION_THRESHOLD_MS -> "completed"
                positionMs > 0L -> "in_progress"
                else -> "not_started"
            },
        )
    }

    private fun Audiobook.toMediaItem(): MediaItem {
        return MediaItem.Builder()
            .setMediaId(id)
            .setUri(Uri.parse(uri))
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(title)
                    .setArtist(author)
                    .setAlbumTitle(album)
                    .setIsPlayable(true)
                    .build(),
            )
            .build()
    }

    companion object {
        private const val SEEK_INCREMENT_MS = 10_000L
        private const val PERIODIC_SAVE_INCREMENT_MS = 5_000L
        private const val COMPLETION_THRESHOLD_MS = 30_000L
        private const val RESTORE_POSITION_TOLERANCE_MS = 1_500L
    }
}
