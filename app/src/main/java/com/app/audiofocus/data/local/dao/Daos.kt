package com.app.audiofocus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.audiofocus.data.local.entity.AppSettingsEntity
import com.app.audiofocus.data.local.entity.AudiobookEntity
import com.app.audiofocus.data.local.entity.PlaybackProgressEntity
import com.app.audiofocus.data.local.entity.ScanStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AudiobookDao {
    @Query("SELECT * FROM audiobooks WHERE visibilityStatus = 'active' AND COALESCE(userDecision, '') != 'deleted' ORDER BY updatedAt DESC")
    fun observeVisibleAudiobooks(): Flow<List<AudiobookEntity>>

    @Query("SELECT * FROM audiobooks WHERE visibilityStatus = 'hidden' AND COALESCE(userDecision, '') != 'deleted' ORDER BY updatedAt DESC")
    fun observeHiddenAudiobooks(): Flow<List<AudiobookEntity>>

    @Query("SELECT * FROM audiobooks WHERE COALESCE(userDecision, '') != 'deleted'")
    suspend fun getLibraryAudiobooks(): List<AudiobookEntity>

    @Query("DELETE FROM audiobooks WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM audiobooks WHERE id = :id LIMIT 1")
    fun observeAudiobook(id: String): Flow<AudiobookEntity?>

    @Query("SELECT * FROM audiobooks WHERE id = :id LIMIT 1")
    suspend fun getAudiobook(id: String): AudiobookEntity?

    @Query("SELECT COUNT(*) FROM audiobooks WHERE visibilityStatus = 'hidden'")
    fun observeHiddenCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM audiobooks")
    suspend fun count(): Int

    @Query("DELETE FROM audiobooks WHERE uri LIKE 'content://sample/%'")
    suspend fun deleteSampleRows()

    @Query("UPDATE audiobooks SET isFavorite = :isFavorite, updatedAt = :updatedAt WHERE id = :id")
    suspend fun setFavorite(id: String, isFavorite: Boolean, updatedAt: String)

    @Query("UPDATE audiobooks SET visibilityStatus = :visibilityStatus, userDecision = :userDecision, updatedAt = :updatedAt WHERE id = :id")
    suspend fun setVisibility(id: String, visibilityStatus: String, userDecision: String?, updatedAt: String)

    @Query("UPDATE audiobooks SET visibilityStatus = 'deleted', userDecision = 'deleted', isFavorite = 0, updatedAt = :updatedAt WHERE id = :id")
    suspend fun markDeleted(id: String, updatedAt: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<AudiobookEntity>)
}

@Dao
interface PlaybackProgressDao {
    @Query("SELECT * FROM playback_progress")
    fun observeAll(): Flow<List<PlaybackProgressEntity>>

    @Query("SELECT * FROM playback_progress WHERE audiobookId = :audiobookId LIMIT 1")
    fun observeByAudiobookId(audiobookId: String): Flow<PlaybackProgressEntity?>

    @Query("SELECT * FROM playback_progress WHERE audiobookId = :audiobookId LIMIT 1")
    suspend fun getByAudiobookId(audiobookId: String): PlaybackProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(progress: PlaybackProgressEntity)
}

@Dao
interface ScanStateDao {
    @Query("SELECT * FROM scan_state WHERE id = :id LIMIT 1")
    fun observeById(id: String): Flow<ScanStateEntity?>

    @Query("SELECT * FROM scan_state WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ScanStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(state: ScanStateEntity)
}

@Dao
interface AppSettingsDao {
    @Query("SELECT COUNT(*) FROM app_settings")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(settings: AppSettingsEntity)
}
