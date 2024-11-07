package com.example.mayacalendarapp.models

data class LongCountModel(
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
