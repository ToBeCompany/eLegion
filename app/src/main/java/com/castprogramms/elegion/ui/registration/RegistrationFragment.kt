package com.castprogramms.elegion.ui.registration

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.castprogramms.elegion.R
import com.castprogramms.elegion.ui.registration.RegistrationActivity
import com.castprogramms.elegion.data.UserType
import com.castprogramms.elegion.databinding.RegistrationFragmentBinding
import com.castprogramms.elegion.repository.Resource
import com.castprogramms.elegion.ui.authentication.AuthenticationViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.type.Date
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.concurrent.timerTask

class RegistrationFragment : Fragment(R.layout.registration_fragment) {

    private val viewModel: RegistrationViewModel by viewModel()
    private val authViewModel: AuthenticationViewModel by sharedViewModel()
    private lateinit var binding: RegistrationFragmentBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = RegistrationFragmentBinding.bind(view)
        binding.userTextContainer.setBoxStrokeColorStateList(ColorStateList.valueOf(Color.WHITE))
        binding.userTextContainer.boxStrokeColor = Color.WHITE
        binding.atpv1.setFillColor(true)
        binding.atpv2.setFillColor(true)
        binding.userType.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                UserType.values().map { it.nameType }
            )
        )
        binding.userType.setOnItemClickListener { adapterView, view, i, l ->
            viewModel.setUserType(
                UserType.values()[i]
            )
        }

        //TODO REALIZE VALIDATE
        binding.userNameText.addTextChangedListener { viewModel.userNameValidate(it.toString()) }
        binding.telegramText.addTextChangedListener { viewModel.setTelegram(it.toString()) }
        binding.datePicker.setOnClickListener {
            createDatePicker {
                viewModel.setBirthday(it)
                binding.date.text =
                    DateFormat.format("dd.MM.yyyy", it).toString()
            }.show(childFragmentManager, "tag")
        }

        binding.doneButton.setOnClickListener {
            binding.doneButton.doneLoadingAnimation(
                resources.getColor(R.color.white),
                resources.getDrawable(R.drawable.done).toBitmap()
            )

            authViewModel.account?.let { account ->
                lifecycle.coroutineScope.launch {
                    viewModel.createUser(account.id).collectLatest {
                        when (it) {
                            is Resource.Error -> {
                                binding.doneButton.revertAnimation {
                                    binding.doneButton.text = "Ошибка"
                                    binding.doneButton.isPressed = true
                                    binding.doneButton.isClickable = false
                                }
                            }
                            is Resource.Loading -> {
                                binding.doneButton.startMorphAnimation()
                            }
                            is Resource.Success -> {
                                binding.doneButton.revertAnimation {
                                    binding.doneButton.text = "Успех"
                                    binding.doneButton.isPressed = true
                                    binding.doneButton.isClickable = false
                                }
                                val user = async { authViewModel.getUser(account.id) }
                                if (user.await() != null) {
                                    authViewModel.auth(user.await()!!)
                                    setTimerToGoMain()
                                }
                            }
                        }
                    }
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setTimerToGoMain() {
        Timer().schedule(
            timerTask { binding.doneButton.post { (requireActivity() as RegistrationActivity).goToMain() } },
            250
        )
    }

    private fun createDatePicker(date: (millis: Long) -> Unit): MaterialDatePicker<Long> {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выбор даты рождения")
                .build()
        datePicker.addOnPositiveButtonClickListener {
            date(it)
        }
        return datePicker
    }
}