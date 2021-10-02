package com.castprogramms.elegion.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.castprogramms.elegion.R
import com.castprogramms.elegion.ui.registration.RegistrationActivity
import com.castprogramms.elegion.data.UserType
import com.castprogramms.elegion.databinding.RegistrationFragmentBinding
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment : Fragment(R.layout.registration_fragment) {

    private val viewModel: RegistrationViewModel by viewModel()
    private lateinit var binding: RegistrationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegistrationFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.userType.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                UserType.values()
            )
        )
        //TODO REALIZE VALIDATE
        binding.userNameText.addTextChangedListener { viewModel.userNameValidate(it.toString()) }
        binding.telegramText.addTextChangedListener { viewModel.userNameValidate(it.toString()) }
        binding.datePicker.setOnClickListener {
            createDatePicker {
                viewModel.setBirthday(it)
            }.show(childFragmentManager, "tag")
        }

        binding.doneButton.setOnClickListener {
            viewModel.createUser()
            (requireActivity() as RegistrationActivity).goToMain()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun createDatePicker(date: (millis: Long) -> Unit): MaterialDatePicker<Long> {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()
        datePicker.addOnPositiveButtonClickListener {
            date(it)
        }
        return datePicker
    }
}