package com.example.mayacalendarapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mayacalendarapp.data.CalendarDataRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class DisplayViewModel(
    private val calendarDataRepository: CalendarDataRepository
) : ViewModel() {
    private val calendarValuesFlow = calendarDataRepository.calendarValuesFlows
}
class DisplayViewModelFactory(
    private val calendarDataRepository: CalendarDataRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DisplayViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DisplayViewModel(calendarDataRepository) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}