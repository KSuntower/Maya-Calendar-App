package com.example.mayacalendarapp

private val dayNames : List<String> = listOf("Ajaw", "Imix", "Ik'", "Ak'bal", "Kan", "Chikchan", "Kimil", "Manik"
    , "Lamat", "Muluk", "Ok", "Chuwen", "Eb", "Ben", "Ix", "Men", "Kib"
    , "Kaban", "Ets'nab", "Kawak")

private val monthNames : List<String> = listOf("Pop", "Wo", "Sip", "Sots'", "Tsek", "Xul", "YaxKin", "Mol"
    , "Ch'en", "Yaax", "sak", "Kej", "Mak", "Kankin", "Muwan", "Pax"
    , "Kayab", "K'umk'u", "Wayeb")

private val colors : List<String> = listOf("Chak", "Sak", "Boox", "K'an")
private val directions: List<String> = listOf("Lak'in", "Xaman", "Chik'in", "Xaman")

data class LongCount(
    var baktun : Int = 0,
    var katun : Int = 0,
    var tun : Int = 0,
    var winal : Int = 0,
    var kin : Int = 0
) {
    override fun toString(): String {
        return "$baktun.$katun.$tun.$winal.$kin"
    }
}

data class CalendarRound(
    var dayNumber: Int = 0,
    var dayName: Int = 0,
    var monthNumber: Int = 0,
    var monthName: Int = 0,
    var yearBearerNumber : Int = 0,
    var yearBearerName : Int = 0,
    var index : Int = 0
) {
    override fun toString(): String {
        return "$dayNumber ${dayNames[dayName]} $monthNumber ${monthNames[monthName]}"
    }
    fun getYearBearer() : String {
        yearBearerNumber = (dayNumber + 1) % 13
        yearBearerName   = (dayName + 1) % 20
        index = (yearBearerName - 4) / 5 // f(x) = (x - 4) / 5 where x is {4, 9, 14, 19}

        return "$yearBearerNumber ${dayNames[yearBearerName]}"
    }
    fun getDay() : String {
        return "$dayNumber ${dayNames[dayName]}"
    }
    fun getMonth() : String {
        return "$monthNumber ${monthNames[monthName]}"
    }
    fun getColor() : String {
        return colors[index]
    }
    fun getDirection() : String {
        return directions[index]
    }
}
