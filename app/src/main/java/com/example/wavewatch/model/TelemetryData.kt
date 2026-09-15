package com.example.wavewatch.model

data class TelemetryData(
    val waveHeight: Float = 1.2f,     // meters
    val wavePeriod: Float = 6.5f,     // seconds
    val pitch: Float = 2.1f,          // degrees
    val roll: Float = -1.5f,          // degrees
    val gpsLat: Double = 18.9220,     // Mumbai harbor default
    val gpsLng: Double = 72.8347,
    val speedKnots: Float = 8.4f,     // knots
    val waterTemp: Float = 27.5f,     // Celsius
    val batteryPct: Int = 92,         // %
    val signalStrength: Int = 4,      // 0-5 bars
    val alertLevel: AlertLevel = AlertLevel.CALM,
    val timestamp: Long = System.currentTimeMillis()
)
