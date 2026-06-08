package com.app.audiofocus.ui.component

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.LruCache
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private object ArtworkCache {
    private val cache = LruCache<String, Bitmap>(32)

    fun get(key: String): Bitmap? = cache.get(key)

    fun put(key: String, bitmap: Bitmap) {
        cache.put(key, bitmap)
    }
}

@Composable
fun AudiobookArtwork(
    sourceUri: String?,
    title: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(18.dp),
) {
    val context = LocalContext.current
    val bitmap by produceState<Bitmap?>(initialValue = sourceUri?.let(ArtworkCache::get), key1 = sourceUri) {
        if (sourceUri.isNullOrBlank() || value != null) return@produceState
        value = withContext(Dispatchers.IO) {
            runCatching {
                val retriever = MediaMetadataRetriever()
                try {
                    retriever.setDataSource(context, Uri.parse(sourceUri))
                    retriever.embeddedPicture?.let { bytes ->
                        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    }
                } finally {
                    retriever.release()
                }
            }.getOrNull()?.also { artwork ->
                ArtworkCache.put(sourceUri, artwork)
            }
        }
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap!!.asImageBitmap(),
            contentDescription = title,
            modifier = modifier.clip(shape),
            contentScale = ContentScale.Crop,
        )
    } else {
        Box(
            modifier = modifier
                .clip(shape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF59B6FF),
                            Color(0xFF6D8DFF),
                            Color(0xFFDB90FF),
                        ),
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = title.take(1).uppercase(),
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White.copy(alpha = 0.94f),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Clip,
            )
        }
    }
}
