package com.castprogramms.elegion.ui.calendar

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.castprogramms.elegion.R
import com.castprogramms.elegion.databinding.FragmentCalendarBinding
import com.castprogramms.elegion.ui.calendar.bottom_sheet.EventBottomSheetFragment
import com.shuhart.materialcalendarview.CalendarDay
import com.shuhart.materialcalendarview.DayView
import com.shuhart.materialcalendarview.MaterialCalendarView
import com.shuhart.materialcalendarview.OnDateSelectedListener

class CalendarFragment : Fragment(R.layout.fragment_calendar) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCalendarBinding.bind(view)
        binding.calendarView.setCurrentDate(binding.calendarView.currentDate)
        binding.calendarView.children.iterator().forEachRemaining {
            if (it is DayView)
                if (it.date == binding.calendarView.currentDate)
                    it.setBackgroundColor(Color.RED)
        }
        binding.calendarView.addOnDateChangedListener(object : OnDateSelectedListener {
            override fun onDateSelected(
                widget: MaterialCalendarView,
                date: CalendarDay,
                selected: Boolean
            ) {
                EventBottomSheetFragment().show(
                    requireActivity().supportFragmentManager,
                    EventBottomSheetFragment::class.toString()
                )
            }
        })
    }
}