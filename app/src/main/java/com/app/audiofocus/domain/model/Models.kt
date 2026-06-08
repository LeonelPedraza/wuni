package com.app.audiofocus.domain.model

data class Audiobook(
    val id: String,
    val mediaStoreId: Long?,
    val uri: String,
    val displayName: String,
    val title: String,
    val author: String?,
    val album: String?,
    val durationMs: Long,
    val sizeBytes: Long?,
    val mimeType: String?,
    val relativePath: String?,
    val dateModified: Long?,
    val classification: String,
    val discoveryScore: Int,
    val isFavorite: Boolean,
    val visibilityStatus: String,
    val userDecision: String?,
)

data class PlaybackProgress(
    val audiobookId: String,
    val positionMs: Long,
    val durationMs: Long,
    val progressPercent: Int,
    val status: String,
    val lastPlayedAt: String?,
)

data class AudioCandidate(
    val mediaStoreId: Long,
    val uri: String,
    val displayName: String,
    val title: String?,
    val artist: String?,
    val album: String?,
    val durationMs: Long,
    val sizeBytes: Long?,
    val mimeType: String?,
    val relativePath: String?,
    val dateModified: Long?,
)

data class AudioCandidateForScoring(
    val displayName: String,
    val title: String?,
    val artist: String?,
    val album: String?,
    val durationMs: Long,
    val relativePath: String?,
    val mimeType: String?,
    val sizeBytes: Long?,
    val group: CandidateGroup?,
    val userDecision: String?,
)

data class CandidateGroup(
    val filesInSameFolder: Int,
    val averageDurationMs: Long,
    val hasSequentialNames: Boolean,
    val sharedAlbum: Boolean,
    val hasChapterPattern: Boolean,
)

enum class DiscoveryClassification {
    AUDIOBOOK,
    PROBABLE_AUDIOBOOK,
    UNKNOWN,
    MUSIC_OR_IGNORED,
    IGNORED,
}

data class DiscoveryResult(
    val score: Int,
    val classification: DiscoveryClassification,
    val reasons: List<String>,
)

data class ScanSummary(
    val totalAnalyzed: Int,
    val totalAccepted: Int,
    val lastClassification: String?,
)

data class ScanStateSnapshot(
    val lastScanAt: String?,
    val lastBackgroundSyncAt: String?,
    val lastMediaStoreVersion: String?,
)

data class ScanProgress(
    val totalDiscovered: Int = 0,
    val analyzedCount: Int = 0,
    val acceptedCount: Int = 0,
    val lastTitle: String? = null,
    val stage: String = "idle",
)
