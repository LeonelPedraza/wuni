package com.app.audiofocus.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.app.audiofocus.ui.navigation.AudioFocusNavHost

@Composable
fun AudioFocusApp() {
    val navController = rememberNavController()
    AudioFocusNavHost(navController = navController)
}
