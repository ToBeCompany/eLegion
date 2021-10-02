package com.castprogramms.elegion.ui.calendar

import androidx.lifecycle.ViewModel
import com.castprogramms.elegion.repository.CalendarRepository

class CalendarViewModel(
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    fun loadEvents() = calendarRepository.loadAllEvents()

}