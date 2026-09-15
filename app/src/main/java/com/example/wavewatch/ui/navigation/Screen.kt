package com.example.wavewatch.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object TacticalMap : Screen("tactical_map")
    object SosCenter : Screen("sos_center")
    object Diagnostics : Screen("diagnostics")
    object Onboarding : Screen("onboarding")
}
