package com.example.wavewatch

import com.example.wavewatch.data.TelemetrySimulator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MarineTelemetryViewModelTest {

    @Test
    fun testSimulatorFlow() = runTest {
        val simulator = TelemetrySimulator()
        val data = simulator.telemetry.first()
        assertNotNull(data)
    }
}
