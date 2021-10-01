package com.castprogramms.elegion

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.castprogramms.elegion.data.TelegramAddress
import com.castprogramms.elegion.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val ta = TelegramAddress("Arbonik", "https://t.me/arbonik")
        FirebaseReference.addTelegramAddress(ta, db)
        binding.button.setOnClickListener {

        }

    }
    private fun openUri(uri : String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)
    }
}