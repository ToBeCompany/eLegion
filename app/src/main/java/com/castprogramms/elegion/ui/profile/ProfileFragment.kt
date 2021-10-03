package com.castprogramms.elegion.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.castprogramms.elegion.R
import com.castprogramms.elegion.databinding.ProfileFragmentBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.shuhart.materialcalendarview.MaterialCalendarView
import org.koin.androidx.viewmodel.ext.android.viewModel
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal

class ProfileFragment : Fragment(R.layout.profile_fragment) {
    private val viewModel: ProfileViewModel by viewModel()
    lateinit var binding: ProfileFragmentBinding

    private val checkListHelp by lazy {
        MaterialTapTargetPrompt.Builder(this)
            .setTarget(R.id.checkList)
            .setPrimaryText("")
            .setPromptBackground(RectanglePromptBackground())
            .setPromptFocal(RectanglePromptFocal())
            .setPromptStateChangeListener { _, state ->
                if (state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                    planListHelp?.show()
                    binding.messageText.text = "Твой личный план развития, чтобы не сбиться с пути"
                }
            }
            .create()
    }

    private val planListHelp by lazy {
        counterClick = 2
        MaterialTapTargetPrompt.Builder(this)
            .setTarget(R.id.plan)
            .setPrimaryText("")
            .setPromptBackground(RectanglePromptBackground())
            .setPromptFocal(RectanglePromptFocal())
            .setPromptStateChangeListener { _, state ->
                if (state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                    binding.messageText.text = "Добро пожаловать, Легионер!"
                }
            }
            .create()
    }
    var counterClick = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProfileFragmentBinding.bind(view)
        viewModel.getUser()?.let {
            binding.userName.text = it.name
            binding.userPost.text = it.userType.nameType
        }

        GoogleSignIn.getLastSignedInAccount(requireContext())?.let {
            Glide.with(binding.profileImage)
                .load(it.photoUrl)
                .error(R.drawable.profile)
                .into(binding.profileImage)
        }

        binding.checkList.setOnClickListener {
            findNavController().navigate(R.id.action_item_profile_to_checkFragment2)
        }

        binding.plan.setOnClickListener {
            findNavController().navigate(R.id.action_item_profile_to_planFragment)
        }

        binding.arrow.setOnClickListener {
            show()
            counterClick++
        }
    }
    private fun show(){
        when(counterClick){
            0 -> {
                checkListHelp?.show()
                binding.messageText.text = "Здесь твой чек-лист на неделю"
            }
            1 -> {
                checkListHelp?.dismiss()
                planListHelp?.show()
                binding.messageText.text = "Твой личный план развития, чтобы не сбиться с пути"
            }
            else ->{
                binding.messageText.text = "Добро пожаловать, Легионер!"
            }
        }
    }
}