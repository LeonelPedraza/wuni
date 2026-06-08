package com.app.audiofocus.ui.viewmodel;

import android.content.Context;
import com.app.audiofocus.domain.repository.ScanRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class ScanViewModel_Factory implements Factory<ScanViewModel> {
  private final Provider<ScanRepository> scanRepositoryProvider;

  private final Provider<Context> appContextProvider;

  public ScanViewModel_Factory(Provider<ScanRepository> scanRepositoryProvider,
      Provider<Context> appContextProvider) {
    this.scanRepositoryProvider = scanRepositoryProvider;
    this.appContextProvider = appContextProvider;
  }

  @Override
  public ScanViewModel get() {
    return newInstance(scanRepositoryProvider.get(), appContextProvider.get());
  }

  public static ScanViewModel_Factory create(Provider<ScanRepository> scanRepositoryProvider,
      Provider<Context> appContextProvider) {
    return new ScanViewModel_Factory(scanRepositoryProvider, appContextProvider);
  }

  public static ScanViewModel newInstance(ScanRepository scanRepository, Context appContext) {
    return new ScanViewModel(scanRepository, appContext);
  }
}
