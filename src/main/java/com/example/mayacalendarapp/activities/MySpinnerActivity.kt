package com.example.mayacalendarapp.activities

import android.app.Activity
import android.view.View
import android.widget.AdapterView
import com.example.mayacalendarapp.Tzolkin

class MySpinnerActivity: Activity(), AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        Tzolkin.getInstance().setDateOnIndex(1, pos)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}