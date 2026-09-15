package com.example.wavewatch.model

data class UserPreferences(
    val hasCompletedOnboarding: Boolean = false,
    val unitSystem: UnitSystem = UnitSystem.METRIC,
    val monitoringActive: Boolean = false,
    val hapticsEnabled: Boolean = true
)
