package com.example.mayacalendarapp.models

import com.example.mayacalendarapp.DAY_NAMES
import com.example.mayacalendarapp.MONTH_NAMES
import com.example.mayacalendarapp.COLORS
import com.example.mayacalendarapp.DIRECTIONS

data class CalendarRoundModel (
    var dayNumber: Int = 0,
    var dayName: Int = 0,
    var monthNumber: Int = 0,
    var monthName: Int = 0,
    var yearBearerNumber : Int = 0,
    var yearBearerName : Int = 0,
    private var index : Int = 0
) {
    override fun toString(): String {
        return "$dayNumber ${DAY_NAMES[dayName]} $monthNumber ${MONTH_NAMES[monthName]}"
    }

    fun indexToCalendar(index: Int, value : Int, add : Int) {
        when(index) {
            0 -> dayNumber = value + (add * dayNumber)
            1 -> dayName = value + (add * dayName)
            2 -> monthNumber = value + (add * monthNumber)
            3 -> monthName = value + (add * dayName)
            4 -> yearBearerNumber = value + (add * yearBearerNumber)
            5 -> yearBearerName = value + (add * yearBearerName)
        }
    }

    fun getYearBearer() : String {
        val startOfYear : Int = ((this.monthNumber + (this.monthName * MONTH_NAMES.size)) -1)
        yearBearerNumber = (dayNumber + startOfYear) % 13
        yearBearerName   = (dayName + startOfYear) % 20

        index = (yearBearerName - 4) / 5 // f(x) = (x - 4) / 5 where x is {4, 9, 14, 19}

        return "$yearBearerNumber ${DAY_NAMES[yearBearerName]}"
    }
    fun getDay() : String {
        return "$dayNumber ${DAY_NAMES[dayName]}"
    }
    fun getMonth() : String {
        return "$monthNumber ${MONTH_NAMES[monthName]}"
    }
    fun getColor() : String {
        return COLORS[index]
    }
    fun getDirection() : String {
        return DIRECTIONS[index]
    }
}
