package com.app.audiofocus.data.bootstrap

import com.app.audiofocus.domain.repository.AudiobookRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppBootstrapper @Inject constructor(
    private val audiobookRepository: AudiobookRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun scheduleSeed() {
        scope.launch {
            audiobookRepository.clearSampleData()
        }
    }
}
