package com.app.audiofocus.data.bootstrap;

import com.app.audiofocus.domain.repository.AudiobookRepository;
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
public final class AppBootstrapper_Factory implements Factory<AppBootstrapper> {
  private final Provider<AudiobookRepository> audiobookRepositoryProvider;

  public AppBootstrapper_Factory(Provider<AudiobookRepository> audiobookRepositoryProvider) {
    this.audiobookRepositoryProvider = audiobookRepositoryProvider;
  }

  @Override
  public AppBootstrapper get() {
    return newInstance(audiobookRepositoryProvider.get());
  }

  public static AppBootstrapper_Factory create(
      Provider<AudiobookRepository> audiobookRepositoryProvider) {
    return new AppBootstrapper_Factory(audiobookRepositoryProvider);
  }

  public static AppBootstrapper newInstance(AudiobookRepository audiobookRepository) {
    return new AppBootstrapper(audiobookRepository);
  }
}
