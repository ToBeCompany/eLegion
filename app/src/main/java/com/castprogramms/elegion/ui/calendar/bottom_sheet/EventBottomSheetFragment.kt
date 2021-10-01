package com.castprogramms.elegion.ui.calendar.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.castprogramms.elegion.R
import com.castprogramms.elegion.databinding.FragmentEventBottomSheetBinding
import com.castprogramms.elegion.ui.calendar.EventAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EventBottomSheetFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_event_bottom_sheet, container, false)
        val binding = FragmentEventBottomSheetBinding.bind(view)
        binding.recycler.adapter = EventAdapter()
        return view
    }
}