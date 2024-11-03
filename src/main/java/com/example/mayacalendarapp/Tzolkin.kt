package com.example.mayacalendarapp

import android.icu.util.Calendar
import android.util.Log
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

class Tzolkin private constructor() {
    companion object {
        @Volatile
        private var instance : Tzolkin? = null
         fun getInstance() =
             instance ?: synchronized(this) {
                 instance ?: Tzolkin().also { instance = it }
             }
    }
    private var day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        set(value) {
            field = if (value in 1..31) {
                value
            } else {
                value
            }
        }
    private var month = Calendar.getInstance().get(Calendar.MONTH)
        set(value) {
            field = if (value in 1..12) {
                value
            } else {
                value
            }
        }
    private var year = Calendar.getInstance().get(Calendar.YEAR)
        set(value) {
            field = if (value > 0) {
                value
            } else {
                value
            }
        }
    private var workingTime = 0
    private val longCountStruct: LongCount = LongCount()
    private val yearBearerCR : CalendarRound = CalendarRound()
    private var daysPassedInCurrentYear = 0
    private val calendarRound: CalendarRound = CalendarRound()
    private var lordOfTheNight : Int = 1

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

    private fun currentDateToDays() {
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
        this.daysPassedInCurrentYear = daysAccumulated
        Log.i("currentDateToDays", "days passed since current year: $daysAccumulated")

        for (i in jdn.indices) {
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

        this.workingTime = days
    }

    private fun workingTimeToMaya() {
        val a: Int = Math.floorMod(this.workingTime, BAKTUN)
        val b: Int = Math.floorMod(a, KATUN)
        val c: Int = Math.floorMod(b, TUN)
        val d: Int = Math.floorMod(c, WINAL)

        longCountStruct.baktun = floor(this.workingTime.toDouble()/BAKTUN).toInt()
        longCountStruct.katun= floor(a.toDouble()/KATUN).toInt()
        longCountStruct.tun = floor(b.toDouble()/TUN).toInt()
        longCountStruct.winal = floor(c.toDouble()/WINAL).toInt()
        longCountStruct.kin = d
    }

    fun setDateOnIndex(index : Int, value : Int) {
        when (index) {
            0 -> {
                this.day = value
            }
            1 -> {
                this.month = value
            }
            2 -> {
                this.year = value
            }
        }
    }

    private fun workingTimeToCalendarRound(wt : Int = this.workingTime,
                                           retVal : CalendarRound = this.calendarRound) {
        var workingNumber: Int = wt + 4

        retVal.dayNumber= Math.floorMod(Math.floorMod(workingNumber, 13), 13)
        retVal.dayName= Math.floorMod(Math.floorMod(wt, 20), 20)

        workingNumber = Math.floorMod(Math.floorMod(wt - 17, 365), 365)

        retVal.monthNumber = Math.floorMod(Math.floorMod(workingNumber, WINAL), WINAL)
        retVal.monthName = workingNumber / WINAL
        return
    }

    fun calculate() {
        currentDateToDays()
        workingTimeToMaya()
        workingTimeToCalendarRound()
        if (true) { // TODO: only compare when the new date is more or less than 365 days
            val starOfYear : Int =  ((calendarRound.monthNumber + (calendarRound.monthName * 20)) -1)
            workingTimeToCalendarRound(this.workingTime - starOfYear, this.yearBearerCR) // year bearer
        }
        this.lordOfTheNight = (this.workingTime % TOTAL_LOtD_COUNT)
        return
    }

    fun getMayaTimeIndex(index : Int) : String {
        return when (index){
            0 -> getLongCount()
            1 -> getCalendarRound()
            2 -> getYearBearer()
            3 -> getLordOfTheNight()
            4 -> getColorAndDirection()
            else -> ""
        }
    }

    fun getLongCount() : String {
        return longCountStruct.toString()
    }
    fun getCalendarRound() : String {
        return calendarRound.toString()
    }
    fun getCalendarDay() : String {
        return calendarRound.getDay()
    }
    fun getCalendarMonth() : String {
        return calendarRound.getMonth()
    }
    fun getLordOfTheNight() : String {
        return "G$lordOfTheNight"
    }
    fun getYearBearer() : String {
        return yearBearerCR.getYearBearer()
    }
    fun getColorAndDirection() : String {
        return "${yearBearerCR.getColor()} ${yearBearerCR.getDirection()}"
    }
}