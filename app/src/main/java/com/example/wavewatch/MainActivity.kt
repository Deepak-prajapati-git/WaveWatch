package com.example.wavewatch

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wavewatch.data.AppDatabase
import com.example.wavewatch.data.MarineRepository
import com.example.wavewatch.data.TelemetrySimulator
import com.example.wavewatch.data.UserPreferencesRepository
import com.example.wavewatch.service.MarineMonitoringService
import com.example.wavewatch.ui.components.PermissionGate
import com.example.wavewatch.ui.navigation.Screen
import com.example.wavewatch.ui.screens.DashboardScreen
import com.example.wavewatch.ui.screens.DiagnosticsSettingsScreen
import com.example.wavewatch.ui.screens.MarineTacticalMapScreen
import com.example.wavewatch.ui.screens.OnboardingScreen
import com.example.wavewatch.ui.screens.SosCenterScreen
import com.example.wavewatch.ui.theme.CrimsonRed
import com.example.wavewatch.ui.theme.NeonCyan
import com.example.wavewatch.ui.theme.SurfaceDark
import com.example.wavewatch.ui.theme.WaveWatchTheme
import com.example.wavewatch.viewmodel.MarineTelemetryViewModel

class MainActivity : ComponentActivity() {

    private val db by lazy { AppDatabase.getDatabase(applicationContext) }
    private val prefsRepo by lazy { UserPreferencesRepository(applicationContext) }
    private val simulator by lazy { TelemetrySimulator() }
    private val repository by lazy { MarineRepository(db.marineDao(), prefsRepo, simulator) }

    private val viewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MarineTelemetryViewModel(repository) as T
            }
        })[MarineTelemetryViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Start background service if monitoring active
        val serviceIntent = Intent(this, MarineMonitoringService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }

        setContent {
            WaveWatchTheme {
                PermissionGate {
                    WaveWatchApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun WaveWatchApp(viewModel: MarineTelemetryViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()

    val hasCompletedOnboarding = uiState.userPreferences.hasCompletedOnboarding

    val startDestination = if (hasCompletedOnboarding) Screen.Dashboard.route else Screen.Onboarding.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (hasCompletedOnboarding) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBar(
                    containerColor = SurfaceDark
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                        label = { Text("Dashboard") },
                        selected = currentRoute == Screen.Dashboard.route,
                        onClick = {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = NeonCyan)
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Map, contentDescription = "Tactical Map") },
                        label = { Text("Radar") },
                        selected = currentRoute == Screen.TacticalMap.route,
                        onClick = {
                            navController.navigate(Screen.TacticalMap.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = NeonCyan)
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Report, contentDescription = "SOS Rescue", tint = CrimsonRed) },
                        label = { Text("SOS", color = CrimsonRed) },
                        selected = currentRoute == Screen.SosCenter.route,
                        onClick = {
                            navController.navigate(Screen.SosCenter.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = CrimsonRed)
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Diagnostics") },
                        label = { Text("Demo") },
                        selected = currentRoute == Screen.Diagnostics.route,
                        onClick = {
                            navController.navigate(Screen.Diagnostics.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = NeonCyan)
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    onFinishOnboarding = {
                        viewModel.setOnboardingCompleted(true)
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Dashboard.route) {
                DashboardScreen(uiState = uiState)
            }
            composable(Screen.TacticalMap.route) {
                MarineTacticalMapScreen(uiState = uiState)
            }
            composable(Screen.SosCenter.route) {
                SosCenterScreen(
                    uiState = uiState,
                    onTriggerSos = { viewModel.triggerSos() },
                    onCancelSos = { viewModel.cancelSos() }
                )
            }
            composable(Screen.Diagnostics.route) {
                DiagnosticsSettingsScreen(
                    uiState = uiState,
                    onToggleUnit = { viewModel.toggleUnitSystem() },
                    onToggleHaptics = { viewModel.setHapticsEnabled(it) },
                    onTriggerCalm = { viewModel.triggerCalmSea() },
                    onTriggerRough = { viewModel.triggerRoughWeather() },
                    onTriggerBorderBreach = { viewModel.triggerBorderBreach() },
                    onTriggerNormal = { viewModel.triggerNormal() },
                    onSimulateGpsDropout = { viewModel.simulateGpsDropout() },
                    onSimulateHardwareDisconnect = { viewModel.simulateHardwareDisconnect() }
                )
            }
        }
    }
}
