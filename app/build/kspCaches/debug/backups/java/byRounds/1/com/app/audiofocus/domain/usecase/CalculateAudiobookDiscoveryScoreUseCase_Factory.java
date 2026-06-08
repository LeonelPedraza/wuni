package com.app.audiofocus.domain.usecase;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class CalculateAudiobookDiscoveryScoreUseCase_Factory implements Factory<CalculateAudiobookDiscoveryScoreUseCase> {
  @Override
  public CalculateAudiobookDiscoveryScoreUseCase get() {
    return newInstance();
  }

  public static CalculateAudiobookDiscoveryScoreUseCase_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CalculateAudiobookDiscoveryScoreUseCase newInstance() {
    return new CalculateAudiobookDiscoveryScoreUseCase();
  }

  private static final class InstanceHolder {
    private static final CalculateAudiobookDiscoveryScoreUseCase_Factory INSTANCE = new CalculateAudiobookDiscoveryScoreUseCase_Factory();
  }
}
