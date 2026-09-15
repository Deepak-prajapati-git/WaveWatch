package com.example.wavewatch.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wavewatch.model.AlertLevel
import com.example.wavewatch.ui.theme.CrimsonRed
import com.example.wavewatch.ui.theme.NeonCyan
import com.example.wavewatch.ui.theme.SurfaceDark
import com.example.wavewatch.viewmodel.MarineUiState

@Composable
fun MarineTacticalMapScreen(
    uiState: MarineUiState
) {
    val hazards = uiState.hazardZones
    val secondaryColor = MaterialTheme.colorScheme.secondary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "TACTICAL RADAR & GEOFENCE",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Radar Canvas Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(SurfaceDark, RoundedCornerShape(16.dp))
                .semantics { contentDescription = "Tactical radar map showing vessel position and ${hazards.size} hazard zones." },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val maxRadius = size.width.coerceAtMost(size.height) / 2f - 40f

                // Radar concentric rings
                drawCircle(color = NeonCyan.copy(alpha = 0.2f), radius = maxRadius * 0.25f, center = center, style = Stroke(width = 2f))
                drawCircle(color = NeonCyan.copy(alpha = 0.3f), radius = maxRadius * 0.50f, center = center, style = Stroke(width = 2f))
                drawCircle(color = NeonCyan.copy(alpha = 0.4f), radius = maxRadius * 0.75f, center = center, style = Stroke(width = 2f))
                drawCircle(color = NeonCyan, radius = maxRadius, center = center, style = Stroke(width = 3f))

                // Crosshairs
                drawLine(color = NeonCyan.copy(alpha = 0.3f), start = Offset(center.x, center.y - maxRadius), end = Offset(center.x, center.y + maxRadius), strokeWidth = 1.5f)
                drawLine(color = NeonCyan.copy(alpha = 0.3f), start = Offset(center.x - maxRadius, center.y), end = Offset(center.x + maxRadius, center.y), strokeWidth = 1.5f)

                // Vessel Wake Trail
                val wakePath = Path().apply {
                    moveTo(center.x, center.y)
                    lineTo(center.x - 10f, center.y + 40f)
                    lineTo(center.x, center.y + 70f)
                    lineTo(center.x + 10f, center.y + 40f)
                    close()
                }
                drawPath(path = wakePath, color = NeonCyan.copy(alpha = 0.3f))

                // Vessel Icon (Center)
                drawCircle(color = NeonCyan, radius = 8f, center = center)

                // Plot Hazards
                hazards.forEach { hazard ->
                    val angleRad = Math.toRadians(hazard.bearingDeg.toDouble() - 90.0)
                    val distRatio = (hazard.distanceNm / 10f).coerceIn(0.1f, 0.95f)
                    val hx = center.x + (maxRadius * distRatio * Math.cos(angleRad)).toFloat()
                    val hy = center.y + (maxRadius * distRatio * Math.sin(angleRad)).toFloat()

                    val hColor = when (hazard.severity) {
                        AlertLevel.CRITICAL -> CrimsonRed
                        AlertLevel.ROUGH -> secondaryColor
                        else -> NeonCyan
                    }

                    drawCircle(color = hColor, radius = 10f, center = Offset(hx, hy))
                    drawCircle(color = hColor, radius = 18f, center = Offset(hx, hy), style = Stroke(width = 2f))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Proximity HUD Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "PROXIMITY HAZARD ALERTS",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                hazards.forEach { hazard ->
                    val color = if (hazard.severity == AlertLevel.CRITICAL) CrimsonRed else MaterialTheme.colorScheme.onSurface
                    Text(
                        text = "• ${hazard.title}: ${hazard.distanceNm} NM away (${hazard.bearingDeg.toInt()}°)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = color
                    )
                }
            }
        }
    }
}
