package com.app.audiofocus.ui.viewmodel;

import com.app.audiofocus.domain.repository.AudiobookRepository;
import com.app.audiofocus.domain.repository.PlaybackProgressRepository;
import com.app.audiofocus.player.AudioFocusPlayerController;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class LibraryViewModel_Factory implements Factory<LibraryViewModel> {
  private final Provider<AudiobookRepository> audiobookRepositoryProvider;

  private final Provider<PlaybackProgressRepository> playbackProgressRepositoryProvider;

  private final Provider<AudioFocusPlayerController> playerControllerProvider;

  public LibraryViewModel_Factory(Provider<AudiobookRepository> audiobookRepositoryProvider,
      Provider<PlaybackProgressRepository> playbackProgressRepositoryProvider,
      Provider<AudioFocusPlayerController> playerControllerProvider) {
    this.audiobookRepositoryProvider = audiobookRepositoryProvider;
    this.playbackProgressRepositoryProvider = playbackProgressRepositoryProvider;
    this.playerControllerProvider = playerControllerProvider;
  }

  @Override
  public LibraryViewModel get() {
    return newInstance(audiobookRepositoryProvider.get(), playbackProgressRepositoryProvider.get(), playerControllerProvider.get());
  }

  public static LibraryViewModel_Factory create(
      Provider<AudiobookRepository> audiobookRepositoryProvider,
      Provider<PlaybackProgressRepository> playbackProgressRepositoryProvider,
      Provider<AudioFocusPlayerController> playerControllerProvider) {
    return new LibraryViewModel_Factory(audiobookRepositoryProvider, playbackProgressRepositoryProvider, playerControllerProvider);
  }

  public static LibraryViewModel newInstance(AudiobookRepository audiobookRepository,
      PlaybackProgressRepository playbackProgressRepository,
      AudioFocusPlayerController playerController) {
    return new LibraryViewModel(audiobookRepository, playbackProgressRepository, playerController);
  }
}
