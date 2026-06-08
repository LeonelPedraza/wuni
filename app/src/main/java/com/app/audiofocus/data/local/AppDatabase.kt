package com.app.audiofocus.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.app.audiofocus.data.local.dao.AppSettingsDao
import com.app.audiofocus.data.local.dao.AudiobookDao
import com.app.audiofocus.data.local.dao.PlaybackProgressDao
import com.app.audiofocus.data.local.dao.ScanStateDao
import com.app.audiofocus.data.local.entity.AppSettingsEntity
import com.app.audiofocus.data.local.entity.AudiobookEntity
import com.app.audiofocus.data.local.entity.PlaybackProgressEntity
import com.app.audiofocus.data.local.entity.ScanStateEntity

@Database(
    entities = [
        AudiobookEntity::class,
        PlaybackProgressEntity::class,
        ScanStateEntity::class,
        AppSettingsEntity::class,
    ],
    version = 2,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun audiobookDao(): AudiobookDao
    abstract fun playbackProgressDao(): PlaybackProgressDao
    abstract fun scanStateDao(): ScanStateDao
    abstract fun appSettingsDao(): AppSettingsDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE scan_state ADD COLUMN lastBackgroundSyncAt TEXT")
    }
}
