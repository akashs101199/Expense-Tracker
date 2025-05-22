package com.example.expensetracker.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class PreferenceManager(private val context: Context) {
    private val INCOME_KEY = floatPreferencesKey("user_income")
    private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode_enabled")

    val incomeFlow = context.dataStore.data.map { prefs ->
        prefs[INCOME_KEY] ?: 0f
    }

    suspend fun saveIncome(value: Float) {
        context.dataStore.edit { prefs ->
            prefs[INCOME_KEY] = value
        }
    }

    val darkModeFlow = context.dataStore.data.map { prefs ->
        prefs[DARK_MODE_KEY] ?: false
    }

    suspend fun saveDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DARK_MODE_KEY] = enabled
        }
    }
}
