package com.app.audiofocus

import android.app.Application
import com.app.audiofocus.data.bootstrap.AppBootstrapper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AudioFocusApplication : Application() {

    @Inject
    lateinit var bootstrapper: AppBootstrapper

    override fun onCreate() {
        super.onCreate()
        bootstrapper.scheduleSeed()
    }
}
