package com.castprogramms.elegion

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.castprogramms.elegion.databinding.ActivityRegistrationBinding

class RegistrationActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_registration)?.findNavController()
        navController?.let {
            val appBarConfiguration = AppBarConfiguration(it.graph)
            setupActionBarWithNavController(it, appBarConfiguration)
        }
    }
     fun goToMain(){
         startActivity(Intent(this, MainActivity::class.java))
         finish()
     }
}