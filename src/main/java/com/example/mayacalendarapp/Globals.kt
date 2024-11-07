package com.example.mayacalendarapp

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

public val DAY_NAMES : List<String> = listOf("Ajaw", "Imix", "Ik'", "Ak'bal", "Kan", "Chikchan", "Kimil", "Manik"
    , "Lamat", "Muluk", "Ok", "Chuwen", "Eb", "Ben", "Ix", "Men", "Kib"
    , "Kaban", "Ets'nab", "Kawak")

public val MONTH_NAMES : List<String> = listOf("Pop", "Wo", "Sip", "Sots'", "Tsek", "Xul", "YaxKin", "Mol"
    , "Ch'en", "Yaax", "sak", "Kej", "Mak", "Kankin", "Muwan", "Pax"
    , "Kayab", "K'umk'u", "Wayeb")

public val LUCK_DAYS : List<Int> = listOf(1, 0, -1, 1, -1, 0, 1, 1, 0, -1, -1, 1, 0, 0, -1, 0, 1, -1, -1, 0)

public val COLORS : List<String> = listOf("Chak", "Sak", "Boox", "K'an")
public val DIRECTIONS : List<String> = listOf("Lak'in", "Xaman", "Chik'in", "Xaman")

const val USER_PREFERENCES_NAME = "app_settings"
val Context.dataStore  by preferencesDataStore(name = USER_PREFERENCES_NAME,)
