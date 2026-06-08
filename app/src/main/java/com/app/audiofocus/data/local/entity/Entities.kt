package com.app.audiofocus.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audiobooks")
data class AudiobookEntity(
    @PrimaryKey val id: String,
    val mediaStoreId: Long?,
    val uri: String,
    val fingerprint: String,
    val displayName: String,
    val title: String,
    val author: String?,
    val album: String?,
    val durationMs: Long,
    val sizeBytes: Long?,
    val mimeType: String?,
    val relativePath: String?,
    val dateModified: Long?,
    val coverUri: String?,
    val classification: String,
    val discoveryScore: Int,
    val isFavorite: Boolean,
    val visibilityStatus: String,
    val userDecision: String?,
    val createdAt: String,
    val updatedAt: String,
)

@Entity(tableName = "playback_progress")
data class PlaybackProgressEntity(
    @PrimaryKey val audiobookId: String,
    val positionMs: Long,
    val durationMs: Long,
    val progressPercent: Int,
    val status: String,
    val lastPlayedAt: String?,
    val updatedAt: String,
)

@Entity(tableName = "scan_state")
data class ScanStateEntity(
    @PrimaryKey val id: String,
    val lastScanAt: String?,
    val lastBackgroundSyncAt: String?,
    val lastMediaStoreVersion: String?,
    val totalFound: Int,
    val totalAdded: Int,
    val totalUpdated: Int,
    val totalHidden: Int,
)

@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey val id: String,
    val skipSeconds: Int,
    val autoScanEnabled: Boolean,
    val theme: String,
    val showPossibleAudiobooks: Boolean,
    val createdAt: String,
    val updatedAt: String,
)
