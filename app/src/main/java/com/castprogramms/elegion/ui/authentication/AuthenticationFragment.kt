package com.castprogramms.elegion.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.castprogramms.elegion.R
import com.castprogramms.elegion.databinding.FragmentAuthenticationBinding
import com.castprogramms.elegion.ui.registration.RegistrationActivity
import com.castprogramms.elegion.ui.registration.RegistrationActivity.Companion.SIGN_IN_CODE
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*
import kotlin.concurrent.timerTask

class AuthenticationFragment : Fragment(R.layout.fragment_authentication) {

    private val viewModel: AuthenticationViewModel by sharedViewModel()
    lateinit var binding: FragmentAuthenticationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAuthenticationBinding.bind(view)
        binding.atpv1.setFillColor(true)
        binding.atpv2.setFillColor(true)

        binding.enter.setOnClickListener {
            signIn()
            binding.enter.doneLoadingAnimation(
                resources.getColor(R.color.white),
                resources.getDrawable(R.drawable.done).toBitmap()
            )
            binding.enter.startMorphAnimation()
        }
    }

    private fun signIn() {
        startActivityForResult(
            Intent(
                GoogleSignIn.getClient(
                    requireActivity(),
                    viewModel.gso
                ).signInIntent
            ),
            SIGN_IN_CODE
        )
    }

    private fun setTimerToGoRegistr() {
        Timer().schedule(
            timerTask { binding.enter.post { findNavController().navigate(R.id.action_authenticationFragment_to_registrationFragment) } },
            250
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_CODE) {
            GoogleSignIn.getSignedInAccountFromIntent(data).addOnCompleteListener {
                if (it.isSuccessful) {
                    lifecycle.coroutineScope.launch {
                        val hasUser = async { viewModel.hasThisUser(it.result.id)}.await()
                        if (hasUser == null){
                            viewModel.account = it.result
                            setTimerToGoRegistr()
                        } else {
                            (requireActivity() as RegistrationActivity).goToMain()
                        }
                    }
                    binding.enter.revertAnimation {
                        binding.enter.text = "Успех"
                        binding.enter.isPressed = true
                        binding.enter.isClickable = false
                    }
                }
            }
        }
    }
}