package com.example.wavewatch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wavewatch.data.MarineRepository
import com.example.wavewatch.model.SosPacket
import com.example.wavewatch.model.UnitSystem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class MarineEvent {
    data class ShowToast(val message: String) : MarineEvent()
    data class SosTriggered(val packet: SosPacket) : MarineEvent()
    object SosCancelled : MarineEvent()
}

class MarineTelemetryViewModel(
    private val repository: MarineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MarineUiState())
    val uiState: StateFlow<MarineUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<MarineEvent>()
    val eventFlow: SharedFlow<MarineEvent> = _eventFlow.asSharedFlow()

    private var lastLoggedTime = 0L

    init {
        // Collect telemetry, preferences, hardware, gps, sos logs
        viewModelScope.launch {
            combine(
                repository.telemetryFlow,
                repository.userPreferences,
                repository.isHardwareConnected,
                repository.isGpsLocked,
                repository.sosLogs
            ) { telemetry, prefs, hardware, gps, sosList ->
                // Throttled logging to Room (every 10 seconds)
                val now = System.currentTimeMillis()
                if (now - lastLoggedTime > 10000 && prefs.monitoringActive) {
                    lastLoggedTime = now
                    repository.logTelemetry(telemetry)
                }

                MarineUiState(
                    telemetry = telemetry,
                    userPreferences = prefs,
                    isHardwareConnected = hardware,
                    isGpsLocked = gps,
                    activeSosPacket = _uiState.value.activeSosPacket,
                    sosLogs = sosList,
                    stalenessTimestamp = now
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun triggerCalmSea() = repository.triggerCalmSea()
    fun triggerRoughWeather() = repository.triggerRoughWeather()
    fun triggerBorderBreach() = repository.triggerBorderBreach()
    fun triggerNormal() = repository.triggerNormal()
    fun simulateGpsDropout() = repository.simulateGpsDropout()
    fun simulateHardwareDisconnect() = repository.simulateHardwareDisconnect()
    fun toggleHardwareConnection() = repository.toggleHardwareConnection()

    fun toggleUnitSystem() {
        viewModelScope.launch {
            val current = _uiState.value.userPreferences.unitSystem
            val newSystem = if (current == UnitSystem.METRIC) UnitSystem.IMPERIAL else UnitSystem.METRIC
            repository.setUnitSystem(newSystem)
        }
    }

    fun setMonitoringActive(active: Boolean) {
        viewModelScope.launch {
            repository.setMonitoringActive(active)
        }
    }

    fun setHapticsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setHapticsEnabled(enabled)
        }
    }

    fun setOnboardingCompleted(completed: Boolean) {
        viewModelScope.launch {
            repository.setOnboardingCompleted(completed)
        }
    }

    fun triggerSos() {
        viewModelScope.launch {
            val currentTel = _uiState.value.telemetry
            val packet = SosPacket(
                lat = currentTel.gpsLat,
                lng = currentTel.gpsLng,
                alertLevel = currentTel.alertLevel
            )
            _uiState.update { it.copy(activeSosPacket = packet) }
            repository.logSosPacket(packet)
            _eventFlow.emit(MarineEvent.SosTriggered(packet))
            _eventFlow.emit(MarineEvent.ShowToast("SOS DISTRESS BEACON ACTIVATED"))
        }
    }

    fun cancelSos() {
        viewModelScope.launch {
            val active = _uiState.value.activeSosPacket
            if (active != null) {
                repository.logSosPacket(active.copy(beaconStatus = "CANCELLED / RESOLVED"))
            }
            _uiState.update { it.copy(activeSosPacket = null) }
            _eventFlow.emit(MarineEvent.SosCancelled)
            _eventFlow.emit(MarineEvent.ShowToast("SOS distress signal cancelled"))
        }
    }
}
