package com.app.audiofocus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AudioFocusColors = darkColorScheme(
    primary = Color(0xFFA86CFF),
    secondary = Color(0xFF79D3FF),
    tertiary = Color(0xFFF4A9FF),
    background = Color(0xFF120C2D),
    surface = Color(0xFF1A153A),
    surfaceVariant = Color(0x26FFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onBackground = Color(0xFFF7F5FB),
    onSurface = Color(0xFFF7F5FB),
    outline = Color(0x26FFFFFF),
    error = Color(0xFFFF6B7A),
)

@Composable
fun AudioFocusTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme()) AudioFocusColors else AudioFocusColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AudioFocusTypography,
        content = content,
    )
}
