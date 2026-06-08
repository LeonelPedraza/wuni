package com.app.audiofocus.prototype.media

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture

class AudioFocusPrototypePlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null
    private var player: ExoPlayer? = null

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_SPEECH)
                    .build(),
                true,
            )
            .build()
        mediaSession = MediaSession.Builder(this, player!!).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = mediaSession

    override fun onDestroy() {
        mediaSession?.release()
        player?.release()
        mediaSession = null
        player = null
        super.onDestroy()
    }

    fun loadPreviewItem(uri: Uri) {
        player?.apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
        }
    }
}

class AudioFocusPrototypeControllerConnector(context: Context) {
    private val sessionToken = SessionToken(
        context,
        ComponentName(context, AudioFocusPrototypePlaybackService::class.java),
    )

    val controllerFuture: ListenableFuture<MediaController> =
        MediaController.Builder(context, sessionToken).buildAsync()
}
