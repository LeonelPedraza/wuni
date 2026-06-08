package com.app.audiofocus.ui.navigation

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.IntentSenderRequest
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.audiofocus.ui.screen.LaunchScreen
import com.app.audiofocus.ui.screen.HiddenScreen
import com.app.audiofocus.ui.screen.FavoritesScreen
import com.app.audiofocus.ui.screen.LibraryScreen
import com.app.audiofocus.ui.screen.OnboardingScreen
import com.app.audiofocus.ui.screen.PermissionsScreen
import com.app.audiofocus.ui.screen.PlayerScreen
import com.app.audiofocus.ui.screen.ScanningScreen
import com.app.audiofocus.ui.screen.SettingsTabScreen
import com.app.audiofocus.ui.viewmodel.LibraryEvent
import com.app.audiofocus.ui.viewmodel.LibraryViewModel
import com.app.audiofocus.ui.viewmodel.PlayerViewModel
import com.app.audiofocus.ui.viewmodel.ScanViewModel
import kotlinx.coroutines.delay

@Composable
fun AudioFocusNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AudioFocusDestination.Launch.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        composable(AudioFocusDestination.Launch.route) {
            LaunchScreen()
            val viewModel: ScanViewModel = hiltViewModel()
            val state = viewModel.uiState
            LaunchedEffect(state.hasPermission, state.hasCompletedInitialScan) {
                delay(850L)
                val destination = when {
                    state.hasCompletedInitialScan -> AudioFocusDestination.Library.route
                    state.hasPermission -> AudioFocusDestination.Scanning.route
                    else -> AudioFocusDestination.Onboarding.route
                }
                navController.navigate(destination) {
                    popUpTo(AudioFocusDestination.Launch.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
        composable(AudioFocusDestination.Onboarding.route) {
            OnboardingScreen(
                onContinue = { navController.navigate(AudioFocusDestination.Permissions.route) },
            )
        }
        composable(AudioFocusDestination.Permissions.route) {
            val viewModel: ScanViewModel = hiltViewModel()
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
            ) { granted ->
                viewModel.onPermissionResult(granted)
                if (granted) {
                    navController.navigate(AudioFocusDestination.Library.route) {
                        popUpTo(AudioFocusDestination.Permissions.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            LaunchedEffect(Unit) {
                viewModel.refreshPermissionState()
            }
            PermissionsScreen(
                hasPermission = viewModel.uiState.hasPermission,
                onGrantPermission = {
                    if (viewModel.uiState.hasPermission) {
                        val target = if (viewModel.uiState.hasCompletedInitialScan) {
                            AudioFocusDestination.Library.route
                        } else {
                            AudioFocusDestination.Scanning.route
                        }
                        navController.navigate(target) {
                            popUpTo(AudioFocusDestination.Permissions.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        permissionLauncher.launch(com.app.audiofocus.util.requiredAudioPermission())
                    }
                },
                onSkip = { navController.navigate(AudioFocusDestination.Library.route) },
                onOpenSettings = {
                    navController.context.startActivity(viewModel.openAppSettingsIntent())
                },
            )
        }
        composable(AudioFocusDestination.Scanning.route) {
            val viewModel: ScanViewModel = hiltViewModel()
            LaunchedEffect(Unit) {
                viewModel.refreshPermissionState()
                if (viewModel.uiState.hasPermission && viewModel.uiState.totalAnalyzed == 0) {
                    viewModel.runPreviewScan()
                }
            }
            LaunchedEffect(viewModel.uiState.lastClassification, viewModel.uiState.isScanning) {
                if (!viewModel.uiState.isScanning && viewModel.uiState.lastClassification == "completed") {
                    navController.navigate(AudioFocusDestination.Library.route) {
                        popUpTo(AudioFocusDestination.Scanning.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            ScanningScreen(
                state = viewModel.uiState,
                onRunScan = viewModel::runPreviewScan,
                onOpenLibrary = {
                    navController.navigate(AudioFocusDestination.Library.route) {
                        popUpTo(AudioFocusDestination.Scanning.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(AudioFocusDestination.Library.route) {
            val viewModel: LibraryViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle()
            val scanViewModel: ScanViewModel = hiltViewModel()
            val scanState = scanViewModel.uiState
            val deleteLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
            ) { result ->
                viewModel.onPermanentDeleteResult(result.resultCode == Activity.RESULT_OK)
            }
            LaunchedEffect(viewModel) {
                viewModel.events.collect { event ->
                    when (event) {
                        is LibraryEvent.LaunchPermanentDeleteConfirmation -> {
                            deleteLauncher.launch(
                                IntentSenderRequest.Builder(event.intentSender).build(),
                            )
                        }
                    }
                }
            }
            LaunchedEffect(scanState.hasPermission, scanState.needsBackgroundSync, scanState.isScanning) {
                if (scanState.hasPermission && scanState.needsBackgroundSync && !scanState.isScanning) {
                    scanViewModel.runPreviewScan()
                }
            }
            LibraryScreen(
                state = state.value,
                scanState = scanState,
                onOpenPlayer = { audiobookId ->
                    navController.navigate(AudioFocusDestination.Player.createRoute(audiobookId))
                },
                onPlayPauseAudiobook = viewModel::playOrPauseAudiobook,
                onSeekBackAudiobook = viewModel::seekBackAudiobook,
                onSeekForwardAudiobook = viewModel::seekForwardAudiobook,
                onToggleFavorite = viewModel::toggleFavorite,
                onHideAudiobook = viewModel::hideAudiobook,
                onDeleteAudiobook = viewModel::deleteAudiobookPermanently,
                deleteErrorMessage = viewModel.deleteErrorMessage,
                onDismissDeleteError = viewModel::clearDeleteError,
                onOpenLibrary = {},
                onOpenFavorites = {},
                onOpenSettings = {},
                onOpenHidden = { navController.navigate(AudioFocusDestination.Hidden.route) },
            )
        }
        composable(AudioFocusDestination.Favorites.route) {
            val viewModel: LibraryViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle()
            val scanViewModel: ScanViewModel = hiltViewModel()
            val scanState = scanViewModel.uiState
            val deleteLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
            ) { result ->
                viewModel.onPermanentDeleteResult(result.resultCode == Activity.RESULT_OK)
            }
            LaunchedEffect(viewModel) {
                viewModel.events.collect { event ->
                    when (event) {
                        is LibraryEvent.LaunchPermanentDeleteConfirmation -> {
                            deleteLauncher.launch(
                                IntentSenderRequest.Builder(event.intentSender).build(),
                            )
                        }
                    }
                }
            }
            FavoritesScreen(
                state = state.value,
                scanState = scanState,
                onOpenPlayer = { audiobookId ->
                    navController.navigate(AudioFocusDestination.Player.createRoute(audiobookId))
                },
                onPlayPauseAudiobook = viewModel::playOrPauseAudiobook,
                onSeekBackAudiobook = viewModel::seekBackAudiobook,
                onSeekForwardAudiobook = viewModel::seekForwardAudiobook,
                onToggleFavorite = viewModel::toggleFavorite,
                onHideAudiobook = viewModel::hideAudiobook,
                onDeleteAudiobook = viewModel::deleteAudiobookPermanently,
                deleteErrorMessage = viewModel.deleteErrorMessage,
                onDismissDeleteError = viewModel::clearDeleteError,
                onOpenLibrary = {},
                onOpenFavorites = {},
                onOpenSettings = {},
                onOpenHidden = { navController.navigate(AudioFocusDestination.Hidden.route) },
            )
        }
        composable(
            route = AudioFocusDestination.Player.route,
            arguments = listOf(navArgument("audiobookId") { type = NavType.StringType }),
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { height -> height },
                    animationSpec = tween(durationMillis = 320),
                ) + fadeIn(animationSpec = tween(durationMillis = 260))
            },
            popExitTransition = {
                slideOutVertically(
                    targetOffsetY = { height -> height },
                    animationSpec = tween(durationMillis = 280),
                ) + fadeOut(animationSpec = tween(durationMillis = 220))
            },
        ) {
            val viewModel: PlayerViewModel = hiltViewModel()
            val libraryViewModel: LibraryViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle()
            val deleteLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
            ) { result ->
                libraryViewModel.onPermanentDeleteResult(result.resultCode == Activity.RESULT_OK)
            }
            LaunchedEffect(libraryViewModel) {
                libraryViewModel.events.collect { event ->
                    when (event) {
                        is LibraryEvent.LaunchPermanentDeleteConfirmation -> {
                            deleteLauncher.launch(
                                IntentSenderRequest.Builder(event.intentSender).build(),
                            )
                        }
                    }
                }
            }
            LaunchedEffect(Unit) {
                viewModel.loadAndPlay()
            }
            PlayerScreen(
                state = state.value,
                onBack = navController::popBackStack,
                onTogglePlayPause = viewModel::togglePlayPause,
                onToggleFavorite = viewModel::toggleFavorite,
                onSeek = viewModel::seekTo,
                onSeekForward = viewModel::seekForward,
                onSeekBack = viewModel::seekBack,
                onHideAudiobook = { libraryViewModel.hideAudiobook(state.value.audiobookId) },
                onDeleteAudiobook = {
                    libraryViewModel.deleteAudiobookPermanently(
                        com.app.audiofocus.ui.viewmodel.LibraryRowUi(
                            id = state.value.audiobookId,
                            audioUri = state.value.audioUri,
                            title = state.value.title,
                            author = state.value.author,
                            relativePath = state.value.relativePath,
                            durationMs = state.value.durationMs,
                            positionMs = state.value.positionMs,
                            formattedDuration = state.value.durationLabel,
                            progressPercent = state.value.progressPercent,
                            playbackStatus = state.value.playbackStatus,
                            isFavorite = state.value.isFavorite,
                            lastPlayedAt = state.value.lastPlayedAt,
                        ),
                    )
                },
            )
        }
        composable(AudioFocusDestination.Hidden.route) {
            val viewModel: LibraryViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle()
            val deleteLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
            ) { result ->
                viewModel.onPermanentDeleteResult(result.resultCode == Activity.RESULT_OK)
            }
            LaunchedEffect(viewModel) {
                viewModel.events.collect { event ->
                    when (event) {
                        is LibraryEvent.LaunchPermanentDeleteConfirmation -> {
                            deleteLauncher.launch(
                                IntentSenderRequest.Builder(event.intentSender).build(),
                            )
                        }
                    }
                }
            }
            HiddenScreen(
                state = state.value,
                pendingPermanentDelete = viewModel.pendingPermanentDelete,
                deleteErrorMessage = viewModel.deleteErrorMessage,
                onRequestPermanentDelete = viewModel::requestPermanentDelete,
                onDismissPermanentDelete = viewModel::dismissPermanentDeleteDialog,
                onConfirmPermanentDelete = viewModel::confirmPermanentDelete,
                onDismissDeleteError = viewModel::clearDeleteError,
                onRestoreAudiobook = viewModel::restoreAudiobook,
                onBack = navController::popBackStack,
            )
        }
        composable(AudioFocusDestination.Settings.route) {
            val viewModel: LibraryViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle()
            val scanViewModel: ScanViewModel = hiltViewModel()
            val scanState = scanViewModel.uiState
            val deleteLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
            ) { result ->
                viewModel.onPermanentDeleteResult(result.resultCode == Activity.RESULT_OK)
            }
            LaunchedEffect(viewModel) {
                viewModel.events.collect { event ->
                    when (event) {
                        is LibraryEvent.LaunchPermanentDeleteConfirmation -> {
                            deleteLauncher.launch(
                                IntentSenderRequest.Builder(event.intentSender).build(),
                            )
                        }
                    }
                }
            }
            SettingsTabScreen(
                state = state.value,
                scanState = scanState,
                onOpenPlayer = { audiobookId ->
                    navController.navigate(AudioFocusDestination.Player.createRoute(audiobookId))
                },
                onPlayPauseAudiobook = viewModel::playOrPauseAudiobook,
                onSeekBackAudiobook = viewModel::seekBackAudiobook,
                onSeekForwardAudiobook = viewModel::seekForwardAudiobook,
                onToggleFavorite = viewModel::toggleFavorite,
                onHideAudiobook = viewModel::hideAudiobook,
                onDeleteAudiobook = viewModel::deleteAudiobookPermanently,
                deleteErrorMessage = viewModel.deleteErrorMessage,
                onDismissDeleteError = viewModel::clearDeleteError,
                onOpenHidden = { navController.navigate(AudioFocusDestination.Hidden.route) },
            )
        }
    }
}
