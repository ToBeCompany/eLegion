package com.castprogramms.elegion.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.castprogramms.elegion.R
import com.castprogramms.elegion.databinding.FragmentAuthenticationBinding
import com.castprogramms.elegion.repository.AuthenticationResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.concurrent.timerTask

class AuthenticationFragment : Fragment(R.layout.fragment_authentication) {
    private val viewModel: AuthenticationViewModel by viewModel()
    lateinit var binding: FragmentAuthenticationBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAuthenticationBinding.bind(view)

        binding.enter.setOnClickListener {
            signIn()
            binding.enter.doneLoadingAnimation(
                resources.getColor(R.color.white),
                resources.getDrawable(R.drawable.done).toBitmap()
            )
            binding.enter.startMorphAnimation()
        }

        viewModel.needRegistrationLiveData.observe(viewLifecycleOwner, {
            if (it == true) {
                setTimerToGoRegistr()
                Log.e("Test", "need")
            } else {
                if (it == false) {
                    Log.e("Test", "noNeed")
                }
            }
        })
    }

    private fun signIn() {
        try {
            startActivityForResult(
                Intent(
                    GoogleSignIn.getClient(
                        requireActivity(),
                        viewModel.gso
                    ).signInIntent
                ),
                viewModel.SIGN_IN_CODE
            )
        } catch (e: Exception) {
            Log.e("Test", e.message.toString())
        }
    }

    private fun setTimerToGoRegistr() {
        Timer().schedule(
            timerTask { binding.enter.post { findNavController().navigate(R.id.action_authenticationFragment_to_registrationFragment) } },
            250
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == viewModel.SIGN_IN_CODE) {
            GoogleSignIn.getSignedInAccountFromIntent(data).addOnCompleteListener {
                if (it.isSuccessful) {
                    viewModel.handleSignInResult(it)
                    binding.enter.revertAnimation {
                        binding.enter.text = "Успех"
                        binding.enter.isPressed = true
                        binding.enter.isClickable = false
                    }
                } else {
                    Log.e("Test", it.exception?.message.toString())
                }
            }
        }
    }
}