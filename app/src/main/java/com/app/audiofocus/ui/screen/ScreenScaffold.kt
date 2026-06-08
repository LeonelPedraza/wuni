package com.app.audiofocus.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun AudioFocusScreen(
    title: String,
    body: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A123F),
                        Color(0xFF120C2D),
                        Color(0xFF0E0A22),
                    ),
                ),
            ),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-84).dp, y = (-72).dp)
                .size(260.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF8B5CF6).copy(alpha = 0.32f),
                            Color.Transparent,
                        ),
                    ),
                    shape = RoundedCornerShape(220.dp),
                ),
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 104.dp, y = 120.dp)
                .size(300.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF7C4DFF).copy(alpha = 0.20f),
                            Color.Transparent,
                        ),
                    ),
                    shape = RoundedCornerShape(240.dp),
                ),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 5.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            if (title.isNotBlank()) {
                Text(text = title, style = MaterialTheme.typography.headlineMedium)
            }
            body()
        }
    }
}

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    color: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.68f),
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = color,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.10f)),
    ) {
        content()
    }
}
