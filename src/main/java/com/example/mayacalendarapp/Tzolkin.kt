package com.example.mayacalendarapp

import android.icu.util.Calendar
import android.util.Log
import com.example.mayacalendarapp.models.CalendarRoundModel
import com.example.mayacalendarapp.models.LongCountModel
import kotlin.math.floor

private const val GMT_CORRELATION : Int = 584283
private const val BAKTUN : Int = 144000
private const val KATUN : Int = 7200
private const val TUN : Int= 360
private const val WINAL : Int = 20
//private const val TOTAL_DAYS_TO_ADD : Int = 263
//private const val INITIAL_DAY_CALENDAR: Int = 4
//private const val INITIAL_NUMBER_CALENDAR: Int = 20
private const val TOTAL_LOtD_COUNT : Int = 9

