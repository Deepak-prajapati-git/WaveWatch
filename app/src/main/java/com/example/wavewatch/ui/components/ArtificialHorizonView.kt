package com.example.wavewatch.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.wavewatch.model.AlertLevel
import com.example.wavewatch.ui.theme.CrimsonRed
import com.example.wavewatch.ui.theme.NeonCyan
import com.example.wavewatch.ui.theme.SuccessGreen
import com.example.wavewatch.ui.theme.SurfaceDark

@Composable
fun ArtificialHorizonView(
    roll: Float, // degrees
    pitch: Float, // degrees
    alertLevel: AlertLevel,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp
) {
    val animatedRoll by animateFloatAsState(
        targetValue = roll,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "roll"
    )
    val animatedPitch by animateFloatAsState(
        targetValue = pitch,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "pitch"
    )

    val ringColor by animateColorAsState(
        targetValue = when (alertLevel) {
            AlertLevel.CALM -> SuccessGreen
            AlertLevel.MODERATE -> NeonCyan
            AlertLevel.ROUGH -> MaterialTheme.colorScheme.secondary
            AlertLevel.CRITICAL -> CrimsonRed
        },
        label = "ringColor"
    )

    Box(
        modifier = modifier
            .size(size)
            .semantics { contentDescription = "Artificial Horizon Gyro. Roll: ${roll.toInt()} deg, Pitch: ${pitch.toInt()} deg" },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.toPx() / 2f, size.toPx() / 2f)
            val radius = size.toPx() / 2f - 8f

            // Outer bezel
            drawCircle(
                color = SurfaceDark,
                radius = radius,
                center = center
            )
            drawCircle(
                color = ringColor,
                radius = radius,
                center = center,
                style = Stroke(width = 4f)
            )

            withTransform({
                translate(center.x, center.y)
                rotate(animatedRoll)
                translate(-center.x, -center.y)
            }) {
                // Pitch translation (pixels per degree)
                val pitchOffsetY = animatedPitch * 4f
                val horizonCenterY = center.y + pitchOffsetY

                // Sky (upper half)
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF0284C7), Color(0xFF38BDF8)),
                        startY = 0f,
                        endY = horizonCenterY
                    )
                )

                // Sea (lower half)
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF0F172A), Color(0xFF1E3A8A)),
                        startY = horizonCenterY,
                        endY = size.toPx()
                    )
                )

                // Horizon line
                drawLine(
                    color = Color.White,
                    start = Offset(center.x - radius, horizonCenterY),
                    end = Offset(center.x + radius, horizonCenterY),
                    strokeWidth = 3f
                )
            }

            // Fixed aircraft / boat reference reticle
            val reticleColor = Color(0xFFE2E8F0)
            // Center dot
            drawCircle(color = reticleColor, radius = 4f, center = center)
            // Left wing
            drawLine(
                color = reticleColor,
                start = Offset(center.x - 50f, center.y),
                end = Offset(center.x - 20f, center.y),
                strokeWidth = 4f
            )
            // Right wing
            drawLine(
                color = reticleColor,
                start = Offset(center.x + 20f, center.y),
                end = Offset(center.x + 50f, center.y),
                strokeWidth = 4f
            )
            // Center pointer
            drawLine(
                color = ringColor,
                start = Offset(center.x, center.y - 15f),
                end = Offset(center.x, center.y - 30f),
                strokeWidth = 3f
            )
        }
    }
}
