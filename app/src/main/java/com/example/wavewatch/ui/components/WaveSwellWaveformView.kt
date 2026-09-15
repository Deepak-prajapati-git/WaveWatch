package com.example.wavewatch.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.wavewatch.ui.theme.NeonCyan
import kotlin.math.sin

@Composable
fun WaveSwellWaveformView(
    waveHeight: Float,
    wavePeriod: Float,
    modifier: Modifier = Modifier,
    height: Dp = 120.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveAnim")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .semantics { contentDescription = "Waveform swell visualization. Height: $waveHeight meters" }
    ) {
        val widthPx = size.width
        val heightPx = size.height
        val amplitude = (waveHeight * 15f).coerceIn(10f, heightPx / 2.5f)
        val frequency = (1f / wavePeriod.coerceAtLeast(1f)) * 0.05f

        // Background Wave Path (Parallax)
        val backPath = Path().apply {
            moveTo(0f, heightPx)
            val step = 10f
            var x = 0f
            while (x <= widthPx) {
                val y = heightPx * 0.6f + sin((x * frequency) + phase + 1.57f) * (amplitude * 0.7f)
                lineTo(x, y)
                x += step
            }
            lineTo(widthPx, heightPx)
            close()
        }

        drawPath(
            path = backPath,
            brush = Brush.verticalGradient(
                colors = listOf(NeonCyan.copy(alpha = 0.2f), Color.Transparent),
                startY = 0f,
                endY = heightPx
            )
        )

        // Foreground Wave Path
        val frontPath = Path().apply {
            moveTo(0f, heightPx)
            val step = 10f
            var x = 0f
            while (x <= widthPx) {
                val y = heightPx * 0.5f + sin((x * frequency * 1.2f) + phase) * amplitude
                lineTo(x, y)
                x += step
            }
            lineTo(widthPx, heightPx)
            close()
        }

        drawPath(
            path = frontPath,
            brush = Brush.verticalGradient(
                colors = listOf(NeonCyan.copy(alpha = 0.5f), NeonCyan.copy(alpha = 0.05f)),
                startY = 0f,
                endY = heightPx
            )
        )

        // Wave border stroke
        val strokePath = Path().apply {
            val step = 10f
            var x = 0f
            var first = true
            while (x <= widthPx) {
                val y = heightPx * 0.5f + sin((x * frequency * 1.2f) + phase) * amplitude
                if (first) {
                    moveTo(x, y)
                    first = false
                } else {
                    lineTo(x, y)
                }
                x += step
            }
        }

        drawPath(
            path = strokePath,
            color = NeonCyan,
            style = Stroke(width = 3f)
        )
    }
}
