package com.castprogramms.elegion.ui.authentication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.castprogramms.elegion.ELegionaryApplication
import com.castprogramms.elegion.data.User
import com.castprogramms.elegion.repository.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthenticationViewModel(
    private val userRepository: UserRepository,
    application: Application
) : AndroidViewModel(application) {

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail().build()

    val googleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(
            (this.getApplication() as ELegionaryApplication).applicationContext,
            gso
        )
    }

    var account: GoogleSignInAccount? = null

    fun signOut() {
        googleSignInClient.signOut()
        userRepository.singOut()
    }

    suspend fun getUser(userId: String) = withContext(Dispatchers.IO) {
        userRepository.getUser(userId)
    }

    fun auth(user: User) {
        userRepository.auth(user)
    }

    suspend fun hasThisUser(id: String) = userRepository.hasUser(id)
}