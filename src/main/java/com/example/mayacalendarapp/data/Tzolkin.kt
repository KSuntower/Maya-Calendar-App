package com.example.mayacalendarapp.data

import android.util.Log
import com.example.mayacalendarapp.DAY_NAMES
import com.example.mayacalendarapp.models.CalendarRoundModel
import com.example.mayacalendarapp.models.LongCountModel
import kotlinx.coroutines.*
import java.util.Calendar
import kotlin.math.floor

private const val GMT_CORRELATION : Int = 584283
private const val BAKTUN : Int = 144000
private const val KATUN : Int = 7200
private const val TUN : Int= 360
private const val WINAL : Int = 20
private const val TOTAL_LOtD_COUNT : Int = 9

class Tzolkin(private val day : Int, private val month : Int, private val year : Int) {
    constructor(dataStoreProvider : CalendarDataRepository) : this(
        Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
        Calendar.getInstance().get(Calendar.MONTH       ),
        Calendar.getInstance().get(Calendar.YEAR        ),
    ) {
        runBlocking {
            getCalendarValues(dataStoreProvider)
        }
    }

    private suspend fun getCalendarValues(dataStoreProvider: CalendarDataRepository) {
        val preferences = dataStoreProvider.fetchInitialValues()
        val isInitialSetup = preferences.workingTime
        if (isInitialSetup == 0) {
            this.runAllCalculations(true)
            return
        }

        val daysPassedSinceUpdate : Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - preferences.daySaved
        if (daysPassedSinceUpdate > 0) {
            this.increaseDateBy(daysPassedSinceUpdate)
            return
        }

        this.calendarRound.dayNumber = preferences.currentDayNumber
        this.calendarRound.dayName = preferences.currentDay

        this.calendarRound.monthNumber = preferences.currentMonthNumber
        this.calendarRound.monthName = preferences.currentMonth

    }

    private fun increaseDateBy(daysPassedSinceUpdate: Int) {
        // TODO: fix this.
        for (i in 0..(CalendarRoundModel::class.members).size) {
            calendarRound.indexToCalendar(i, daysPassedSinceUpdate, 1)
        }
    }

    private fun isLeapYear(year : Int) : Int {
        if (year % 400 == 0) {
            return 1
        } else if (year % 100 == 0) {
            return 0
        } else if (year % 4 == 0) {
            return 1
        }
        return 0
    }

    /* TODO: fix 'magic numbers' in function below */
    private fun calculateCalendarRound(doYearBearer : Boolean) {
        var workingNumber: Int = this.workingTime + 4

        this.calendarRound.dayNumber= Math.floorMod(Math.floorMod(workingNumber, 13), 13)
        this.calendarRound.dayName= Math.floorMod(Math.floorMod(this.workingTime, 20), 20)

        workingNumber = Math.floorMod(Math.floorMod(this.workingTime - 17, 365), 365)

        this.calendarRound.monthNumber = Math.floorMod(Math.floorMod(workingNumber, WINAL), WINAL)
        this.calendarRound.monthName = workingNumber / WINAL
        if (doYearBearer) {
            this.calendarRound.getYearBearer()
        }
    }

    private fun calculateLongCount() {
        val a: Int = Math.floorMod(this.workingTime, BAKTUN)
        val b: Int = Math.floorMod(a, KATUN)
        val c: Int = Math.floorMod(b, TUN)
        val d: Int = Math.floorMod(c, WINAL)

        longCount.baktun = floor(this.workingTime.toDouble()/ BAKTUN).toInt()
        longCount.katun= floor(a.toDouble()/ KATUN).toInt()
        longCount.tun = floor(b.toDouble()/ TUN).toInt()
        longCount.winal = floor(c.toDouble()/ WINAL).toInt()
        longCount.kin = d
    }

    private fun getWorkingTime() {
        val jdn : List<Int> = listOf<Int>(1721060, 1757585, 1794109, 1830633, 1867157, 1903682, 1940206, 1976730, 2013254,
            2049779, 2086303, 2122827, 2159351, 2195876, 2232400, 2268924, 2305448, 2341973, 2378497,
            2415021, 2451545)

        val daysInMonths : List<List<Int>> = listOf<List<Int>>(
            listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31),
            listOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31))

        var days : Int = 2451545
        var yearMinus : Int = 2000
        var daysAccumulated : Int = 0
        var leapDays : Int = 0

        Log.i("currentDateToDays", "${this.month}")

        for (i in 0..<this.month) {
            daysAccumulated += daysInMonths[0][i]
        }

        daysAccumulated += this.day
        Log.i("currentDateToDays", "days passed since current year: $daysAccumulated")

        for (i in jdn.indices) { // 2014 - 2000 = 14
            if ((i + 1) * 100 > this.year) {
                days = jdn[i]
                yearMinus = i * 100
                break
            }
        }

        Log.i("currentDateToDays", "years to subtract: $yearMinus")
        val yearOld : Int = yearMinus
        yearMinus = this.year - yearMinus

        for (i in yearOld..<this.year) {
            if (isLeapYear(i) == 1) {
                leapDays += 1
            }
        }
        val dayCount : Int = (yearMinus * 365) + daysAccumulated + leapDays

        Log.i("currentDateToDays", "days in $yearMinus years: $dayCount")

        days += dayCount - GMT_CORRELATION

        Log.i("currentDateToDays", "after GMT correlation: $days")

        // TODO: save here to datastore
        this.workingTime = days
    }

    // TODO: update DATASTORE
    fun runAllCalculations (doYearBearer: Boolean) {
        getWorkingTime()
        calculateLongCount()
        calculateCalendarRound(doYearBearer)
        this.lordOfTheNight = (this.workingTime % TOTAL_LOtD_COUNT)
    }

    fun getCalendarRound() : String = calendarRound.toString()
    fun getLongCount() : String = longCount.toString()

    private val calendarRound = CalendarRoundModel()
    private val longCount = LongCountModel()
    private var workingTime = 0
    private var lordOfTheNight : Int = 0
}