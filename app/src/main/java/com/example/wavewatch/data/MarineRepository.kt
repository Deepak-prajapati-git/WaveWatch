package com.example.wavewatch.data

import com.example.wavewatch.model.AlertLevel
import com.example.wavewatch.model.SosPacket
import com.example.wavewatch.model.TelemetryData
import com.example.wavewatch.model.UnitSystem
import com.example.wavewatch.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MarineRepository(
    private val marineDao: MarineDao,
    private val preferencesRepository: UserPreferencesRepository,
    val telemetrySimulator: TelemetrySimulator
) {
    val userPreferences: Flow<UserPreferences> = preferencesRepository.userPreferencesFlow
    val telemetryFlow: Flow<TelemetryData> = telemetrySimulator.telemetry
    val isHardwareConnected: Flow<Boolean> = telemetrySimulator.isHardwareConnected
    val isGpsLocked: Flow<Boolean> = telemetrySimulator.isGpsLocked

    suspend fun logTelemetry(data: TelemetryData) {
        marineDao.insertTelemetry(
            TelemetryLogEntity(
                timestamp = data.timestamp,
                waveHeight = data.waveHeight,
                wavePeriod = data.wavePeriod,
                pitch = data.pitch,
                roll = data.roll,
                speedKnots = data.speedKnots,
                alertLevel = data.alertLevel.name
            )
        )
    }

    suspend fun logSosPacket(packet: SosPacket) {
        marineDao.insertSos(
            SosLogEntity(
                packetId = packet.packetId,
                timestamp = packet.timestamp,
                lat = packet.lat,
                lng = packet.lng,
                alertLevel = packet.alertLevel.name,
                beaconStatus = packet.beaconStatus,
                isAcknowledged = packet.isAcknowledged
            )
        )
    }

    val sosLogs: Flow<List<SosPacket>> = marineDao.getSosLogs().map { list ->
        list.map { entity ->
            SosPacket(
                packetId = entity.packetId,
                timestamp = entity.timestamp,
                lat = entity.lat,
                lng = entity.lng,
                alertLevel = AlertLevel.valueOf(entity.alertLevel),
                beaconStatus = entity.beaconStatus,
                isAcknowledged = entity.isAcknowledged
            )
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        preferencesRepository.setOnboardingCompleted(completed)
    }

    suspend fun setUnitSystem(unitSystem: UnitSystem) {
        preferencesRepository.setUnitSystem(unitSystem)
    }

    suspend fun setMonitoringActive(active: Boolean) {
        preferencesRepository.setMonitoringActive(active)
    }

    suspend fun setHapticsEnabled(enabled: Boolean) {
        preferencesRepository.setHapticsEnabled(enabled)
    }

    // Demo Preset triggers
    fun triggerCalmSea() = telemetrySimulator.triggerCalmSea()
    fun triggerRoughWeather() = telemetrySimulator.triggerRoughWeather()
    fun triggerBorderBreach() = telemetrySimulator.triggerBorderBreach()
    fun triggerNormal() = telemetrySimulator.triggerNormal()
    fun simulateGpsDropout() = telemetrySimulator.simulateGpsDropout()
    fun simulateHardwareDisconnect() = telemetrySimulator.simulateHardwareDisconnect()
    fun toggleHardwareConnection() = telemetrySimulator.toggleHardwareConnection()
}
