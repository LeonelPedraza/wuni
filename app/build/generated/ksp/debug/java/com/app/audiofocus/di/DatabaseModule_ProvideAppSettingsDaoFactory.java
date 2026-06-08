package com.app.audiofocus.di;

import com.app.audiofocus.data.local.AppDatabase;
import com.app.audiofocus.data.local.dao.AppSettingsDao;
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
public final class DatabaseModule_ProvideAppSettingsDaoFactory implements Factory<AppSettingsDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideAppSettingsDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public AppSettingsDao get() {
    return provideAppSettingsDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideAppSettingsDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideAppSettingsDaoFactory(databaseProvider);
  }

  public static AppSettingsDao provideAppSettingsDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAppSettingsDao(database));
  }
}
