package com.app.audiofocus.domain.repository

import android.content.IntentSender
import com.app.audiofocus.domain.model.Audiobook
import com.app.audiofocus.domain.model.PlaybackProgress
import com.app.audiofocus.domain.model.ScanProgress
import com.app.audiofocus.domain.model.ScanSummary
import com.app.audiofocus.domain.model.ScanStateSnapshot
import kotlinx.coroutines.flow.Flow

sealed interface PermanentDeletePreparation {
    data object DeletedImmediately : PermanentDeletePreparation
    data class NeedsSystemConfirmation(
        val intentSender: IntentSender,
        val retryDeleteAfterConfirmation: Boolean,
    ) : PermanentDeletePreparation
}

interface AudiobookRepository {
    fun observeVisibleAudiobooks(): Flow<List<Audiobook>>
    fun observeHiddenAudiobooks(): Flow<List<Audiobook>>
    fun observeAudiobook(id: String): Flow<Audiobook?>
    fun observeHiddenCount(): Flow<Int>
    suspend fun clearSampleData()
    suspend fun upsertScannedAudiobooks(audiobooks: List<Audiobook>)
    suspend fun setFavorite(id: String, isFavorite: Boolean)
    suspend fun hideAudiobook(id: String)
    suspend fun restoreAudiobook(id: String)
    suspend fun preparePermanentDelete(id: String): PermanentDeletePreparation
    suspend fun completePermanentDelete(id: String)
    suspend fun retryPermanentDelete(id: String): Boolean
}

interface PlaybackProgressRepository {
    fun observeAllProgress(): Flow<List<PlaybackProgress>>
    fun observeProgress(audiobookId: String): Flow<PlaybackProgress?>
    suspend fun getProgress(audiobookId: String): PlaybackProgress?
    suspend fun saveProgress(progress: PlaybackProgress)
}

interface ScanRepository {
    fun observeInitialScanCompleted(): Flow<Boolean>
    fun observeScanState(): Flow<ScanStateSnapshot?>
    suspend fun previewScan(): ScanSummary
    fun scanAudiobooks(): Flow<ScanProgress>
}
