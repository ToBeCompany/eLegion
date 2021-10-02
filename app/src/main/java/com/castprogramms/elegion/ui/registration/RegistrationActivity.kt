package com.castprogramms.elegion.ui.registration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.castprogramms.elegion.MainActivity
import com.castprogramms.elegion.databinding.ActivityRegistrationBinding
import com.castprogramms.elegion.ui.authentication.AuthenticationViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationActivity : AppCompatActivity() {
    companion object {
        val SIGN_IN_CODE = 7
    }

    private lateinit var binding: ActivityRegistrationBinding

    private val viewModel: AuthenticationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.auth.addOnCompleteListener {
            if (it.isSuccessful) {
                if (viewModel.isAuth) {
                    lifecycle.coroutineScope.launch {
                        val user = async { viewModel.getUser(it.result.id) }
                        viewModel.auth(user.await())
                        goToMain()
                    }
                }
            }
        }
    }

    fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}