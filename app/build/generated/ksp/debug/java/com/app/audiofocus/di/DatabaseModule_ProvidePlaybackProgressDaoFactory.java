package com.app.audiofocus.di;

import com.app.audiofocus.data.local.AppDatabase;
import com.app.audiofocus.data.local.dao.PlaybackProgressDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class DatabaseModule_ProvidePlaybackProgressDaoFactory implements Factory<PlaybackProgressDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvidePlaybackProgressDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public PlaybackProgressDao get() {
    return providePlaybackProgressDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvidePlaybackProgressDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvidePlaybackProgressDaoFactory(databaseProvider);
  }

  public static PlaybackProgressDao providePlaybackProgressDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providePlaybackProgressDao(database));
  }
}
