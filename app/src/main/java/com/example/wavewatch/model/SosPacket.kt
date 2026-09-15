package com.example.wavewatch.model

data class SosPacket(
    val packetId: String = "SOS-${System.currentTimeMillis().toString().takeLast(6)}",
    val timestamp: Long = System.currentTimeMillis(),
    val lat: Double = 18.9220,
    val lng: Double = 72.8347,
    val alertLevel: AlertLevel = AlertLevel.CRITICAL,
    val beaconStatus: String = "TRANSMITTING 406MHz / BLE RELAY",
    val isAcknowledged: Boolean = false
)
