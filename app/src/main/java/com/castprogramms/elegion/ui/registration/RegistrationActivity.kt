package com.castprogramms.elegion.ui.registration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.castprogramms.elegion.MainActivity
import com.castprogramms.elegion.databinding.ActivityRegistrationBinding
import com.castprogramms.elegion.ui.authentication.AuthenticationViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationActivity : AppCompatActivity() {
    companion object {
        const val SIGN_IN_CODE = 7
    }

    private lateinit var binding: ActivityRegistrationBinding

    private val viewModel: AuthenticationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val googleAuth = GoogleSignIn.getLastSignedInAccount(this)
        if (googleAuth != null){
            lifecycle.coroutineScope.launch(Dispatchers.IO) {
                val user =  viewModel.getUser(googleAuth.id)
                if (user != null) {
                    user.let { viewModel.auth(it) }
                    goToMain()
                }
            }
        }
    }

    fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}