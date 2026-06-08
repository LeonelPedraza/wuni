package com.app.audiofocus.di

import android.content.Context
import androidx.room.Room
import com.app.audiofocus.data.local.MIGRATION_1_2
import com.app.audiofocus.data.local.AppDatabase
import com.app.audiofocus.data.local.dao.AppSettingsDao
import com.app.audiofocus.data.local.dao.AudiobookDao
import com.app.audiofocus.data.local.dao.PlaybackProgressDao
import com.app.audiofocus.data.local.dao.ScanStateDao
import com.app.audiofocus.data.repository.DefaultAudiobookRepository
import com.app.audiofocus.data.repository.DefaultPlaybackProgressRepository
import com.app.audiofocus.data.repository.DefaultScanRepository
import com.app.audiofocus.domain.repository.AudiobookRepository
import com.app.audiofocus.domain.repository.PlaybackProgressRepository
import com.app.audiofocus.domain.repository.ScanRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "audiofocus.db")
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    fun provideAudiobookDao(database: AppDatabase): AudiobookDao = database.audiobookDao()

    @Provides
    fun providePlaybackProgressDao(database: AppDatabase): PlaybackProgressDao = database.playbackProgressDao()

    @Provides
    fun provideScanStateDao(database: AppDatabase): ScanStateDao = database.scanStateDao()

    @Provides
    fun provideAppSettingsDao(database: AppDatabase): AppSettingsDao = database.appSettingsDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAudiobookRepository(impl: DefaultAudiobookRepository): AudiobookRepository

    @Binds
    abstract fun bindPlaybackProgressRepository(impl: DefaultPlaybackProgressRepository): PlaybackProgressRepository

    @Binds
    abstract fun bindScanRepository(impl: DefaultScanRepository): ScanRepository
}
