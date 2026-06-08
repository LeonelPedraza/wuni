package com.app.audiofocus.di;

import com.app.audiofocus.data.local.AppDatabase;
import com.app.audiofocus.data.local.dao.AudiobookDao;
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
public final class DatabaseModule_ProvideAudiobookDaoFactory implements Factory<AudiobookDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideAudiobookDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public AudiobookDao get() {
    return provideAudiobookDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideAudiobookDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideAudiobookDaoFactory(databaseProvider);
  }

  public static AudiobookDao provideAudiobookDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAudiobookDao(database));
  }
}
