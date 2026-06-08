package com.app.audiofocus.data.repository

import android.app.RecoverableSecurityException
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import com.app.audiofocus.data.local.dao.AppSettingsDao
import com.app.audiofocus.data.local.dao.AudiobookDao
import com.app.audiofocus.data.local.dao.PlaybackProgressDao
import com.app.audiofocus.data.local.dao.ScanStateDao
import com.app.audiofocus.data.local.entity.AppSettingsEntity
import com.app.audiofocus.data.local.entity.AudiobookEntity
import com.app.audiofocus.data.local.entity.PlaybackProgressEntity
import com.app.audiofocus.data.local.entity.ScanStateEntity
import com.app.audiofocus.domain.model.AudioCandidate
import com.app.audiofocus.domain.model.AudioCandidateForScoring
import com.app.audiofocus.domain.model.Audiobook
import com.app.audiofocus.domain.model.CandidateGroup
import com.app.audiofocus.domain.model.DiscoveryClassification
import com.app.audiofocus.domain.model.PlaybackProgress
import com.app.audiofocus.domain.model.ScanProgress
import com.app.audiofocus.domain.model.ScanSummary
import com.app.audiofocus.domain.model.ScanStateSnapshot
import com.app.audiofocus.domain.repository.AudiobookRepository
import com.app.audiofocus.domain.repository.PermanentDeletePreparation
import com.app.audiofocus.domain.repository.PlaybackProgressRepository
import com.app.audiofocus.domain.repository.ScanRepository
import com.app.audiofocus.domain.usecase.CalculateAudiobookDiscoveryScoreUseCase
import com.app.audiofocus.util.nowIsoString
import com.app.audiofocus.util.requiredAudioPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultAudiobookRepository @Inject constructor(
    private val appContext: Context,
    private val audiobookDao: AudiobookDao,
) : AudiobookRepository {

    override fun observeVisibleAudiobooks(): Flow<List<Audiobook>> {
        return audiobookDao.observeVisibleAudiobooks().map { list -> list.map { it.toDomain() } }
    }

    override fun observeHiddenAudiobooks(): Flow<List<Audiobook>> {
        return audiobookDao.observeHiddenAudiobooks().map { list -> list.map { it.toDomain() } }
    }

    override fun observeAudiobook(id: String): Flow<Audiobook?> {
        return audiobookDao.observeAudiobook(id).map { entity -> entity?.toDomain() }
    }

    override fun observeHiddenCount(): Flow<Int> = audiobookDao.observeHiddenCount()

    override suspend fun clearSampleData() {
        audiobookDao.deleteSampleRows()
    }

    override suspend fun upsertScannedAudiobooks(audiobooks: List<Audiobook>) {
        val now = nowIsoString()
        audiobookDao.upsertAll(
            audiobooks.map { item ->
                AudiobookEntity(
                    id = item.id,
                    mediaStoreId = item.mediaStoreId,
                    uri = item.uri,
                    fingerprint = item.id,
                    displayName = item.displayName,
                    title = item.title,
                    author = item.author,
                    album = item.album,
                    durationMs = item.durationMs,
                    sizeBytes = item.sizeBytes,
                    mimeType = item.mimeType,
                    relativePath = item.relativePath,
                    dateModified = item.dateModified,
                    coverUri = null,
                    classification = item.classification,
                    discoveryScore = item.discoveryScore,
                    isFavorite = item.isFavorite,
                    visibilityStatus = item.visibilityStatus,
                    userDecision = item.userDecision,
                    createdAt = now,
                    updatedAt = now,
                )
            },
        )
    }

    override suspend fun setFavorite(id: String, isFavorite: Boolean) {
        audiobookDao.setFavorite(id = id, isFavorite = isFavorite, updatedAt = nowIsoString())
    }

    override suspend fun hideAudiobook(id: String) {
        audiobookDao.setVisibility(
            id = id,
            visibilityStatus = "hidden",
            userDecision = "hidden",
            updatedAt = nowIsoString(),
        )
    }

    override suspend fun restoreAudiobook(id: String) {
        audiobookDao.setVisibility(
            id = id,
            visibilityStatus = "active",
            userDecision = null,
            updatedAt = nowIsoString(),
        )
    }

    override suspend fun preparePermanentDelete(id: String): PermanentDeletePreparation {
        val audiobook = audiobookDao.getAudiobook(id) ?: error("Audiobook not found: $id")
        val uri = Uri.parse(audiobook.uri)
        val resolver = appContext.contentResolver

        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                val request = MediaStore.createDeleteRequest(resolver, listOf(uri))
                PermanentDeletePreparation.NeedsSystemConfirmation(
                    intentSender = request.intentSender,
                    retryDeleteAfterConfirmation = false,
                )
            }

            Build.VERSION.SDK_INT == Build.VERSION_CODES.Q -> {
                try {
                    val rowsDeleted = resolver.delete(uri, null, null)
                    if (rowsDeleted > 0) {
                        completePermanentDelete(id)
                        PermanentDeletePreparation.DeletedImmediately
                    } else {
                        error("MediaStore delete returned 0 rows for $id")
                    }
                } catch (error: RecoverableSecurityException) {
                    PermanentDeletePreparation.NeedsSystemConfirmation(
                        intentSender = error.userAction.actionIntent.intentSender,
                        retryDeleteAfterConfirmation = true,
                    )
                }
            }

            else -> {
                val rowsDeleted = resolver.delete(uri, null, null)
                if (rowsDeleted > 0) {
                    completePermanentDelete(id)
                    PermanentDeletePreparation.DeletedImmediately
                } else {
                    error("MediaStore delete returned 0 rows for $id")
                }
            }
        }
    }

    override suspend fun completePermanentDelete(id: String) {
        audiobookDao.markDeleted(id = id, updatedAt = nowIsoString())
    }

    override suspend fun retryPermanentDelete(id: String): Boolean {
        val audiobook = audiobookDao.getAudiobook(id) ?: return false
        val rowsDeleted = appContext.contentResolver.delete(Uri.parse(audiobook.uri), null, null)
        if (rowsDeleted > 0) {
            completePermanentDelete(id)
            return true
        }
        return false
    }

    private fun AudiobookEntity.toDomain(): Audiobook {
        return Audiobook(
            id = id,
            mediaStoreId = mediaStoreId,
            uri = uri,
            displayName = displayName,
            title = title,
            author = author,
            album = album,
            durationMs = durationMs,
            sizeBytes = sizeBytes,
            mimeType = mimeType,
            relativePath = relativePath,
            dateModified = dateModified,
            classification = classification,
            discoveryScore = discoveryScore,
            isFavorite = isFavorite,
            visibilityStatus = visibilityStatus,
            userDecision = userDecision,
        )
    }
}

@Singleton
class DefaultPlaybackProgressRepository @Inject constructor(
    private val playbackProgressDao: PlaybackProgressDao,
) : PlaybackProgressRepository {

    override fun observeAllProgress(): Flow<List<PlaybackProgress>> {
        return playbackProgressDao.observeAll().map { list -> list.map { it.toDomain() } }
    }

    override fun observeProgress(audiobookId: String): Flow<PlaybackProgress?> {
        return playbackProgressDao.observeByAudiobookId(audiobookId).map { it?.toDomain() }
    }

    override suspend fun getProgress(audiobookId: String): PlaybackProgress? {
        return playbackProgressDao.getByAudiobookId(audiobookId)?.toDomain()
    }

    override suspend fun saveProgress(progress: PlaybackProgress) {
        playbackProgressDao.upsert(
            PlaybackProgressEntity(
                audiobookId = progress.audiobookId,
                positionMs = progress.positionMs,
                durationMs = progress.durationMs,
                progressPercent = progress.progressPercent,
                status = progress.status,
                lastPlayedAt = progress.lastPlayedAt,
                updatedAt = nowIsoString(),
            ),
        )
    }

    private fun PlaybackProgressEntity.toDomain(): PlaybackProgress {
        return PlaybackProgress(
            audiobookId = audiobookId,
            positionMs = positionMs,
            durationMs = durationMs,
            progressPercent = progressPercent,
            status = status,
            lastPlayedAt = lastPlayedAt,
        )
    }
}

@Singleton
class DefaultScanRepository @Inject constructor(
    private val appContext: Context,
    private val audiobookDao: AudiobookDao,
    private val appSettingsDao: AppSettingsDao,
    private val scanStateDao: ScanStateDao,
    private val discoveryScore: CalculateAudiobookDiscoveryScoreUseCase,
) : ScanRepository {

    override fun observeInitialScanCompleted(): Flow<Boolean> {
        return scanStateDao.observeById("main").map { it?.lastScanAt != null }
    }

    override fun observeScanState(): Flow<ScanStateSnapshot?> {
        return scanStateDao.observeById("main").map { it?.toSnapshot() }
    }

    override fun scanAudiobooks(): Flow<ScanProgress> = flow {
        seedDefaults()
        if (!hasAudioPermission()) {
            emit(ScanProgress(stage = "permission_missing"))
            return@flow
        }

        audiobookDao.deleteSampleRows()
        val currentMediaStoreVersion = MediaStore.getVersion(appContext)
        val existingState = scanStateDao.getById("main")
        if (
            existingState != null &&
            existingState.lastMediaStoreVersion == currentMediaStoreVersion &&
            existingState.lastBackgroundSyncAt != null
        ) {
            emit(
                ScanProgress(
                    totalDiscovered = existingState.totalFound,
                    analyzedCount = existingState.totalFound,
                    acceptedCount = existingState.totalAdded,
                    stage = "completed",
                ),
            )
            return@flow
        }

        emit(ScanProgress(stage = "querying"))
        val candidates = queryCandidates()
        val groupedCandidates = buildCandidateGroups(candidates)
        val existingAudiobooks = audiobookDao.getLibraryAudiobooks()
        val existingByMediaStoreId = existingAudiobooks.associateBy { it.mediaStoreId }
        val existingByKey = existingAudiobooks.associateBy { it.reconciliationKey() }
        val acceptedEntities = ArrayList<AudiobookEntity>(candidates.size)
        var addedCount = 0
        var updatedCount = 0

        emit(
            ScanProgress(
                totalDiscovered = candidates.size,
                analyzedCount = 0,
                acceptedCount = 0,
                stage = "analyzing",
            ),
        )

        candidates.forEachIndexed { index, candidate ->
            val result = discoveryScore.execute(
                AudioCandidateForScoring(
                    displayName = candidate.displayName,
                    title = candidate.title,
                    artist = candidate.artist,
                    album = candidate.album,
                    durationMs = candidate.durationMs,
                    relativePath = candidate.relativePath,
                    mimeType = candidate.mimeType,
                    sizeBytes = candidate.sizeBytes,
                    group = groupedCandidates[candidate.mediaStoreId],
                    userDecision = null,
                ),
            )

            if (result.classification == DiscoveryClassification.AUDIOBOOK) {
                val matchedExisting = existingByMediaStoreId[candidate.mediaStoreId]
                    ?: existingByKey[candidate.reconciliationKey()]
                acceptedEntities += candidate.toEntity(
                    score = result.score,
                    classification = "audiobook",
                    existing = matchedExisting,
                )
                if (matchedExisting == null) {
                    addedCount += 1
                } else {
                    updatedCount += 1
                }
            }

            if (index == candidates.lastIndex || index % 10 == 9) {
                emit(
                    ScanProgress(
                        totalDiscovered = candidates.size,
                        analyzedCount = index + 1,
                        acceptedCount = acceptedEntities.size,
                        lastTitle = candidate.title?.takeIf { it.isNotBlank() }
                            ?: candidate.displayName,
                        stage = "analyzing",
                    ),
                )
            }
        }

        audiobookDao.upsertAll(acceptedEntities)
        val acceptedIds = acceptedEntities.mapTo(hashSetOf()) { it.id }
        existingAudiobooks
            .asSequence()
            .filter { it.visibilityStatus == "active" }
            .filterNot { it.id in acceptedIds }
            .forEach { audiobookDao.deleteById(it.id) }

        scanStateDao.upsert(
            ScanStateEntity(
                id = "main",
                lastScanAt = nowIsoString(),
                lastBackgroundSyncAt = nowIsoString(),
                lastMediaStoreVersion = currentMediaStoreVersion,
                totalFound = candidates.size,
                totalAdded = addedCount,
                totalUpdated = updatedCount,
                totalHidden = 0,
            ),
        )

        emit(
            ScanProgress(
                totalDiscovered = candidates.size,
                analyzedCount = candidates.size,
                acceptedCount = acceptedEntities.size,
                lastTitle = acceptedEntities.lastOrNull()?.title,
                stage = "completed",
            ),
        )
    }.flowOn(Dispatchers.IO)

    override suspend fun previewScan(): ScanSummary {
        var finalProgress = ScanProgress(stage = "idle")
        scanAudiobooks().collect { progress ->
            finalProgress = progress
        }
        return ScanSummary(
            totalAnalyzed = finalProgress.analyzedCount,
            totalAccepted = finalProgress.acceptedCount,
            lastClassification = finalProgress.stage,
        )
    }

    private suspend fun seedDefaults() {
        if (appSettingsDao.count() == 0) {
            val now = nowIsoString()
            appSettingsDao.upsert(
                AppSettingsEntity(
                    id = "main",
                    skipSeconds = 10,
                    autoScanEnabled = true,
                    theme = "dark",
                    showPossibleAudiobooks = false,
                    createdAt = now,
                    updatedAt = now,
                ),
            )
        }
    }

    private fun hasAudioPermission(): Boolean {
        val permission = requiredAudioPermission()
        return ContextCompat.checkSelfPermission(appContext, permission) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    private fun queryCandidates(): List<AudioCandidate> {
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.RELATIVE_PATH,
            MediaStore.Audio.Media.DATE_MODIFIED,
        )
        val collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val sortOrder = "${MediaStore.Audio.Media.DATE_MODIFIED} DESC"
        val resolver = appContext.contentResolver
        return buildList {
            resolver.query(collection, projection, "${MediaStore.Audio.Media.DURATION} > 0", null, sortOrder)?.use { cursor ->
                val idIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val displayNameIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                val titleIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val albumIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val durationIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                val mimeTypeIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)
                val relativePathIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.RELATIVE_PATH)
                val dateModifiedIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idIndex)
                    add(
                        AudioCandidate(
                            mediaStoreId = id,
                            uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id.toString()).toString(),
                            displayName = cursor.getString(displayNameIndex),
                            title = cursor.getString(titleIndex),
                            artist = cursor.getString(artistIndex),
                            album = cursor.getString(albumIndex),
                            durationMs = cursor.getLong(durationIndex),
                            sizeBytes = cursor.getLong(sizeIndex),
                            mimeType = cursor.getString(mimeTypeIndex),
                            relativePath = cursor.getString(relativePathIndex),
                            dateModified = cursor.getLong(dateModifiedIndex),
                        ),
                    )
                }
            }
        }
    }

    private fun buildCandidateGroups(candidates: List<AudioCandidate>): Map<Long, CandidateGroup> {
        val byFolder = candidates.groupBy { it.relativePath.orEmpty().lowercase() }
        val byAlbum = candidates.groupBy { it.album.orEmpty().lowercase() }

        return candidates.associate { candidate ->
            val folderItems = byFolder[candidate.relativePath.orEmpty().lowercase()].orEmpty()
            val albumItems = byAlbum[candidate.album.orEmpty().lowercase()].orEmpty()
            val relatedItems = (folderItems + albumItems)
                .distinctBy { it.mediaStoreId }

            candidate.mediaStoreId to CandidateGroup(
                filesInSameFolder = folderItems.size,
                averageDurationMs = if (relatedItems.isEmpty()) {
                    candidate.durationMs
                } else {
                    relatedItems.map { it.durationMs }.average().toLong()
                },
                hasSequentialNames = relatedItems.map { it.displayName }.hasSequentialNames(),
                sharedAlbum = candidate.album?.isNotBlank() == true && albumItems.size >= 2,
                hasChapterPattern = relatedItems.any { item ->
                    item.displayName.containsChapterPattern() ||
                        item.title?.containsChapterPattern() == true
                },
            )
        }
    }

    private fun AudioCandidate.toEntity(
        score: Int,
        classification: String,
        existing: AudiobookEntity?,
    ): AudiobookEntity {
        val resolvedTitle = title?.takeIf { it.isNotBlank() } ?: displayName.substringBeforeLast('.')
        val fingerprint = buildString {
            append(mediaStoreId)
            append('|')
            append(relativePath.orEmpty())
            append('|')
            append(displayName)
            append('|')
            append(durationMs)
            append('|')
            append(sizeBytes ?: 0L)
        }

        return AudiobookEntity(
            id = existing?.id ?: fingerprint,
            mediaStoreId = mediaStoreId,
            uri = uri,
            fingerprint = existing?.fingerprint ?: fingerprint,
            displayName = displayName,
            title = resolvedTitle,
            author = artist,
            album = album,
            durationMs = durationMs,
            sizeBytes = sizeBytes,
            mimeType = mimeType,
            relativePath = relativePath,
            dateModified = dateModified,
            coverUri = existing?.coverUri,
            classification = classification,
            discoveryScore = score,
            isFavorite = existing?.isFavorite ?: false,
            visibilityStatus = existing?.visibilityStatus ?: "active",
            userDecision = existing?.userDecision,
            createdAt = existing?.createdAt ?: nowIsoString(),
            updatedAt = nowIsoString(),
        )
    }

    private fun AudioCandidate.reconciliationKey(): String {
        return buildString {
            append(normalizeKey(title?.takeIf { it.isNotBlank() } ?: displayName))
            append('|')
            append(durationMs / 1000L)
            append('|')
            append(sizeBytes ?: 0L)
            append('|')
            append(normalizeKey(artist))
            append('|')
            append(normalizeKey(album))
        }
    }

    private fun AudiobookEntity.reconciliationKey(): String {
        return buildString {
            append(normalizeKey(title.ifBlank { displayName }))
            append('|')
            append(durationMs / 1000L)
            append('|')
            append(sizeBytes ?: 0L)
            append('|')
            append(normalizeKey(author))
            append('|')
            append(normalizeKey(album))
        }
    }

    private fun normalizeKey(value: String?): String {
        return value.orEmpty()
            .lowercase()
            .replace(Regex("[^a-z0-9]+"), "")
    }

    private fun ScanStateEntity.toSnapshot(): ScanStateSnapshot {
        return ScanStateSnapshot(
            lastScanAt = lastScanAt,
            lastBackgroundSyncAt = lastBackgroundSyncAt,
            lastMediaStoreVersion = lastMediaStoreVersion,
        )
    }

    private fun List<String>.hasSequentialNames(): Boolean {
        val numbers = mapNotNull { name ->
            "(^|\\D)(\\d{1,3})(\\D|$)".toRegex().find(name)?.groupValues?.get(2)?.toIntOrNull()
        }.sorted()
        if (numbers.size < 2) return false
        return numbers.zipWithNext().any { (left, right) -> right - left == 1 }
    }

    private fun String.containsChapterPattern(): Boolean {
        val source = lowercase()
        return listOf("chapter", "capitulo", "capítulo", "parte", "part", "modulo", "módulo").any(source::contains)
    }
}
