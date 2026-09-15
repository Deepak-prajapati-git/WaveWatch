package com.example.wavewatch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.GpsOff
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wavewatch.model.AlertLevel
import com.example.wavewatch.model.UnitSystem
import com.example.wavewatch.ui.components.ArtificialHorizonView
import com.example.wavewatch.ui.components.MetricGlassCard
import com.example.wavewatch.ui.components.WaveSwellWaveformView
import com.example.wavewatch.ui.theme.CrimsonRed
import com.example.wavewatch.ui.theme.NeonCyan
import com.example.wavewatch.ui.theme.SuccessGreen
import com.example.wavewatch.viewmodel.MarineUiState
import java.util.Locale

@Composable
fun DashboardScreen(
    uiState: MarineUiState
) {
    val telemetry = uiState.telemetry
    val prefs = uiState.userPreferences
    val isMetric = prefs.unitSystem == UnitSystem.METRIC

    val waveHeightDisplay = if (isMetric) {
        String.format(Locale.getDefault(), "%.1f", telemetry.waveHeight)
    } else {
        String.format(Locale.getDefault(), "%.1f", telemetry.waveHeight * 3.28084f)
    }
    val waveHeightUnit = if (isMetric) "m" else "ft"

    val tempDisplay = if (isMetric) {
        String.format(Locale.getDefault(), "%.1f", telemetry.waterTemp)
    } else {
        String.format(Locale.getDefault(), "%.1f", telemetry.waterTemp * 9f / 5f + 32f)
    }
    val tempUnit = if (isMetric) "°C" else "°F"

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "MARINE TELEMETRY",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = if (uiState.isHardwareConnected) "Sensor Hub: ONLINE" else "Sensor Hub: DISCONNECTED",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (uiState.isHardwareConnected) SuccessGreen else CrimsonRed
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (uiState.isGpsLocked) Icons.Default.GpsFixed else Icons.Default.GpsOff,
                    contentDescription = "GPS Status",
                    tint = if (uiState.isGpsLocked) NeonCyan else CrimsonRed
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.BatteryFull,
                    contentDescription = "Battery",
                    tint = SuccessGreen
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${telemetry.batteryPct}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Alert Banner if Rough or Critical
        if (telemetry.alertLevel == AlertLevel.ROUGH || telemetry.alertLevel == AlertLevel.CRITICAL) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (telemetry.alertLevel == AlertLevel.CRITICAL) CrimsonRed.copy(alpha = 0.3f) else MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "WARNING: ${telemetry.alertLevel.name} SEA STATE",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (telemetry.alertLevel == AlertLevel.CRITICAL) CrimsonRed else MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Artificial Horizon & Waveform Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "GYRO / HORIZON",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                ArtificialHorizonView(
                    roll = telemetry.roll,
                    pitch = telemetry.pitch,
                    alertLevel = telemetry.alertLevel,
                    size = 150.dp
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "WAVE SWELL",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                WaveSwellWaveformView(
                    waveHeight = telemetry.waveHeight,
                    wavePeriod = telemetry.wavePeriod,
                    height = 150.dp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Metric Cards Grid
        Text(
            text = "LIVE SENSORS",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MetricGlassCard(
                title = "Wave Height",
                value = waveHeightDisplay,
                unit = waveHeightUnit,
                icon = Icons.Default.Waves,
                trendText = "Swell active",
                modifier = Modifier.weight(1f),
                accentColor = NeonCyan
            )
            MetricGlassCard(
                title = "Wave Period",
                value = String.format(Locale.getDefault(), "%.1f", telemetry.wavePeriod),
                unit = "s",
                icon = Icons.Default.Water,
                trendText = "Interval rate",
                modifier = Modifier.weight(1f),
                accentColor = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MetricGlassCard(
                title = "Water Temp",
                value = tempDisplay,
                unit = tempUnit,
                icon = Icons.Default.Thermostat,
                trendText = "Subsea probe",
                modifier = Modifier.weight(1f),
                accentColor = SuccessGreen
            )
            MetricGlassCard(
                title = "Speed",
                value = String.format(Locale.getDefault(), "%.1f", telemetry.speedKnots),
                unit = "knots",
                icon = Icons.Default.Waves,
                trendText = "Vessel velocity",
                modifier = Modifier.weight(1f),
                accentColor = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
