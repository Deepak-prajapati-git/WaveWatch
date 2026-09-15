package com.example.wavewatch.model

data class HazardZone(
    val id: String,
    val title: String,
    val distanceNm: Float,
    val bearingDeg: Float,
    val severity: AlertLevel
)
