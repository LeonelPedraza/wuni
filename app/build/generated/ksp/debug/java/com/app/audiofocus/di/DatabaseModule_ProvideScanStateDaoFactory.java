package com.app.audiofocus.di;

import com.app.audiofocus.data.local.AppDatabase;
import com.app.audiofocus.data.local.dao.ScanStateDao;
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
public final class DatabaseModule_ProvideScanStateDaoFactory implements Factory<ScanStateDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideScanStateDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ScanStateDao get() {
    return provideScanStateDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideScanStateDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideScanStateDaoFactory(databaseProvider);
  }

  public static ScanStateDao provideScanStateDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideScanStateDao(database));
  }
}
