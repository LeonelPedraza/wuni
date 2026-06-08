package com.app.audiofocus.ui.screen

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.app.audiofocus.R
import com.app.audiofocus.ui.component.AudiobookArtwork
import com.app.audiofocus.ui.component.HugeIcon
import com.app.audiofocus.ui.component.TimelineWaveform
import com.app.audiofocus.ui.viewmodel.LibraryRowUi
import com.app.audiofocus.ui.viewmodel.LibraryUiState
import com.app.audiofocus.ui.viewmodel.PendingPermanentDeleteUi
import com.app.audiofocus.ui.viewmodel.PlayerUiState
import com.app.audiofocus.ui.viewmodel.ScanUiState
import com.app.audiofocus.util.formatDuration
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random
import kotlinx.coroutines.delay

private enum class CatalogTab {
    Library,
    Favorites,
    Settings,
}

private data class FrostPoint(
    val x: Float,
    val y: Float,
    val alpha: Float,
    val radius: Float,
)

@Composable
fun LaunchScreen() {
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val uri = Uri.parse("android.resource://${context.packageName}/${R.raw.splash_screen_animation}")
            setMediaItem(MediaItem.fromUri(uri))
            repeatMode = Player.REPEAT_MODE_ALL
            playWhenReady = true
            volume = 0f
            prepare()
        }
    }
    DisposableEffect(player) {
        onDispose {
            player.release()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { viewContext ->
                PlayerView(viewContext).apply {
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    setShutterBackgroundColor(android.graphics.Color.BLACK)
                    this.player = player
                }
            },
            update = { view ->
                view.player = player
            },
        )
    }
}

@Composable
fun OnboardingScreen(onContinue: () -> Unit) {
    AudioFocusScreen(title = "") {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            androidx.compose.foundation.Image(
                painter = painterResource(R.drawable.named_brand),
                contentDescription = "Wuni",
                modifier = Modifier.width(220.dp),
                contentScale = ContentScale.Fit,
            )
            GlassSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Text(
                        text = "Tus audiolibros locales, siempre listos para seguir donde los dejaste.",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Text(
                        text = "Sin cuentas, sin nube, sin ruido.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                    )
                    Button(onClick = onContinue, modifier = Modifier.fillMaxWidth()) {
                        Text("Buscar mis audiolibros")
                    }
                }
            }
        }
    }
}

@Composable
fun PermissionsScreen(
    hasPermission: Boolean,
    onGrantPermission: () -> Unit,
    onSkip: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    AudioFocusScreen(title = "Permisos") {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            GlassSurface(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Text(
                        text = if (hasPermission) {
                            "Acceso listo. Ya podemos sincronizar tu biblioteca local."
                        } else {
                            "Necesitamos leer tus audios para construir la biblioteca local y mantener tu progreso."
                        },
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Button(onClick = onGrantPermission, modifier = Modifier.fillMaxWidth()) {
                        Text(if (hasPermission) "Continuar" else "Permitir acceso")
                    }
                    if (!hasPermission) {
                        OutlinedButton(onClick = onOpenSettings, modifier = Modifier.fillMaxWidth()) {
                            Text("Abrir configuracion")
                        }
                    }
                    OutlinedButton(onClick = onSkip, modifier = Modifier.fillMaxWidth()) {
                        Text("Ahora no")
                    }
                }
            }
        }
    }
}

@Composable
fun ScanningScreen(
    state: ScanUiState,
    onRunScan: () -> Unit,
    onOpenLibrary: () -> Unit,
) {
    AudioFocusScreen(title = "Sincronizando") {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            GlassSurface(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "Estamos preparando tu biblioteca local.",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Text(
                        text = "Analizando y sincronizando sin interrumpirte.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                    )
                    MetricsRow(
                        label = "Audios encontrados",
                        value = state.totalDiscovered.toString(),
                    )
                    MetricsRow(
                        label = "Audios analizados",
                        value = state.totalAnalyzed.toString(),
                    )
                    MetricsRow(
                        label = "Audiolibros detectados",
                        value = state.totalAccepted.toString(),
                    )
                    state.currentTitle?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    if (state.isScanning && state.totalDiscovered > 0) {
                        androidx.compose.material3.LinearProgressIndicator(
                            progress = { state.totalAnalyzed.toFloat() / state.totalDiscovered.toFloat() },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    } else {
                        CircularProgressIndicator()
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Button(
                    onClick = onRunScan,
                    enabled = state.hasPermission && !state.isScanning,
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Escanear")
                }
                OutlinedButton(onClick = onOpenLibrary, modifier = Modifier.weight(1f)) {
                    Text("Ir a biblioteca")
                }
            }
        }
    }
}

@Composable
fun LibraryScreen(
    state: LibraryUiState,
    scanState: ScanUiState,
    onOpenPlayer: (String) -> Unit,
    onPlayPauseAudiobook: (LibraryRowUi) -> Unit,
    onSeekBackAudiobook: (LibraryRowUi) -> Unit,
    onSeekForwardAudiobook: (LibraryRowUi) -> Unit,
    onToggleFavorite: (LibraryRowUi) -> Unit,
    onHideAudiobook: (String) -> Unit,
    onDeleteAudiobook: (LibraryRowUi) -> Unit,
    deleteErrorMessage: String?,
    onDismissDeleteError: () -> Unit,
    onOpenLibrary: () -> Unit,
    onOpenFavorites: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenHidden: () -> Unit = {},
) {
    LibraryTabsScreen(
        initialTab = CatalogTab.Library,
        state = state,
        scanState = scanState,
        onOpenPlayer = onOpenPlayer,
        onPlayPauseAudiobook = onPlayPauseAudiobook,
        onSeekBackAudiobook = onSeekBackAudiobook,
        onSeekForwardAudiobook = onSeekForwardAudiobook,
        onToggleFavorite = onToggleFavorite,
        onHideAudiobook = onHideAudiobook,
        onDeleteAudiobook = onDeleteAudiobook,
        deleteErrorMessage = deleteErrorMessage,
        onDismissDeleteError = onDismissDeleteError,
        onOpenHidden = onOpenHidden,
    )
}

@Composable
fun FavoritesScreen(
    state: LibraryUiState,
    scanState: ScanUiState? = null,
    onOpenPlayer: (String) -> Unit,
    onPlayPauseAudiobook: (LibraryRowUi) -> Unit,
    onSeekBackAudiobook: (LibraryRowUi) -> Unit,
    onSeekForwardAudiobook: (LibraryRowUi) -> Unit,
    onToggleFavorite: (LibraryRowUi) -> Unit,
    onHideAudiobook: (String) -> Unit,
    onDeleteAudiobook: (LibraryRowUi) -> Unit,
    deleteErrorMessage: String?,
    onDismissDeleteError: () -> Unit,
    onOpenLibrary: () -> Unit,
    onOpenFavorites: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenHidden: () -> Unit = {},
) {
    LibraryTabsScreen(
        initialTab = CatalogTab.Favorites,
        state = state,
        scanState = scanState,
        onOpenPlayer = onOpenPlayer,
        onPlayPauseAudiobook = onPlayPauseAudiobook,
        onSeekBackAudiobook = onSeekBackAudiobook,
        onSeekForwardAudiobook = onSeekForwardAudiobook,
        onToggleFavorite = onToggleFavorite,
        onHideAudiobook = onHideAudiobook,
        onDeleteAudiobook = onDeleteAudiobook,
        deleteErrorMessage = deleteErrorMessage,
        onDismissDeleteError = onDismissDeleteError,
        onOpenHidden = onOpenHidden,
    )
}

@Composable
fun SettingsTabScreen(
    state: LibraryUiState,
    scanState: ScanUiState? = null,
    onOpenPlayer: (String) -> Unit,
    onPlayPauseAudiobook: (LibraryRowUi) -> Unit,
    onSeekBackAudiobook: (LibraryRowUi) -> Unit,
    onSeekForwardAudiobook: (LibraryRowUi) -> Unit,
    onToggleFavorite: (LibraryRowUi) -> Unit,
    onHideAudiobook: (String) -> Unit,
    onDeleteAudiobook: (LibraryRowUi) -> Unit,
    deleteErrorMessage: String?,
    onDismissDeleteError: () -> Unit,
    onOpenHidden: () -> Unit,
) {
    LibraryTabsScreen(
        initialTab = CatalogTab.Settings,
        state = state,
        scanState = scanState,
        onOpenPlayer = onOpenPlayer,
        onPlayPauseAudiobook = onPlayPauseAudiobook,
        onSeekBackAudiobook = onSeekBackAudiobook,
        onSeekForwardAudiobook = onSeekForwardAudiobook,
        onToggleFavorite = onToggleFavorite,
        onHideAudiobook = onHideAudiobook,
        onDeleteAudiobook = onDeleteAudiobook,
        deleteErrorMessage = deleteErrorMessage,
        onDismissDeleteError = onDismissDeleteError,
        onOpenHidden = onOpenHidden,
    )
}

@Composable
private fun LibraryTabsScreen(
    initialTab: CatalogTab,
    state: LibraryUiState,
    scanState: ScanUiState?,
    onOpenPlayer: (String) -> Unit,
    onPlayPauseAudiobook: (LibraryRowUi) -> Unit,
    onSeekBackAudiobook: (LibraryRowUi) -> Unit,
    onSeekForwardAudiobook: (LibraryRowUi) -> Unit,
    onToggleFavorite: (LibraryRowUi) -> Unit,
    onHideAudiobook: (String) -> Unit,
    onDeleteAudiobook: (LibraryRowUi) -> Unit,
    deleteErrorMessage: String?,
    onDismissDeleteError: () -> Unit,
    onOpenHidden: () -> Unit,
) {
    var activeTab by rememberSaveable { mutableStateOf(initialTab) }
    val hazeState = remember { HazeState() }
    val dockedPlayerItem = remember(
        state.miniPlayerAudiobookId,
        state.audiobooks,
        state.favoriteAudiobooks,
        state.recentAudiobooks,
        state.hiddenAudiobooks,
    ) {
        buildList {
            addAll(state.audiobooks)
            addAll(state.favoriteAudiobooks)
            addAll(state.recentAudiobooks)
            addAll(state.hiddenAudiobooks)
        }
            .distinctBy { it.id }
            .firstOrNull { it.id == state.miniPlayerAudiobookId }
    }
    LaunchedEffect(initialTab) {
        activeTab = initialTab
    }
    AudioFocusScreen(title = "") {
        Box(modifier = Modifier.weight(1f, fill = true)) {
            val showDockedPlayer = activeTab != CatalogTab.Settings && dockedPlayerItem != null
            AnimatedContent(
                modifier = Modifier
                    .fillMaxSize()
                    .haze(state = hazeState),
                targetState = activeTab,
                transitionSpec = {
                    val forward = targetState.ordinal > initialState.ordinal
                    val enter = slideInHorizontally { width ->
                        if (forward) width else -width
                    } + fadeIn()
                    val exit = slideOutHorizontally { width ->
                        if (forward) -width else width
                    } + fadeOut()
                    enter togetherWith exit using SizeTransform(clip = false)
                },
                label = "tab-slide",
            ) { tab ->
                when (tab) {
                    CatalogTab.Library -> CatalogScreen(
                        items = state.audiobooks,
                        recentItems = state.recentAudiobooks,
                        scanState = scanState,
                        deleteErrorMessage = deleteErrorMessage,
                        onDismissDeleteError = onDismissDeleteError,
                        onOpenPlayer = onOpenPlayer,
                        onToggleFavorite = onToggleFavorite,
                        onHideAudiobook = onHideAudiobook,
                        onDeleteAudiobook = onDeleteAudiobook,
                        bottomInset = if (showDockedPlayer) 156.dp else 82.dp,
                    )

                    CatalogTab.Favorites -> CatalogScreen(
                        items = state.favoriteAudiobooks,
                        recentItems = emptyList(),
                        scanState = null,
                        deleteErrorMessage = deleteErrorMessage,
                        onDismissDeleteError = onDismissDeleteError,
                        onOpenPlayer = onOpenPlayer,
                        onToggleFavorite = onToggleFavorite,
                        onHideAudiobook = onHideAudiobook,
                        onDeleteAudiobook = onDeleteAudiobook,
                        bottomInset = if (showDockedPlayer) 156.dp else 82.dp,
                    )

                    CatalogTab.Settings -> SettingsTabContent(
                        onOpenHidden = onOpenHidden,
                    )
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    if (activeTab != CatalogTab.Settings) {
                dockedPlayerItem?.let { item ->
                    DockedPlayerCard(
                        item = item,
                        isPlaying = state.activePlaybackAudiobookId == item.id && state.activePlaybackIsPlaying,
                        hazeState = hazeState,
                        onOpenPlayer = { onOpenPlayer(item.id) },
                        onPlayPause = { onPlayPauseAudiobook(item) },
                        onSeekBack = { onSeekBackAudiobook(item) },
                        onSeekForward = { onSeekForwardAudiobook(item) },
                    )
                }
                    }
                    BottomNavBar(
                        activeTab = activeTab,
                        onOpenLibrary = { activeTab = CatalogTab.Library },
                        onOpenFavorites = { activeTab = CatalogTab.Favorites },
                        onOpenSettings = { activeTab = CatalogTab.Settings },
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
private fun CatalogScreen(
    items: List<LibraryRowUi>,
    recentItems: List<LibraryRowUi>,
    scanState: ScanUiState?,
    deleteErrorMessage: String?,
    onDismissDeleteError: () -> Unit,
    onOpenPlayer: (String) -> Unit,
    onToggleFavorite: (LibraryRowUi) -> Unit,
    onHideAudiobook: (String) -> Unit,
    onDeleteAudiobook: (LibraryRowUi) -> Unit,
    bottomInset: androidx.compose.ui.unit.Dp,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var selectedFilter by rememberSaveable { mutableIntStateOf(0) }
    var infoItem by remember { mutableStateOf<LibraryRowUi?>(null) }
    var deleteItem by remember { mutableStateOf<LibraryRowUi?>(null) }

    val filtered = remember(items, query, selectedFilter) {
        items.filter { item ->
            val matchesQuery = query.isBlank() ||
                item.title.contains(query, ignoreCase = true) ||
                (item.author?.contains(query, ignoreCase = true) == true)
            val matchesFilter = when (selectedFilter) {
                1 -> item.progressPercent in 1..99
                2 -> item.playbackStatus == "completed"
                else -> true
            }
            matchesQuery && matchesFilter
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        contentPadding = PaddingValues(bottom = bottomInset),
    ) {
        item("catalog-header") {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                SearchField(
                    query = query,
                    onQueryChange = { query = it },
                )
                CatalogFilters(
                    selectedIndex = selectedFilter,
                    onSelected = { selectedFilter = it },
                )
            }
        }
        deleteErrorMessage?.let { message ->
            item("delete-error") {
                GlassSurface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp),
                    shape = RoundedCornerShape(18.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            text = "Cerrar",
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.clickable(onClick = onDismissDeleteError),
                        )
                    }
                }
            }
        }
        if (recentItems.isNotEmpty()) {
            item("recent-title") {
                Text(
                    text = "Recientes",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    modifier = Modifier.padding(top = 22.dp, bottom = 14.dp),
                )
            }
            item("recent-row") {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(end = 6.dp),
                ) {
                    items(recentItems.take(6), key = { item -> item.id }) { item ->
                        RecentBookCard(item = item, onOpenPlayer = onOpenPlayer)
                    }
                }
            }
            item("recent-gap") {
                Spacer(modifier = Modifier.height(20.dp))
            }
        } else {
            item("list-gap-top") {
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
        items(filtered, key = { item -> item.id }) { item ->
            AudiobookListRow(
                item = item,
                onOpenPlayer = onOpenPlayer,
                onOpenInfo = { infoItem = item },
            )
        }
    }

    infoItem?.let { item ->
        ModalBottomSheet(
            onDismissRequest = { infoItem = null },
            containerColor = Color(0xFF181438),
            dragHandle = { SheetHandle() },
        ) {
            InfoSheetContent(
                item = item,
                onContinue = {
                    infoItem = null
                    onOpenPlayer(item.id)
                },
                onToggleFavorite = {
                    onToggleFavorite(item)
                    infoItem = null
                },
                onHide = {
                    onHideAudiobook(item.id)
                    infoItem = null
                },
                onDelete = {
                    infoItem = null
                    deleteItem = item
                },
            )
        }
    }

    deleteItem?.let { item ->
        ModalBottomSheet(
            onDismissRequest = { deleteItem = null },
            containerColor = Color(0xFF181438),
            dragHandle = { SheetHandle() },
        ) {
            DeleteSheetContent(
                onConfirm = {
                    onDeleteAudiobook(item)
                    deleteItem = null
                },
                onCancel = { deleteItem = null },
            )
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.07f))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(modifier = Modifier.weight(1f)) {
                if (query.isBlank()) {
                    Text(
                        text = "Buscar audiolibro...",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                )
            }
            HugeIcon(
                iconRes = R.drawable.ic_huge_search_01,
                contentDescription = "Buscar",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f),
                modifier = Modifier
                    .size(18.dp)
            )
        }
    }
}

@Composable
private fun CatalogFilters(
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        listOf("Todos", "Escuchando", "Completados").forEachIndexed { index, label ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip( RoundedCornerShape(8.dp)                    )
                    .background(
                        if (selectedIndex == index) {
                            Color(0xFF4F3E79)
                        } else {
                            Color.Transparent
                        },
                    )
                    .clickable(onClick = { onSelected(index) })
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = if (selectedIndex == index) 0.96f else 0.88f,
                    ),
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PlayerScreen(
    state: PlayerUiState,
    onBack: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onToggleFavorite: () -> Unit,
    onSeek: (Long) -> Unit,
    onSeekForward: () -> Unit,
    onSeekBack: () -> Unit,
    onHideAudiobook: () -> Unit,
    onDeleteAudiobook: () -> Unit,
) {
    var showInfoSheet by remember { mutableStateOf(false) }
    var showDeleteSheet by remember { mutableStateOf(false) }
    var scrubPreviewPositionMs by remember(state.audiobookId) { mutableStateOf<Long?>(null) }
    LaunchedEffect(state.audiobookId, state.positionMs, scrubPreviewPositionMs) {
        val preview = scrubPreviewPositionMs ?: return@LaunchedEffect
        if (abs(state.positionMs - preview) <= 1_500L) {
            scrubPreviewPositionMs = null
        }
    }
    val currentItem = remember(state) {
        LibraryRowUi(
            id = state.audiobookId,
            audioUri = state.audioUri,
            title = state.title,
            author = state.author,
            relativePath = state.relativePath,
            durationMs = state.durationMs,
            positionMs = state.positionMs,
            formattedDuration = state.durationLabel,
            progressPercent = state.progressPercent,
            playbackStatus = state.playbackStatus,
            isFavorite = state.isFavorite,
            lastPlayedAt = state.lastPlayedAt,
        )
    }
    val displayedPositionMs = scrubPreviewPositionMs ?: state.positionMs
    AudioFocusScreen(title = "") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            IconButton(onClick = onBack) {
                HugeIcon(
                    iconRes = R.drawable.ic_huge_arrow_down_01,
                    contentDescription = "Cerrar",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(32.dp)
                )
            }
            IconButton(onClick = { showInfoSheet = true }) {
                HugeIcon(
                    iconRes = R.drawable.ic_huge_more_vertical,
                    contentDescription = "Opciones",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, 0.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp),
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                AudiobookArtwork(
                    sourceUri = state.audioUri.ifBlank { null },
                    title = state.title,
                    modifier = Modifier.size(300.dp),
                    shape = CircleShape,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = state.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    state.author?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.78f),
                        )
                    }
                }
                IconButton(onClick = onToggleFavorite) {
                    HugeIcon(
                        iconRes = R.drawable.ic_huge_favourite,
                        contentDescription = "Favorito",
                        tint = if (state.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            TimelineWaveform(
                audiobookId = state.audiobookId,
                title = state.title,
                author = state.author,
                durationMs = state.durationMs,
                positionMs = displayedPositionMs,
                onScrub = { position ->
                    scrubPreviewPositionMs = position
                },
                onScrubFinished = { position ->
                    scrubPreviewPositionMs = position
                    onSeek(position)
                },
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = formatDuration(displayedPositionMs),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                )
                Text(
                    text = state.durationLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.72f),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircleActionButton(
                    iconRes = R.drawable.ic_huge_go_backward_10_sec,
                    size = 60.dp,
                    tint = MaterialTheme.colorScheme.onSurface,
                    containerColor = Color.Transparent,
                    onClick = onSeekBack,
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .size(82.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF965CF6))
                        .clickable(onClick = onTogglePlayPause),
                    contentAlignment = Alignment.Center,
                ) {
                    HugeIcon(
                        iconRes = if (state.isPlaying) R.drawable.ic_huge_pause else R.drawable.ic_huge_play,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = Color.White,
                    )
                }
                CircleActionButton(
                    iconRes = R.drawable.ic_huge_go_forward_10_sec,
                    size = 60.dp,
                    tint = MaterialTheme.colorScheme.onSurface,
                    containerColor = Color.Transparent,
                    onClick = onSeekForward,
                )
            }
        }
    }
    if (showInfoSheet) {
        ModalBottomSheet(
            onDismissRequest = { showInfoSheet = false },
            containerColor = Color(0xFF181438),
            dragHandle = { SheetHandle() },
        ) {
            InfoSheetContent(
                item = currentItem,
                onContinue = {
                    showInfoSheet = false
                },
                onToggleFavorite = {
                    onToggleFavorite()
                    showInfoSheet = false
                },
                onHide = {
                    onHideAudiobook()
                    showInfoSheet = false
                },
                onDelete = {
                    showInfoSheet = false
                    showDeleteSheet = true
                },
            )
        }
    }
    if (showDeleteSheet) {
        ModalBottomSheet(
            onDismissRequest = { showDeleteSheet = false },
            containerColor = Color(0xFF181438),
            dragHandle = { SheetHandle() },
        ) {
            DeleteSheetContent(
                onConfirm = {
                    onDeleteAudiobook()
                    showDeleteSheet = false
                },
                onCancel = { showDeleteSheet = false },
            )
        }
    }
}

@Composable
fun HiddenScreen(
    state: LibraryUiState,
    pendingPermanentDelete: PendingPermanentDeleteUi?,
    deleteErrorMessage: String?,
    onRequestPermanentDelete: (LibraryRowUi) -> Unit,
    onDismissPermanentDelete: () -> Unit,
    onConfirmPermanentDelete: () -> Unit,
    onDismissDeleteError: () -> Unit,
    onRestoreAudiobook: (String) -> Unit,
    onBack: () -> Unit,
) {
    AudioFocusScreen(title = "Ocultos") {
        deleteErrorMessage?.let {
            GlassSurface(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(it, modifier = Modifier.weight(1f))
                    Text(
                        text = "Cerrar",
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.clickable(onClick = onDismissDeleteError),
                    )
                }
            }
        }
        LazyColumn(
            modifier = Modifier.weight(1f, fill = true),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(state.hiddenAudiobooks, key = { item -> item.id }) { item ->
                GlassSurface(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AudiobookArtwork(
                            sourceUri = item.audioUri,
                            title = item.title,
                            modifier = Modifier.size(58.dp),
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.title, style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = item.author ?: item.relativePath ?: "Sin autor",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = { onRestoreAudiobook(item.id) }) {
                                Text("Restaurar")
                            }
                            OutlinedButton(onClick = { onRequestPermanentDelete(item) }) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
    pendingPermanentDelete?.let { request ->
        AlertDialog(
            onDismissRequest = onDismissPermanentDelete,
            title = { Text("Eliminar archivo") },
            text = { Text("Vas a borrar de forma permanente \"${request.title}\". Esta acciÃ³n no se puede deshacer.") },
            dismissButton = {
                OutlinedButton(onClick = onDismissPermanentDelete) { Text("Cancelar") }
            },
            confirmButton = {
                Button(onClick = onConfirmPermanentDelete) { Text("Eliminar") }
            },
        )
    }
}

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onOpenHidden: () -> Unit = {},
) {
    AudioFocusScreen(title = "Ajustes") {
        GlassSurface(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Text("Saltos de 10 segundos", style = MaterialTheme.typography.titleMedium)
                Text("ReproducciÃ³n en segundo plano", style = MaterialTheme.typography.titleMedium)
                OutlinedButton(onClick = onOpenHidden, modifier = Modifier.fillMaxWidth()) {
                    Text("Ver ocultos")
                }
            }
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}

@Composable
private fun SettingsTabContent(
    onOpenHidden: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Text(
            text = "Ajustes",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        GlassSurface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "Saltos de 10 segundos",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                )
                Text(
                    text = "Reproduccion en segundo plano",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                )
                OutlinedButton(onClick = onOpenHidden, modifier = Modifier.fillMaxWidth()) {
                    Text("Ver ocultos")
                }
            }
        }
    }
}

@Composable
private fun DockedPlayerCard(
    item: LibraryRowUi,
    isPlaying: Boolean,
    hazeState: HazeState,
    onOpenPlayer: () -> Unit,
    onPlayPause: () -> Unit,
    onSeekBack: () -> Unit,
    onSeekForward: () -> Unit,
) {
    val playerShape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fullBleed(horizontal = 5.dp)
            .hazeChild(
                state = hazeState,
                shape = playerShape,
                tint = Color(0xFF2A2148).copy(alpha = 0.30f),
                blurRadius = 34.dp,
                noiseFactor = 0.38f,
            )
            .clickable { onOpenPlayer() },
        shape = playerShape,
        color = Color(0xFF2A2148).copy(alpha = 0.34f),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.035f)),
    ) {
        Box {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color(0xFF1D153F).copy(alpha = 0.24f)),
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.085f),
                                Color(0xFF3B2F61).copy(alpha = 0.18f),
                                Color(0xFF221A45).copy(alpha = 0.20f),
                            ),
                        ),
                    )
            )
            FrostedNoiseOverlay(
                modifier = Modifier.matchParentSize(),
                pointCount = 1050,
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.White.copy(alpha = 0.08f)),
            )
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AudiobookArtwork(
                    sourceUri = item.audioUri,
                    title = item.title,
                    modifier = Modifier.size(52.dp),
                    shape = RoundedCornerShape(10.dp),
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = item.author ?: "Sin autor",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.74f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CircleActionButton(
                        iconRes = R.drawable.ic_huge_go_backward_10_sec,
                        size = 46.dp,
                        containerColor = Color.Transparent,
                        tint = Color.White,
                        onClick = onSeekBack,
                    )
                    CircleActionButton(
                        iconRes = if (isPlaying) R.drawable.ic_huge_pause else R.drawable.ic_huge_play,
                        size = 48.dp,
                        containerColor = Color.Transparent,
                        tint = Color.White,
                        onClick = onPlayPause,
                    )
                    CircleActionButton(
                        iconRes = R.drawable.ic_huge_go_forward_10_sec,
                        size = 46.dp,
                        containerColor = Color.Transparent,
                        tint = Color.White,
                        onClick = onSeekForward,
                    )
                }
            }
        }
    }
}

@Composable
private fun FrostedNoiseOverlay(
    modifier: Modifier = Modifier,
    pointCount: Int,
) {
    val points = remember(pointCount) {
        val random = Random(7321)
        List(pointCount) {
            FrostPoint(
                x = random.nextFloat(),
                y = random.nextFloat(),
                alpha = random.nextFloat(),
                radius = 0.45f + random.nextFloat() * 1.1f,
            )
        }
    }
    Canvas(modifier = modifier) {
        points.forEachIndexed { index, point ->
            val isBright = index % 4 != 0
            drawCircle(
                color = if (isBright) {
                    Color.White.copy(alpha = 0.030f + point.alpha * 0.070f)
                } else {
                    Color(0xFF0B0820).copy(alpha = 0.018f + point.alpha * 0.040f)
                },
                radius = point.radius,
                center = Offset(point.x * size.width, point.y * size.height),
            )
        }
    }
}

@Composable
private fun RecentBookCard(
    item: LibraryRowUi,
    onOpenPlayer: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .width(118.dp)
            .height(128.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onOpenPlayer(item.id) },
    ) {
        AudiobookArtwork(
            sourceUri = item.audioUri,
            title = item.title,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xD90B0A1F)),
                    ),
                )
                .padding(horizontal = 10.dp, vertical = 8.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = remainingLabel(item),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.82f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun AudiobookListRow(
    item: LibraryRowUi,
    onOpenPlayer: (String) -> Unit,
    onOpenInfo: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenPlayer(item.id) }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AudiobookArtwork(
            sourceUri = item.audioUri,
            title = item.title,
            modifier = Modifier.size(58.dp),
            shape = RoundedCornerShape(10.dp),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = item.author ?: item.relativePath ?: "Sin autor",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.72f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "Duracion: ${item.formattedDuration}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.58f),
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            ProgressBadge(progressPercent = item.progressPercent)
            IconButton(onClick = onOpenInfo) {
                HugeIcon(
                    iconRes = R.drawable.ic_huge_more_vertical,
                    contentDescription = "Mas",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.86f),
                )
            }
        }
    }
}

@Composable
private fun BottomNavBar(
    activeTab: CatalogTab,
    onOpenLibrary: () -> Unit,
    onOpenFavorites: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1D153F))
            .padding(top = 14.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NavAction(
            iconRes = R.drawable.ic_huge_book_open_01,
            title="Librería",
            isSelected = activeTab == CatalogTab.Library,
            onClick = onOpenLibrary,
        )
        NavAction(
            iconRes = R.drawable.ic_huge_favourite,
            title="Favoritos",
            isSelected = activeTab == CatalogTab.Favorites,
            onClick = onOpenFavorites,
        )
        NavAction(
            iconRes = R.drawable.ic_huge_settings_01,
            title="Ajustes",
            isSelected = activeTab == CatalogTab.Settings,
            onClick = onOpenSettings,
        )
    }
}

@Composable
private fun NavAction(
    iconRes: Int,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HugeIcon(
                iconRes = iconRes,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.88f),
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.88f),
            )
        }
    }
}

@Composable
private fun ProgressBadge(progressPercent: Int) {
    Box(
        modifier = Modifier
            .size(34.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            progress = { progressPercent.coerceIn(0, 100) / 100f },
            modifier = Modifier.fillMaxSize(),
            strokeWidth = 2.dp,
            color = Color.White.copy(alpha = 0.88f),
            trackColor = Color.White.copy(alpha = 0.12f),
        )
        Text(
            text = "${progressPercent.coerceIn(0, 100)}%",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.86f),
        )
    }
}

@Composable
private fun CircleActionButton(
    iconRes: Int,
    size: androidx.compose.ui.unit.Dp,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    containerColor: Color = Color.White.copy(alpha = 0.08f),
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(containerColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        HugeIcon(
            iconRes = iconRes,
            contentDescription = null,
            modifier = Modifier.size(size * 0.72f),
            tint = tint,
        )
    }
}

private fun Modifier.fullBleed(horizontal: androidx.compose.ui.unit.Dp): Modifier = layout { measurable, constraints ->
    val bleedPx = horizontal.roundToPx()
    val extraWidth = bleedPx * 2
    val placeable = measurable.measure(
        constraints.copy(
            minWidth = constraints.maxWidth + extraWidth,
            maxWidth = constraints.maxWidth + extraWidth,
        ),
    )
    layout(constraints.maxWidth, placeable.height) {
        placeable.placeRelative(-bleedPx, 0)
    }
}

@Composable
private fun InfoSheetContent(
    item: LibraryRowUi,
    onContinue: () -> Unit,
    onToggleFavorite: () -> Unit,
    onHide: () -> Unit,
    onDelete: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        GlassSurface(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                AudiobookArtwork(
                    sourceUri = item.audioUri,
                    title = item.title,
                    modifier = Modifier.size(62.dp),
                    shape = RoundedCornerShape(12.dp),
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                    )
                    Text(
                        text = item.author ?: item.relativePath ?: "Sin autor",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.72f),
                    )
                }
            }
        }
        GlassSurface(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                ActionRow(
                    iconRes = R.drawable.ic_huge_favourite,
                    title = if (item.isFavorite) "Quitar de favoritos" else "Marcar como favorito",
                    tint = if (item.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    onClick = onToggleFavorite,
                )
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                ActionRow(
                    iconRes = R.drawable.ic_huge_view_off,
                    title = "Quitar de la biblioteca",
                    onClick = onHide,
                )
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                ActionRow(
                    iconRes = R.drawable.ic_huge_delete_02,
                    title = "Eliminar permanentemente",
                    tint = Color(0xFFFF6F73),
                    onClick = onDelete,
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            StatCard(label = "Duración", value = item.formattedDuration, modifier = Modifier.weight(1f))
            StatCard(label = "Progreso", value = "${item.progressPercent}%", modifier = Modifier.weight(1f))
            StatCard(
                label = "Reproducido",
                value = if (item.lastPlayedAt != null) "Reciente" else "Nunca",
                modifier = Modifier.weight(1f),
            )
        }
        Button(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(0.dp, 15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF5F5F5),
                contentColor = Color(0xFF1A1A1A),
            ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                HugeIcon(
                    iconRes = R.drawable.ic_huge_play,
                    contentDescription = null,
                    tint = Color(0xFF1A1A1A),
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    "Continuar escuchando",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun DeleteSheetContent(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .background(Color(0xFF3F2740)),
            contentAlignment = Alignment.Center,
        ) {
            HugeIcon(
                iconRes = R.drawable.ic_huge_delete_02,
                contentDescription = null,
                tint = Color(0xFFFF6F73),
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "¿Eliminar permanentemente?",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 1f)
            )
            Text(
                text = "Esta acción borrará el archivo del almacenamiento de tu dispositivo. No podrás recuperarlo desde la app.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(0.dp, 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF24949)),
            ) {
                Text("Eliminar archivo", style = MaterialTheme.typography.titleMedium)
            }
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(0.dp, 12.dp),
            ) {
                Text("Cancelar", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun ActionRow(
    iconRes: Int,
    title: String,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HugeIcon(
            iconRes = iconRes,
            contentDescription = null,
            tint = tint,
        )
        Text(text = title, style = MaterialTheme.typography.bodyLarge, color = tint)
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    GlassSurface(modifier = modifier) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.72f),
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
            )
        }
    }
}

@Composable
private fun MetricsRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f))
        Text(text = value, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun SheetHandle() {
    Box(
        modifier = Modifier
            .padding(top = 6.dp, bottom = 10.dp)
            .width(74.dp)
            .height(5.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.38f)),
    )
}

private fun remainingLabel(item: LibraryRowUi): String {
    val remainingMs = max(item.durationMs - item.positionMs, 0L)
    val totalMinutes = remainingMs / 60_000L
    val hours = totalMinutes / 60L
    val minutes = totalMinutes % 60L
    return if (hours > 0L) {
        "${hours}h ${minutes}min restantes"
    } else {
        "${minutes}min restantes"
    }
}
