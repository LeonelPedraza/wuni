package com.app.audiofocus.data.repository;

import com.app.audiofocus.data.local.dao.PlaybackProgressDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class DefaultPlaybackProgressRepository_Factory implements Factory<DefaultPlaybackProgressRepository> {
  private final Provider<PlaybackProgressDao> playbackProgressDaoProvider;

  public DefaultPlaybackProgressRepository_Factory(
      Provider<PlaybackProgressDao> playbackProgressDaoProvider) {
    this.playbackProgressDaoProvider = playbackProgressDaoProvider;
  }

  @Override
  public DefaultPlaybackProgressRepository get() {
    return newInstance(playbackProgressDaoProvider.get());
  }

  public static DefaultPlaybackProgressRepository_Factory create(
      Provider<PlaybackProgressDao> playbackProgressDaoProvider) {
    return new DefaultPlaybackProgressRepository_Factory(playbackProgressDaoProvider);
  }

  public static DefaultPlaybackProgressRepository newInstance(
      PlaybackProgressDao playbackProgressDao) {
    return new DefaultPlaybackProgressRepository(playbackProgressDao);
  }
}
