package com.app.audiofocus.player

data class AudioFocusPlaybackState(
    val mediaId: String? = null,
    val title: String? = null,
    val author: String? = null,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val isPlaying: Boolean = false,
    val isConnecting: Boolean = true,
    val playbackStatus: String = "not_started",
)
