package com.app.audiofocus.ui.viewmodel;

import androidx.lifecycle.SavedStateHandle;
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
public final class PlayerViewModel_Factory implements Factory<PlayerViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<AudiobookRepository> audiobookRepositoryProvider;

  private final Provider<PlaybackProgressRepository> playbackProgressRepositoryProvider;

  private final Provider<AudioFocusPlayerController> playerControllerProvider;

  public PlayerViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<AudiobookRepository> audiobookRepositoryProvider,
      Provider<PlaybackProgressRepository> playbackProgressRepositoryProvider,
      Provider<AudioFocusPlayerController> playerControllerProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.audiobookRepositoryProvider = audiobookRepositoryProvider;
    this.playbackProgressRepositoryProvider = playbackProgressRepositoryProvider;
    this.playerControllerProvider = playerControllerProvider;
  }

  @Override
  public PlayerViewModel get() {
    return newInstance(savedStateHandleProvider.get(), audiobookRepositoryProvider.get(), playbackProgressRepositoryProvider.get(), playerControllerProvider.get());
  }

  public static PlayerViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<AudiobookRepository> audiobookRepositoryProvider,
      Provider<PlaybackProgressRepository> playbackProgressRepositoryProvider,
      Provider<AudioFocusPlayerController> playerControllerProvider) {
    return new PlayerViewModel_Factory(savedStateHandleProvider, audiobookRepositoryProvider, playbackProgressRepositoryProvider, playerControllerProvider);
  }

  public static PlayerViewModel newInstance(SavedStateHandle savedStateHandle,
      AudiobookRepository audiobookRepository,
      PlaybackProgressRepository playbackProgressRepository,
      AudioFocusPlayerController playerController) {
    return new PlayerViewModel(savedStateHandle, audiobookRepository, playbackProgressRepository, playerController);
  }
}
