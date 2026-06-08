package com.app.audiofocus;

import com.app.audiofocus.data.bootstrap.AppBootstrapper;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class AudioFocusApplication_MembersInjector implements MembersInjector<AudioFocusApplication> {
  private final Provider<AppBootstrapper> bootstrapperProvider;

  public AudioFocusApplication_MembersInjector(Provider<AppBootstrapper> bootstrapperProvider) {
    this.bootstrapperProvider = bootstrapperProvider;
  }

  public static MembersInjector<AudioFocusApplication> create(
      Provider<AppBootstrapper> bootstrapperProvider) {
    return new AudioFocusApplication_MembersInjector(bootstrapperProvider);
  }

  @Override
  public void injectMembers(AudioFocusApplication instance) {
    injectBootstrapper(instance, bootstrapperProvider.get());
  }

  @InjectedFieldSignature("com.app.audiofocus.AudioFocusApplication.bootstrapper")
  public static void injectBootstrapper(AudioFocusApplication instance,
      AppBootstrapper bootstrapper) {
    instance.bootstrapper = bootstrapper;
  }
}
