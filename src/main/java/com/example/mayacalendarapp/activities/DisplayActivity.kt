package com.example.mayacalendarapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mayacalendarapp.R

class DisplayActivity : AppCompatActivity() {
//    val tzolkinPreferences = TzolkinModel(DataStoreProvider())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)
    }
}
