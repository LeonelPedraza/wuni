package com.app.audiofocus.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

private const val MIN_WAVEFORM_SAMPLES = 900
private const val MAX_WAVEFORM_SAMPLES = 1_800
private const val SCRUB_SENSITIVITY_MULTIPLIER = 1.7f

@Composable
fun TimelineWaveform(
    modifier: Modifier = Modifier,
    audiobookId: String,
    title: String,
    author: String?,
    durationMs: Long,
    positionMs: Long,
    height: Dp = 76.dp,
    onScrubStart: () -> Unit = {},
    onScrub: (Long) -> Unit = {},
    onScrubFinished: (Long) -> Unit,
) {
    val samples = remember(audiobookId, title, author, durationMs) {
        generateSyntheticWaveform(
            audiobookId = audiobookId,
            title = title,
            author = author,
            durationMs = durationMs,
        )
    }
    val density = LocalDensity.current
    val barWidthPx = with(density) { 1.8.dp.toPx() }
    val barSpacingPx = with(density) { 5.2.dp.toPx() }
    val barStepPx = barWidthPx + barSpacingPx
    val waveformWidthPx = remember(samples.size, barStepPx) {
        max((samples.lastIndex * barStepPx), 1f)
    }
    var scrubOffsetPx by remember { mutableFloatStateOf(Float.NaN) }
    var isUserScrubbing by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .padding(vertical = 4.dp)
            .background(Color.Transparent)
    ) {
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .draggable(
                    state = rememberDraggableState { delta ->
                        if (!isUserScrubbing) {
                            isUserScrubbing = true
                            scrubOffsetPx = positionToOffset(positionMs, durationMs, waveformWidthPx)
                            onScrubStart()
                        }
                        scrubOffsetPx = (
                            scrubOffsetPx - (delta * SCRUB_SENSITIVITY_MULTIPLIER)
                        ).coerceIn(0f, waveformWidthPx)
                        onScrub(offsetToPosition(scrubOffsetPx, durationMs, waveformWidthPx))
                    },
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        val targetPositionMs = offsetToPosition(
                            offsetPx = scrubOffsetPx.takeUnless { it.isNaN() }
                                ?: positionToOffset(positionMs, durationMs, waveformWidthPx),
                            durationMs = durationMs,
                            waveformWidthPx = waveformWidthPx,
                        )
                        onScrubFinished(targetPositionMs)
                        isUserScrubbing = false
                        scrubOffsetPx = Float.NaN
                    },
                )
        ) {
            val viewportCenterX = size.width / 2f
            val currentOffsetPx = if (isUserScrubbing && !scrubOffsetPx.isNaN()) {
                scrubOffsetPx
            } else {
                positionToOffset(positionMs, durationMs, waveformWidthPx)
            }
            val visibleStartIndex = max(
                floor((currentOffsetPx - viewportCenterX) / barStepPx).toInt() - 4,
                0,
            )
            val visibleEndIndex = min(
                floor((currentOffsetPx + viewportCenterX) / barStepPx).toInt() + 4,
                samples.lastIndex,
            )
            for (index in visibleStartIndex..visibleEndIndex) {
                val x = viewportCenterX + (index * barStepPx) - currentOffsetPx
                val sample = samples[index].coerceIn(18, 100) / 100f
                val barHeight = ((size.height * 0.10f) + (size.height * 0.54f * sample))
                    .coerceAtMost(size.height)
                val alpha = 0.92f - (0.28f * ((x - viewportCenterX).absoluteValue / viewportCenterX).coerceIn(0f, 1f))
                val widthVariance = 0.72f + (0.48f * (((sample * 1.45f) + ((index % 7) * 0.08f)).coerceIn(0f, 1.45f)))
                val color = if (x <= viewportCenterX) {
                    Color(0xFFB488FF).copy(alpha = alpha)
                } else {
                    Color(0xFF6F6A87).copy(alpha = alpha * 0.72f)
                }
                drawLine(
                    color = color,
                    start = Offset(x, (size.height - barHeight) / 2f),
                    end = Offset(x, (size.height + barHeight) / 2f),
                    strokeWidth = barWidthPx * widthVariance,
                    cap = StrokeCap.Round,
                )
            }
            drawLine(
                color = Color(0xFFD2B6FF).copy(alpha = 0.95f),
                start = Offset(viewportCenterX, size.height * 0.18f),
                end = Offset(viewportCenterX, size.height * 0.82f),
                strokeWidth = with(density) { 2.dp.toPx() },
                cap = StrokeCap.Round,
            )
        }
    }

    LaunchedEffect(positionMs, durationMs, waveformWidthPx) {
        if (!isUserScrubbing) {
            scrubOffsetPx = Float.NaN
        }
    }
}

private fun generateSyntheticWaveform(
    audiobookId: String,
    title: String,
    author: String?,
    durationMs: Long,
): IntArray {
    val durationMinutes = max(durationMs / 60_000L, 1L)
    val targetSampleCount = (
        MIN_WAVEFORM_SAMPLES + (durationMinutes / 6L).toInt()
    ).coerceIn(MIN_WAVEFORM_SAMPLES, MAX_WAVEFORM_SAMPLES)
    val seedSource = buildString {
        append(audiobookId)
        append('|')
        append(title)
        append('|')
        append(author.orEmpty())
        append('|')
        append(durationMs)
    }
    val random = Random(seedSource.hashCode())
    val samples = IntArray(targetSampleCount)
    var phase = random.nextFloat() * (2f * PI.toFloat())
    var anchorStart = 0.22f + random.nextFloat() * 0.45f
    var anchorEnd = 0.28f + random.nextFloat() * 0.52f
    var segmentLength = 20 + random.nextInt(38)
    var localIndex = 0

    for (index in samples.indices) {
        if (localIndex >= segmentLength) {
            phase += 0.45f + random.nextFloat() * 0.95f
            anchorStart = anchorEnd
            anchorEnd = (0.18f + random.nextFloat() * 0.68f).coerceIn(0.16f, 0.88f)
            segmentLength = 16 + random.nextInt(52)
            localIndex = 0
        }
        val progress = localIndex.toFloat() / segmentLength.toFloat()
        val envelope = interpolate(anchorStart, anchorEnd, smoothStep(progress))
        val harmonicA = sin((index * 0.085f) + phase) * (0.07f + envelope * 0.14f)
        val harmonicB = cos((index * 0.19f) + phase * 0.72f) * (0.04f + envelope * 0.08f)
        val harmonicC = sin((index * 0.38f) + phase * 1.31f) * 0.035f
        val jitter = (random.nextFloat() - 0.5f) * 0.045f
        val valleyBias = if (index % 11 == 0) -0.07f * random.nextFloat() else 0f
        val crestBias = if (index % 17 == 0) 0.09f * random.nextFloat() else 0f
        val shaped = (envelope + harmonicA + harmonicB + harmonicC + jitter + valleyBias + crestBias)
            .coerceIn(0.12f, 0.95f)
            .let { value ->
                val eased = 1f - ((1f - value) * (1f - value))
                14f + (eased * 86f)
            }
        samples[index] = shaped.toInt()
        localIndex += 1
    }
    return smoothSamples(samples, passes = 1)
}

private fun smoothSamples(samples: IntArray, passes: Int): IntArray {
    if (samples.size < 3) return samples
    var current = samples
    repeat(passes) {
        val smoothed = IntArray(current.size)
        for (index in current.indices) {
            val previous = current[max(index - 1, 0)]
            val center = current[index]
            val next = current[min(index + 1, current.lastIndex)]
            smoothed[index] = ((previous * 0.18f) + (center * 0.64f) + (next * 0.18f)).toInt()
        }
        current = smoothed
    }
    return current
}

private fun smoothStep(value: Float): Float {
    val clamped = value.coerceIn(0f, 1f)
    return clamped * clamped * (3f - (2f * clamped))
}

private fun interpolate(start: Float, end: Float, fraction: Float): Float {
    return start + ((end - start) * fraction.coerceIn(0f, 1f))
}

private fun positionToOffset(
    positionMs: Long,
    durationMs: Long,
    waveformWidthPx: Float,
): Float {
    if (durationMs <= 0L) return 0f
    return ((positionMs.coerceIn(0L, durationMs).toFloat() / durationMs.toFloat()) * waveformWidthPx)
        .coerceIn(0f, waveformWidthPx)
}

private fun offsetToPosition(
    offsetPx: Float,
    durationMs: Long,
    waveformWidthPx: Float,
): Long {
    if (durationMs <= 0L || waveformWidthPx <= 0f) return 0L
    val fraction = (offsetPx / waveformWidthPx).coerceIn(0f, 1f)
    return (durationMs * fraction).toLong()
}
