package com.app.audiofocus.ui.viewmodel

import android.content.IntentSender
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.audiofocus.domain.repository.AudiobookRepository
import com.app.audiofocus.domain.repository.PermanentDeletePreparation
import com.app.audiofocus.domain.repository.PlaybackProgressRepository
import com.app.audiofocus.domain.repository.ScanRepository
import com.app.audiofocus.player.AudioFocusPlayerController
import com.app.audiofocus.util.formatDuration
import com.app.audiofocus.util.currentMediaStoreVersion
import com.app.audiofocus.util.requiredAudioPermission
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class LibraryRowUi(
    val id: String,
    val audioUri: String,
    val title: String,
    val author: String?,
    val relativePath: String?,
    val durationMs: Long,
    val positionMs: Long,
    val formattedDuration: String,
    val progressPercent: Int,
    val playbackStatus: String,
    val isFavorite: Boolean,
    val lastPlayedAt: String?,
)

data class LibraryUiState(
    val audiobooks: List<LibraryRowUi> = emptyList(),
    val recentAudiobooks: List<LibraryRowUi> = emptyList(),
    val favoriteAudiobooks: List<LibraryRowUi> = emptyList(),
    val hiddenAudiobooks: List<LibraryRowUi> = emptyList(),
    val hiddenCount: Int = 0,
    val activePlaybackAudiobookId: String? = null,
    val activePlaybackIsPlaying: Boolean = false,
    val miniPlayerAudiobookId: String? = null,
)

data class PendingPermanentDeleteUi(
    val audiobookId: String,
    val title: String,
)

sealed interface LibraryEvent {
    data class LaunchPermanentDeleteConfirmation(
        val intentSender: IntentSender,
    ) : LibraryEvent
}

data class PlayerUiState(
    val audiobookId: String = "",
    val audioUri: String = "",
    val title: String = "",
    val author: String? = null,
    val relativePath: String? = null,
    val durationLabel: String = "00:00",
    val positionLabel: String = "00:00",
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = true,
    val playbackStatus: String = "not_started",
    val isFavorite: Boolean = false,
    val progressPercent: Int = 0,
    val lastPlayedAt: String? = null,
)

data class ScanUiState(
    val hasPermission: Boolean = false,
    val hasCompletedInitialScan: Boolean = false,
    val lastMediaStoreVersion: String? = null,
    val lastBackgroundSyncAt: String? = null,
    val needsBackgroundSync: Boolean = false,
    val totalDiscovered: Int = 0,
    val totalAnalyzed: Int = 0,
    val totalAccepted: Int = 0,
    val lastClassification: String? = null,
    val currentTitle: String? = null,
    val isScanning: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val audiobookRepository: AudiobookRepository,
    playbackProgressRepository: PlaybackProgressRepository,
    private val playerController: AudioFocusPlayerController,
) : ViewModel() {
    private data class PendingSystemDelete(
        val audiobookId: String,
        val retryDeleteAfterConfirmation: Boolean,
    )

    private val _events = MutableSharedFlow<LibraryEvent>()
    val events = _events.asSharedFlow()

    private var pendingSystemDelete: PendingSystemDelete? = null

    var pendingPermanentDelete by mutableStateOf<PendingPermanentDeleteUi?>(null)
        private set

    var deleteErrorMessage by mutableStateOf<String?>(null)
        private set

    val uiState: StateFlow<LibraryUiState> = combine(
        audiobookRepository.observeVisibleAudiobooks(),
        audiobookRepository.observeHiddenAudiobooks(),
        playbackProgressRepository.observeAllProgress(),
        audiobookRepository.observeHiddenCount(),
        playerController.playerState,
    ) { books, hiddenBooks, progressItems, hiddenCount, playerState ->
        val progressById = progressItems.associateBy { it.audiobookId }
        fun mapRow(book: com.app.audiofocus.domain.model.Audiobook): LibraryRowUi {
            val progress = progressById[book.id]
            return LibraryRowUi(
                id = book.id,
                audioUri = book.uri,
                title = book.title,
                author = book.author,
                relativePath = book.relativePath,
                durationMs = book.durationMs,
                positionMs = progress?.positionMs ?: 0L,
                formattedDuration = formatDuration(book.durationMs),
                progressPercent = progress?.progressPercent ?: 0,
                playbackStatus = progress?.status ?: "not_started",
                isFavorite = book.isFavorite,
                lastPlayedAt = progress?.lastPlayedAt,
            )
        }
        val mapped = books.map(::mapRow)
        val recent = mapped
            .filter { it.lastPlayedAt != null }
            .sortedByDescending { it.lastPlayedAt }
        val favorites = mapped.filter { it.isFavorite }
        val hiddenMapped = hiddenBooks.map(::mapRow)
        val miniPlayerAudiobookId = playerState.mediaId ?: recent.firstOrNull()?.id
        LibraryUiState(
            audiobooks = mapped,
            recentAudiobooks = recent,
            favoriteAudiobooks = favorites,
            hiddenAudiobooks = hiddenMapped,
            hiddenCount = hiddenCount,
            activePlaybackAudiobookId = playerState.mediaId,
            activePlaybackIsPlaying = playerState.isPlaying,
            miniPlayerAudiobookId = miniPlayerAudiobookId,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LibraryUiState(),
    )

    fun toggleFavorite(item: LibraryRowUi) {
        viewModelScope.launch {
            audiobookRepository.setFavorite(item.id, !item.isFavorite)
        }
    }

    fun hideAudiobook(id: String) {
        viewModelScope.launch {
            audiobookRepository.hideAudiobook(id)
        }
    }

    fun restoreAudiobook(id: String) {
        viewModelScope.launch {
            audiobookRepository.restoreAudiobook(id)
        }
    }

    fun playOrPauseAudiobook(item: LibraryRowUi) {
        viewModelScope.launch {
            val playerState = playerController.playerState.value
            if (playerState.mediaId == item.id) {
                playerController.togglePlayPause()
            } else {
                audiobookRepository.observeAudiobook(item.id).first()?.let { audiobook ->
                    playerController.playAudiobook(audiobook)
                }
            }
        }
    }

    fun seekForwardAudiobook(item: LibraryRowUi) {
        viewModelScope.launch {
            val playerState = playerController.playerState.value
            if (playerState.mediaId == item.id) {
                playerController.seekForward()
            } else {
                audiobookRepository.observeAudiobook(item.id).first()?.let { audiobook ->
                    playerController.playAudiobook(audiobook)
                    playerController.seekForward()
                }
            }
        }
    }

    fun seekBackAudiobook(item: LibraryRowUi) {
        viewModelScope.launch {
            val playerState = playerController.playerState.value
            if (playerState.mediaId == item.id) {
                playerController.seekBack()
            } else {
                audiobookRepository.observeAudiobook(item.id).first()?.let { audiobook ->
                    playerController.playAudiobook(audiobook)
                    playerController.seekBack()
                }
            }
        }
    }

    fun requestPermanentDelete(item: LibraryRowUi) {
        pendingPermanentDelete = PendingPermanentDeleteUi(
            audiobookId = item.id,
            title = item.title,
        )
        deleteErrorMessage = null
    }

    fun deleteAudiobookPermanently(item: LibraryRowUi) {
        requestPermanentDelete(item)
        confirmPermanentDelete()
    }

    fun dismissPermanentDeleteDialog() {
        pendingPermanentDelete = null
    }

    fun clearDeleteError() {
        deleteErrorMessage = null
    }

    fun confirmPermanentDelete() {
        val request = pendingPermanentDelete ?: return
        pendingPermanentDelete = null
        viewModelScope.launch {
            deleteErrorMessage = null
            runCatching {
                playerController.stopPlaybackForAudiobook(request.audiobookId)
                audiobookRepository.preparePermanentDelete(request.audiobookId)
            }.onSuccess { result ->
                when (result) {
                    PermanentDeletePreparation.DeletedImmediately -> {
                        pendingSystemDelete = null
                    }

                    is PermanentDeletePreparation.NeedsSystemConfirmation -> {
                        pendingSystemDelete = PendingSystemDelete(
                            audiobookId = request.audiobookId,
                            retryDeleteAfterConfirmation = result.retryDeleteAfterConfirmation,
                        )
                        _events.emit(
                            LibraryEvent.LaunchPermanentDeleteConfirmation(
                                intentSender = result.intentSender,
                            ),
                        )
                    }
                }
            }.onFailure {
                deleteErrorMessage = "No se pudo preparar la eliminacion permanente."
            }
        }
    }

    fun onPermanentDeleteResult(confirmed: Boolean) {
        val pending = pendingSystemDelete ?: return
        pendingSystemDelete = null
        if (!confirmed) return
        viewModelScope.launch {
            runCatching {
                if (pending.retryDeleteAfterConfirmation) {
                    check(audiobookRepository.retryPermanentDelete(pending.audiobookId))
                } else {
                    audiobookRepository.completePermanentDelete(pending.audiobookId)
                }
            }.onFailure {
                deleteErrorMessage = "No se pudo eliminar el archivo."
            }
        }
    }
}

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val scanRepository: ScanRepository,
    @ApplicationContext private val appContext: Context,
) : ViewModel() {
    var uiState by mutableStateOf(ScanUiState(hasPermission = hasPermission()))
        private set

    init {
        viewModelScope.launch {
            scanRepository.observeScanState().collect { snapshot ->
                val currentVersion = com.app.audiofocus.util.currentMediaStoreVersion(appContext)
                uiState = uiState.copy(
                    hasCompletedInitialScan = snapshot?.lastScanAt != null,
                    lastMediaStoreVersion = snapshot?.lastMediaStoreVersion,
                    lastBackgroundSyncAt = snapshot?.lastBackgroundSyncAt,
                    needsBackgroundSync = snapshot?.lastScanAt != null &&
                        snapshot?.lastMediaStoreVersion != currentVersion,
                )
            }
        }
    }

    fun onPermissionResult(granted: Boolean) {
        uiState = uiState.copy(
            hasPermission = granted,
            errorMessage = if (granted) null else "permission_missing",
        )
    }

    fun refreshPermissionState() {
        uiState = uiState.copy(hasPermission = hasPermission())
    }

    fun runPreviewScan() {
        if (!hasPermission()) {
            uiState = uiState.copy(
                hasPermission = false,
                errorMessage = "permission_missing",
                lastClassification = "permission_missing",
            )
            return
        }
        viewModelScope.launch {
            uiState = uiState.copy(isScanning = true, errorMessage = null, hasPermission = true)
            scanRepository.scanAudiobooks().collect { progress ->
            uiState = uiState.copy(
                hasPermission = progress.stage != "permission_missing",
                hasCompletedInitialScan = uiState.hasCompletedInitialScan || progress.stage == "completed",
                needsBackgroundSync = if (progress.stage == "completed") false else uiState.needsBackgroundSync,
                totalDiscovered = progress.totalDiscovered,
                totalAnalyzed = progress.analyzedCount,
                totalAccepted = progress.acceptedCount,
                lastClassification = progress.stage,
                    currentTitle = progress.lastTitle,
                    isScanning = progress.stage == "querying" || progress.stage == "analyzing",
                    errorMessage = if (progress.stage == "permission_missing") "permission_missing" else null,
                )
            }
        }
    }

    fun openAppSettingsIntent() = android.content.Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        android.net.Uri.fromParts("package", appContext.packageName, null),
    )

    private fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            requiredAudioPermission(),
        ) == PackageManager.PERMISSION_GRANTED
    }
}

@HiltViewModel
class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val audiobookRepository: AudiobookRepository,
    playbackProgressRepository: PlaybackProgressRepository,
    private val playerController: AudioFocusPlayerController,
) : ViewModel() {
    private val audiobookId: String = checkNotNull(savedStateHandle["audiobookId"])

    val uiState: StateFlow<PlayerUiState> = combine(
        audiobookRepository.observeAudiobook(audiobookId),
        playbackProgressRepository.observeProgress(audiobookId),
        playerController.playerState,
    ) { audiobook, progress, playback ->
        val effectiveDuration = playback.durationMs.takeIf { it > 0 } ?: (audiobook?.durationMs ?: 0L)
        PlayerUiState(
            audiobookId = audiobookId,
            audioUri = audiobook?.uri.orEmpty(),
            title = playback.title ?: audiobook?.title.orEmpty(),
            author = audiobook?.author ?: playback.author,
            relativePath = audiobook?.relativePath,
            durationLabel = formatDuration(effectiveDuration),
            positionLabel = formatDuration(playback.positionMs),
            positionMs = playback.positionMs.coerceAtMost(effectiveDuration.coerceAtLeast(0L)),
            durationMs = effectiveDuration,
            isPlaying = playback.isPlaying && playback.mediaId == audiobookId,
            isLoading = audiobook == null || playback.isConnecting,
            playbackStatus = if (playback.mediaId == audiobookId) {
                playback.playbackStatus
            } else {
                progress?.status ?: "not_started"
            },
            isFavorite = audiobook?.isFavorite ?: false,
            progressPercent = progress?.progressPercent ?: when {
                effectiveDuration <= 0L -> 0
                else -> ((playback.positionMs.coerceAtLeast(0L) * 100L) / effectiveDuration).toInt().coerceIn(0, 100)
            },
            lastPlayedAt = progress?.lastPlayedAt,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PlayerUiState(isLoading = true),
    )

    fun loadAndPlay() {
        viewModelScope.launch {
            audiobookRepository.observeAudiobook(audiobookId).first()?.let { audiobook ->
                playerController.playAudiobook(audiobook)
            }
        }
    }

    fun togglePlayPause() {
        viewModelScope.launch {
            playerController.togglePlayPause()
        }
    }

    fun seekTo(positionMs: Long) {
        viewModelScope.launch {
            playerController.seekTo(positionMs)
        }
    }

    fun seekForward() {
        viewModelScope.launch {
            playerController.seekForward()
        }
    }

    fun seekBack() {
        viewModelScope.launch {
            playerController.seekBack()
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            audiobookRepository.observeAudiobook(audiobookId).first()?.let { audiobook ->
                audiobookRepository.setFavorite(audiobookId, !audiobook.isFavorite)
            }
        }
    }
}
