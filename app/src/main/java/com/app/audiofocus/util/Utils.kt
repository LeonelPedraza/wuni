package com.app.audiofocus.util

import android.Manifest
import android.content.Context
import android.provider.MediaStore
import android.os.Build
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun formatDuration(ms: Long): String {
    val totalSeconds = ms / 1_000
    val hours = totalSeconds / 3_600
    val minutes = (totalSeconds % 3_600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        "%02d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%02d:%02d".format(minutes, seconds)
    }
}

fun nowIsoString(): String {
    return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(Instant.now().atOffset(ZoneOffset.UTC))
}

fun requiredAudioPermission(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
}

fun currentMediaStoreVersion(context: Context): String {
    return MediaStore.getVersion(context)
}
