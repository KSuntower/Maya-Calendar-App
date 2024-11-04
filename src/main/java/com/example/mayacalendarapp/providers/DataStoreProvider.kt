package com.example.mayacalendarapp.providers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

data class AppPreferences(
    val currentMonth: Int,
    val currentMonthNumber: Int,
    val currentDay: Int,
    val currentDayNumber: Int,
)

private object PreferenceKeys {
    val CURRENT_MONTH = intPreferencesKey("month")
    val CURRENT_MONTH_NUMBER = intPreferencesKey("month_number")
    val CURRENT_DAY = intPreferencesKey("day")
    val CURRENT_DAY_NUMBER = intPreferencesKey("day_number")

}

class DataStoreProvider(
    private val dataStore: DataStore<androidx.datastore.preferences.core.Preferences>,
    context: Context
) {
    val dataPreferenceFlows : Flow<AppPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val currentMonth = preferences[PreferenceKeys.CURRENT_MONTH]?: 0
            val currentMonthNumber = preferences[PreferenceKeys.CURRENT_MONTH_NUMBER]?: 0
            val currentDay = preferences[PreferenceKeys.CURRENT_DAY]?: 0
            val currentDayNumber = preferences[PreferenceKeys.CURRENT_DAY_NUMBER]?: 0

            AppPreferences(
                currentMonth = currentMonth,
                currentMonthNumber = currentMonthNumber,
                currentDay = currentDay,
                currentDayNumber = currentDayNumber,
            )
        }
    suspend fun updateCurrentMonth(currentMonth: Int) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.CURRENT_MONTH] = currentMonth
        }
    }
    suspend fun updateCurrentMonthNumber(currentMonthNumber: Int) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.CURRENT_MONTH_NUMBER] = currentMonthNumber
        }
    }
    suspend fun updateCurrentDay(currentDay: Int) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.CURRENT_DAY] = currentDay
        }
    }
    suspend fun updateCurrentDayNumber(currentDayNumber: Int) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.CURRENT_DAY_NUMBER] = currentDayNumber
        }
    }
}