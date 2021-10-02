package com.castprogramms.elegion.ui.registration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.castprogramms.elegion.MainActivity
import com.castprogramms.elegion.R
import com.castprogramms.elegion.databinding.ActivityRegistrationBinding

class RegistrationActivity: AppCompatActivity() {
    companion object {
        val SIGN_IN_CODE = 7
    }
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
     fun goToMain(){
         startActivity(Intent(this, MainActivity::class.java))
         finish()
     }
}