package com.app.audiofocus.data.repository;

import android.content.Context;
import com.app.audiofocus.data.local.dao.AppSettingsDao;
import com.app.audiofocus.data.local.dao.AudiobookDao;
import com.app.audiofocus.data.local.dao.ScanStateDao;
import com.app.audiofocus.domain.usecase.CalculateAudiobookDiscoveryScoreUseCase;
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
public final class DefaultScanRepository_Factory implements Factory<DefaultScanRepository> {
  private final Provider<Context> appContextProvider;

  private final Provider<AudiobookDao> audiobookDaoProvider;

  private final Provider<AppSettingsDao> appSettingsDaoProvider;

  private final Provider<ScanStateDao> scanStateDaoProvider;

  private final Provider<CalculateAudiobookDiscoveryScoreUseCase> discoveryScoreProvider;

  public DefaultScanRepository_Factory(Provider<Context> appContextProvider,
      Provider<AudiobookDao> audiobookDaoProvider, Provider<AppSettingsDao> appSettingsDaoProvider,
      Provider<ScanStateDao> scanStateDaoProvider,
      Provider<CalculateAudiobookDiscoveryScoreUseCase> discoveryScoreProvider) {
    this.appContextProvider = appContextProvider;
    this.audiobookDaoProvider = audiobookDaoProvider;
    this.appSettingsDaoProvider = appSettingsDaoProvider;
    this.scanStateDaoProvider = scanStateDaoProvider;
    this.discoveryScoreProvider = discoveryScoreProvider;
  }

  @Override
  public DefaultScanRepository get() {
    return newInstance(appContextProvider.get(), audiobookDaoProvider.get(), appSettingsDaoProvider.get(), scanStateDaoProvider.get(), discoveryScoreProvider.get());
  }

  public static DefaultScanRepository_Factory create(Provider<Context> appContextProvider,
      Provider<AudiobookDao> audiobookDaoProvider, Provider<AppSettingsDao> appSettingsDaoProvider,
      Provider<ScanStateDao> scanStateDaoProvider,
      Provider<CalculateAudiobookDiscoveryScoreUseCase> discoveryScoreProvider) {
    return new DefaultScanRepository_Factory(appContextProvider, audiobookDaoProvider, appSettingsDaoProvider, scanStateDaoProvider, discoveryScoreProvider);
  }

  public static DefaultScanRepository newInstance(Context appContext, AudiobookDao audiobookDao,
      AppSettingsDao appSettingsDao, ScanStateDao scanStateDao,
      CalculateAudiobookDiscoveryScoreUseCase discoveryScore) {
    return new DefaultScanRepository(appContext, audiobookDao, appSettingsDao, scanStateDao, discoveryScore);
  }
}
