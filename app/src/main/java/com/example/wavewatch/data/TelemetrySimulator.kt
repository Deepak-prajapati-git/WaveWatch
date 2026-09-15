package com.example.wavewatch.data

import com.example.wavewatch.model.AlertLevel
import com.example.wavewatch.model.TelemetryData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sin

class TelemetrySimulator {

    private val scope = CoroutineScope(Dispatchers.Default + Job())
    private var simulationJob: Job? = null

    private val _telemetry = MutableStateFlow(TelemetryData())
    val telemetry: StateFlow<TelemetryData> = _telemetry.asStateFlow()

    private val _isHardwareConnected = MutableStateFlow(true)
    val isHardwareConnected: StateFlow<Boolean> = _isHardwareConnected.asStateFlow()

    private val _isGpsLocked = MutableStateFlow(true)
    val isGpsLocked: StateFlow<Boolean> = _isGpsLocked.asStateFlow()

    private var targetWaveHeight = 1.2f
    private var targetWavePeriod = 6.5f
    private var targetPitch = 2.1f
    private var targetRoll = -1.5f
    private var targetLat = 18.9220
    private var targetLng = 72.8347
    private var targetSpeed = 8.4f

    private var currentWaveHeight = 1.2f
    private var currentWavePeriod = 6.5f
    private var currentPitch = 2.1f
    private var currentRoll = -1.5f
    private var currentLat = 18.9220
    private var currentLng = 72.8347
    private var currentSpeed = 8.4f

    init {
        startSimulation()
    }

    fun startSimulation() {
        if (simulationJob?.isActive == true) return
        simulationJob = scope.launch {
            var timeStep = 0f
            while (isActive) {
                delay(250) // 250ms ticks
                timeStep += 0.25f

                // Smoothly interpolate towards target values (1.5s transition rate ~ alpha 0.15)
                val alpha = 0.15f
                currentWaveHeight += (targetWaveHeight - currentWaveHeight) * alpha
                currentWavePeriod += (targetWavePeriod - currentWavePeriod) * alpha
                currentPitch += (targetPitch - currentPitch) * alpha
                currentRoll += (targetRoll - currentRoll) * alpha
                currentLat += (targetLat - currentLat) * alpha
                currentLng += (targetLng - currentLng) * alpha
                currentSpeed += (targetSpeed - currentSpeed) * alpha

                // Add slight sinusoidal oscillation for realism
                val dynamicPitch = currentPitch + (sin(timeStep.toDouble() * 1.5).toFloat() * 1.2f)
                val dynamicRoll = currentRoll + (sin(timeStep.toDouble() * 2.0).toFloat() * 1.5f)
                val dynamicHeight = (currentWaveHeight + (sin(timeStep.toDouble() * 0.8).toFloat() * 0.2f)).coerceAtLeast(0.1f)

                // GPS drift near Mumbai
                val driftLat = currentLat + (sin(timeStep.toDouble() * 0.1) * 0.0001)
                val driftLng = currentLng + (sin(timeStep.toDouble() * 0.15) * 0.0001)

                val alert = when {
                    dynamicHeight > 3.5f || abs(dynamicRoll) > 12f -> AlertLevel.CRITICAL
                    dynamicHeight > 2.2f || abs(dynamicRoll) > 7f -> AlertLevel.ROUGH
                    dynamicHeight > 1.5f || abs(dynamicRoll) > 4f -> AlertLevel.MODERATE
                    else -> AlertLevel.CALM
                }

                val connected = _isHardwareConnected.value
                val gpsLocked = _isGpsLocked.value

                _telemetry.value = TelemetryData(
                    waveHeight = if (connected) dynamicHeight else 0f,
                    wavePeriod = if (connected) currentWavePeriod else 0f,
                    pitch = if (connected) dynamicPitch else 0f,
                    roll = if (connected) dynamicRoll else 0f,
                    gpsLat = if (gpsLocked) driftLat else 0.0,
                    gpsLng = if (gpsLocked) driftLng else 0.0,
                    speedKnots = if (connected) currentSpeed else 0f,
                    waterTemp = if (connected) 27.5f else 0.0f,
                    batteryPct = if (connected) 92 else 0,
                    signalStrength = if (connected && gpsLocked) 4 else 0,
                    alertLevel = if (connected) alert else AlertLevel.CRITICAL,
                    timestamp = System.currentTimeMillis()
                )
            }
        }
    }

    fun triggerCalmSea() {
        targetWaveHeight = 0.8f
        targetWavePeriod = 7.0f
        targetPitch = 1.0f
        targetRoll = -0.5f
        targetLat = 18.9220
        targetLng = 72.8347
        targetSpeed = 6.0f
        _isHardwareConnected.value = true
        _isGpsLocked.value = true
    }

    fun triggerRoughWeather() {
        targetWaveHeight = 4.8f
        targetWavePeriod = 4.0f
        targetPitch = 9.5f
        targetRoll = -11.0f
        targetLat = 18.9500
        targetLng = 72.8000
        targetSpeed = 12.5f
        _isHardwareConnected.value = true
        _isGpsLocked.value = true
    }

    fun triggerBorderBreach() {
        // International Maritime Boundary Line near India/Pakistan or India/Sri Lanka (e.g. 20.50 N, 68.10 E)
        targetWaveHeight = 2.5f
        targetWavePeriod = 5.5f
        targetPitch = 4.0f
        targetRoll = 3.0f
        targetLat = 20.4500
        targetLng = 68.2000
        targetSpeed = 14.0f
        _isHardwareConnected.value = true
        _isGpsLocked.value = true
    }

    fun triggerNormal() {
        targetWaveHeight = 1.2f
        targetWavePeriod = 6.5f
        targetPitch = 2.1f
        targetRoll = -1.5f
        targetLat = 18.9220
        targetLng = 72.8347
        targetSpeed = 8.4f
        _isHardwareConnected.value = true
        _isGpsLocked.value = true
    }

    fun simulateGpsDropout() {
        _isGpsLocked.value = false
    }

    fun simulateHardwareDisconnect() {
        _isHardwareConnected.value = false
    }

    fun toggleHardwareConnection() {
        _isHardwareConnected.value = !_isHardwareConnected.value
    }
}
