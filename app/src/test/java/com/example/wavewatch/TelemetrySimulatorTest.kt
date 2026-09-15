package com.example.wavewatch

import com.example.wavewatch.data.TelemetrySimulator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TelemetrySimulatorTest {

    @Test
    fun testSimulatorInitialState() = runTest {
        val simulator = TelemetrySimulator()
        val telemetry = simulator.telemetry.first()
        assertTrue(telemetry.waveHeight > 0f)
        assertTrue(simulator.isHardwareConnected.first())
    }

    @Test
    fun testTriggerCalmSea() = runTest {
        val simulator = TelemetrySimulator()
        simulator.triggerCalmSea()
        val telemetry = simulator.telemetry.first()
        assertEquals(18.9220, telemetry.gpsLat, 0.0001)
    }

    @Test
    fun testHardwareDisconnect() = runTest {
        val simulator = TelemetrySimulator()
        simulator.simulateHardwareDisconnect()
        val connected = simulator.isHardwareConnected.first()
        assertTrue(!connected)
    }
}
