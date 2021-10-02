package com.castprogramms.elegion.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.castprogramms.elegion.data.User
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthenticationRepository(private val userRepository: UserRepository) {

    private val _userData = MutableLiveData<Resource<out User>>(null)

    val user: LiveData<Resource<out User>> = _userData

    fun loadUserData(account: GoogleSignInAccount?, isNeedRegist: (Boolean) -> Unit): AuthenticationResult {
        return if (account != null) {
            _userData.postValue(Resource.Loading())
            MainScope().launch(Dispatchers.IO) {
                userRepository.getUser(account.id.toString()).collectLatest {
                    when(it){
                        is Resource.Error -> {
                            _userData.postValue(Resource.Error(it.message.toString()))
                            isNeedRegist(true)
                        }
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            if (it.data != null) {
                                _userData.postValue(Resource.Success(it.data))
                                isNeedRegist(false)
                            }
                        }
                    }
                }
            }
            AuthenticationResult.UserExistYet()
        } else {
            isNeedRegist(true)
            AuthenticationResult.UserDontExist()
        }
    }
}