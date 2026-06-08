package com.app.audiofocus.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun HugeIcon(
    @DrawableRes iconRes: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color,
) {
    Icon(
        painter = painterResource(iconRes),
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint,
    )
}
