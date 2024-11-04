package com.example.mayacalendarapp

import android.app.Activity
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.datastore.preferences.preferencesDataStore
import com.example.mayacalendarapp.providers.DataStoreProvider


private const val USER_PREFERENCES_NAME = "app_settings"
private val Context.dataStore  by preferencesDataStore(name = USER_PREFERENCES_NAME)

class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        Tzolkin.getInstance().setDateOnIndex(1, pos)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var submitButton : Button

    // We put this in a list to make it easier to instantiate each element.
    private val idOutputs = listOf(
        R.id.LongCount,
        R.id.CalendarRound,
        R.id.YearBearer,
        R.id.LordOfTheNight,
        R.id.ColorAndDirection
    )
    private var outputLabels = arrayOfNulls<TextView>(idOutputs.size)

    private val idInputs = listOf(
        R.id.DayInput,
        R.id.YearInput
    )
    private var inputTextObjects = arrayOfNulls<AppCompatEditText>(idInputs.size)
    private val initValues = listOf(Calendar.DAY_OF_MONTH, Calendar.YEAR)

    private val tzolkin = Tzolkin.getInstance()
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        DataStoreProvider(dataStore = dataStore, this)
        tzolkin.calculate()

        val monthInput : Spinner = findViewById(R.id.Spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.month_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            monthInput.adapter = adapter
            monthInput.setSelection(12 -Calendar.MONTH)
        }
        monthInput.onItemSelectedListener = SpinnerActivity()

        submitButton = findViewById(R.id.button)
        submitButton.setOnClickListener {
            tzolkin.calculate()
            outputLabels.forEachIndexed { index, label ->
                label?.text = tzolkin.getMayaTimeIndex(index)
            }
        }

        outputLabels.forEachIndexed { index, _ ->
            outputLabels[index] = findViewById(idOutputs[index])
            outputLabels[index]?.text = tzolkin.getMayaTimeIndex(index)
        }

        inputTextObjects.forEachIndexed { index, _ ->
            inputTextObjects[index] = findViewById(idInputs[index])
            inputTextObjects[index]?.setText(this.calendar.get(initValues[index]).toString())
            inputTextObjects[index]?.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    return@doOnTextChanged
                }
                tzolkin.setDateOnIndex(index, text.toString().toInt())
            }
        }
    }
}