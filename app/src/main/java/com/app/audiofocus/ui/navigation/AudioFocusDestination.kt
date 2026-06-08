package com.app.audiofocus.ui.navigation

sealed class AudioFocusDestination(val route: String) {
    data object Launch : AudioFocusDestination("launch")
    data object Onboarding : AudioFocusDestination("onboarding")
    data object Permissions : AudioFocusDestination("permissions")
    data object Scanning : AudioFocusDestination("scanning")
    data object Library : AudioFocusDestination("library")
    data object Favorites : AudioFocusDestination("favorites")
    data object Player : AudioFocusDestination("player/{audiobookId}") {
        fun createRoute(audiobookId: String): String {
            return "player/${android.net.Uri.encode(audiobookId)}"
        }
    }
    data object Hidden : AudioFocusDestination("hidden")
    data object Settings : AudioFocusDestination("settings")
}
