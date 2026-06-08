package com.app.audiofocus.player;

import android.content.Context;
import com.app.audiofocus.domain.repository.PlaybackProgressRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AudioFocusPlayerController_Factory implements Factory<AudioFocusPlayerController> {
  private final Provider<Context> appContextProvider;

  private final Provider<PlaybackProgressRepository> playbackProgressRepositoryProvider;

  public AudioFocusPlayerController_Factory(Provider<Context> appContextProvider,
      Provider<PlaybackProgressRepository> playbackProgressRepositoryProvider) {
    this.appContextProvider = appContextProvider;
    this.playbackProgressRepositoryProvider = playbackProgressRepositoryProvider;
  }

  @Override
  public AudioFocusPlayerController get() {
    return newInstance(appContextProvider.get(), playbackProgressRepositoryProvider.get());
  }

  public static AudioFocusPlayerController_Factory create(Provider<Context> appContextProvider,
      Provider<PlaybackProgressRepository> playbackProgressRepositoryProvider) {
    return new AudioFocusPlayerController_Factory(appContextProvider, playbackProgressRepositoryProvider);
  }

  public static AudioFocusPlayerController newInstance(Context appContext,
      PlaybackProgressRepository playbackProgressRepository) {
    return new AudioFocusPlayerController(appContext, playbackProgressRepository);
  }
}
