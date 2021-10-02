package com.castprogramms.elegion.ui.authentication

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.castprogramms.elegion.ELegionerApplication
import com.castprogramms.elegion.repository.AuthenticationRepository
import com.castprogramms.elegion.repository.AuthenticationResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class AuthenticationViewModel(
    private val repository: AuthenticationRepository,
    application: Application
) : AndroidViewModel(application) {
    val SIGN_IN_CODE = 7
    val needRegistrationLiveData = MutableLiveData<Boolean>(null)

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail().build()

    lateinit var googleSignInClient: GoogleSignInClient
    var account: GoogleSignInAccount? = null

    init {
        initGoogleSign()
    }

    private fun initGoogleSign() {
        googleSignInClient = GoogleSignIn.getClient(
            (this.getApplication() as ELegionerApplication).applicationContext,
            gso
        )
    }

    fun signOut() {
        googleSignInClient.signOut()//.addOnSuccessListener {   }
    }

    fun getUser() = repository.user

    fun handleSignInResult(task: Task<GoogleSignInAccount>): AuthenticationResult {
        return try {
            account = task.getResult(ApiException::class.java)
            Log.e("Test", "almost win")
            repository.loadUserData(account) { needRegistrationLiveData.postValue(it) }
        } catch (e: ApiException) {
            repository.loadUserData(null) { needRegistrationLiveData.postValue(it) }
        }
    }
}