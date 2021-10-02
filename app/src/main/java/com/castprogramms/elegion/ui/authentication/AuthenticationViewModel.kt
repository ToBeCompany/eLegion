package com.castprogramms.elegion.ui.authentication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.castprogramms.elegion.ELegionerApplication
import com.castprogramms.elegion.repository.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthenticationViewModel(
    private val userRepository: UserRepository,
    application: Application
) : AndroidViewModel(application) {

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
        googleSignInClient.signOut()
        userRepository.singOut()
    }

    fun handleSignInResult(task: Task<GoogleSignInAccount>) = flow {
        account = task.getResult(ApiException::class.java)
        emit(loadUserData(account))
    }.catch {
        emit(false)
    }.flowOn(Dispatchers.IO)

    private suspend fun loadUserData(account: GoogleSignInAccount?) =
        (account != null && userRepository.hasUser(account.id) == null)
}