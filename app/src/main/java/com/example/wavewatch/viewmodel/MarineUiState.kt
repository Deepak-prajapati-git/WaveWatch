package com.example.wavewatch.viewmodel

import com.example.wavewatch.model.AlertLevel
import com.example.wavewatch.model.HazardZone
import com.example.wavewatch.model.SosPacket
import com.example.wavewatch.model.TelemetryData
import com.example.wavewatch.model.UnitSystem
import com.example.wavewatch.model.UserPreferences

data class MarineUiState(
    val telemetry: TelemetryData = TelemetryData(),
    val userPreferences: UserPreferences = UserPreferences(),
    val isHardwareConnected: Boolean = true,
    val isGpsLocked: Boolean = true,
    val activeSosPacket: SosPacket? = null,
    val hazardZones: List<HazardZone> = listOf(
        HazardZone("H1", "Colaba Reef Shallows", 1.4f, 45f, AlertLevel.MODERATE),
        HazardZone("H2", "International Maritime Boundary", 8.2f, 275f, AlertLevel.CRITICAL),
        HazardZone("H3", "Heavy Tanker Traffic Lane", 3.1f, 120f, AlertLevel.ROUGH)
    ),
    val sosLogs: List<SosPacket> = emptyList(),
    val stalenessTimestamp: Long = System.currentTimeMillis()
)
