package com.castprogramms.elegion.ui.plan

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.castprogramms.elegion.R
import com.castprogramms.elegion.databinding.FragmentPlanBinding

class PlanFragment: Fragment(R.layout.fragment_plan) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentPlanBinding.bind(view)
        binding.pdfView.fromAsset("WB_E-LEGION 2021.pdf").load()
    }
}