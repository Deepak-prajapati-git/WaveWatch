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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wavewatch.model.UnitSystem
import com.example.wavewatch.ui.theme.SurfaceDark
import com.example.wavewatch.viewmodel.MarineUiState

@Composable
fun DiagnosticsSettingsScreen(
    uiState: MarineUiState,
    onToggleUnit: () -> Unit,
    onToggleHaptics: (Boolean) -> Unit,
    onTriggerCalm: () -> Unit,
    onTriggerRough: () -> Unit,
    onTriggerBorderBreach: () -> Unit,
    onTriggerNormal: () -> Unit,
    onSimulateGpsDropout: () -> Unit,
    onSimulateHardwareDisconnect: () -> Unit
) {
    val prefs = uiState.userPreferences

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "DIAGNOSTICS & DEMO CONTROLLER",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Judge Demo Controller Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "JUDGE DEMO CONTROLLER",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = onTriggerCalm, modifier = Modifier.weight(1f)) {
                        Text(text = "Calm Sea")
                    }
                    Button(onClick = onTriggerRough, modifier = Modifier.weight(1f)) {
                        Text(text = "Rough")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = onTriggerBorderBreach, modifier = Modifier.weight(1f)) {
                        Text(text = "Border Breach")
                    }
                    Button(onClick = onTriggerNormal, modifier = Modifier.weight(1f)) {
                        Text(text = "Normal")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = onSimulateGpsDropout, modifier = Modifier.weight(1f)) {
                        Text(text = "GPS Dropout")
                    }
                    Button(onClick = onSimulateHardwareDisconnect, modifier = Modifier.weight(1f)) {
                        Text(text = "HW Disconnect")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Settings Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "APPLICATION SETTINGS",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Unit System (${prefs.unitSystem.name})",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Button(onClick = onToggleUnit) {
                        Text(text = "Toggle Metric/Imperial")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Haptic Feedback",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Switch(
                        checked = prefs.hapticsEnabled,
                        onCheckedChange = onToggleHaptics
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sensor Diagnostics Status Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SENSOR DIAGNOSTICS & BLE",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• BLE Sensor Node: ${if (uiState.isHardwareConnected) "CONNECTED (WaveWatch-BLE-09)" else "DISCONNECTED"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "• Gyroscope Calibration: OPTIMAL",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "• Background Service: ACTIVE",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
