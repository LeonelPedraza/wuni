package com.app.audiofocus.data.repository;

import android.content.Context;
import com.app.audiofocus.data.local.dao.AudiobookDao;
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
public final class DefaultAudiobookRepository_Factory implements Factory<DefaultAudiobookRepository> {
  private final Provider<Context> appContextProvider;

  private final Provider<AudiobookDao> audiobookDaoProvider;

  public DefaultAudiobookRepository_Factory(Provider<Context> appContextProvider,
      Provider<AudiobookDao> audiobookDaoProvider) {
    this.appContextProvider = appContextProvider;
    this.audiobookDaoProvider = audiobookDaoProvider;
  }

  @Override
  public DefaultAudiobookRepository get() {
    return newInstance(appContextProvider.get(), audiobookDaoProvider.get());
  }

  public static DefaultAudiobookRepository_Factory create(Provider<Context> appContextProvider,
      Provider<AudiobookDao> audiobookDaoProvider) {
    return new DefaultAudiobookRepository_Factory(appContextProvider, audiobookDaoProvider);
  }

  public static DefaultAudiobookRepository newInstance(Context appContext,
      AudiobookDao audiobookDao) {
    return new DefaultAudiobookRepository(appContext, audiobookDao);
  }
}
