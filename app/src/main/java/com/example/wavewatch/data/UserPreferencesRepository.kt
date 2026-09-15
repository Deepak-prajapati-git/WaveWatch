package com.example.wavewatch.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.wavewatch.model.UnitSystem
import com.example.wavewatch.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val UNIT_SYSTEM = stringPreferencesKey("unit_system")
        val MONITORING_ACTIVE = booleanPreferencesKey("monitoring_active")
        val HAPTICS_ENABLED = booleanPreferencesKey("haptics_enabled")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data
        .map { preferences ->
            val completed = preferences[PreferencesKeys.ONBOARDING_COMPLETED] ?: false
            val unitStr = preferences[PreferencesKeys.UNIT_SYSTEM] ?: UnitSystem.METRIC.name
            val unitSystem = try { UnitSystem.valueOf(unitStr) } catch (e: Exception) { UnitSystem.METRIC }
            val monitoring = preferences[PreferencesKeys.MONITORING_ACTIVE] ?: false
            val haptics = preferences[PreferencesKeys.HAPTICS_ENABLED] ?: true
            UserPreferences(
                hasCompletedOnboarding = completed,
                unitSystem = unitSystem,
                monitoringActive = monitoring,
                hapticsEnabled = haptics
            )
        }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun setUnitSystem(unitSystem: UnitSystem) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.UNIT_SYSTEM] = unitSystem.name
        }
    }

    suspend fun setMonitoringActive(active: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.MONITORING_ACTIVE] = active
        }
    }

    suspend fun setHapticsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAPTICS_ENABLED] = enabled
        }
    }
}
