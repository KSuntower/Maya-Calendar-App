package com.example.mayacalendarapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Calendar
import kotlin.reflect.jvm.internal.impl.types.model.TypeVariance


data class CalendarData(
    val currentMonth: Int,
    val currentMonthNumber: Int,
    val currentDay: Int,
    val currentDayNumber: Int,
    val workingTime : Int,
    val daySaved: Int,
)

class CalendarDataRepository(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        @Volatile
        private var instance : CalendarDataRepository? = null
        fun getInstance(
            dataStore: DataStore<Preferences>,
        ) =
            instance ?: synchronized(this) {
                instance ?: CalendarDataRepository(dataStore).also {
                    instance = it
                }
            }
    }
    object CalendarKeys {
        val CURRENT_MONTH = intPreferencesKey("month")
        val CURRENT_MONTH_NUMBER = intPreferencesKey("month_number")
        val CURRENT_DAY = intPreferencesKey("day")
        val CURRENT_DAY_NUMBER = intPreferencesKey("day_number")
        val CURRENT_WORKING_TIME = intPreferencesKey("working_time")
        val DAY_SAVED = intPreferencesKey("day_saved")
    }

    val calendarValuesFlows : Flow<CalendarData> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapCalendarValues(preferences)
        }

    suspend fun fetchInitialValues() =
        mapCalendarValues(dataStore.data.first().toPreferences())

    private fun mapCalendarValues(preferences : Preferences) : CalendarData {
        val currentMonth = preferences[CalendarKeys.CURRENT_MONTH]?: 0
        val currentMonthNumber = preferences[CalendarKeys.CURRENT_MONTH_NUMBER]?: 0
        val currentDay = preferences[CalendarKeys.CURRENT_DAY]?: 0
        val currentDayNumber = preferences[CalendarKeys.CURRENT_DAY_NUMBER]?: 0
        val currentWorkingNumber = preferences[CalendarKeys.CURRENT_WORKING_TIME]?: 0
        val daySaved = preferences[CalendarKeys.DAY_SAVED] ?: Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        return CalendarData(
            currentMonth = currentMonth,
            currentMonthNumber = currentMonthNumber,
            currentDay = currentDay,
            currentDayNumber = currentDayNumber,
            workingTime = currentWorkingNumber,
            daySaved = daySaved
        )
    }

    suspend fun updateCurrentMonth(currentMonth: Int) {
        dataStore.edit { preferences ->
            preferences[CalendarKeys.CURRENT_MONTH] = currentMonth
        }
    }
    suspend fun updateCurrentMonthNumber(currentMonthNumber: Int) {
        dataStore.edit { preferences ->
            preferences[CalendarKeys.CURRENT_MONTH_NUMBER] = currentMonthNumber
        }
    }
    suspend fun updateCurrentDay(currentDay: Int) {
        dataStore.edit { preferences ->
            preferences[CalendarKeys.CURRENT_DAY] = currentDay
        }
    }
    suspend fun updateCurrentDayNumber(currentDayNumber: Int) {
        dataStore.edit { preferences ->
            preferences[CalendarKeys.CURRENT_DAY_NUMBER] = currentDayNumber
        }
    }
    suspend fun updateWorkingTime(workingTime: Int) {
        dataStore.edit { preferences ->
            preferences[CalendarKeys.CURRENT_WORKING_TIME] = workingTime
        }
    }
    suspend fun updateDaySaved(day : Int) {
        dataStore.edit { preferences ->
            preferences[CalendarKeys.DAY_SAVED] = day
        }
    }
}