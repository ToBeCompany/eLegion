package com.castprogramms.elegion.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castprogramms.elegion.data.User
import com.castprogramms.elegion.data.UserType
import com.castprogramms.elegion.interactors.StartInteractor
import com.castprogramms.elegion.repository.Resource
import com.castprogramms.elegion.repository.UserRepository
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

class RegistrationViewModel(
    private val userRepository: UserRepository,
    private val startInteractor: StartInteractor
) : ViewModel() {

    private val userName = MutableStateFlow("")
    private val birthday = MutableStateFlow(0L)
    private val telegram = MutableStateFlow("")
    private val type = MutableStateFlow(UserType.BEGINNER)

    fun userNameValidate(name: String) {
        userName.value = name
    }
    fun setBirthday(it: Long) {
        birthday.value = it
    }
    fun setUserType(userType: UserType) {
        type.value = userType
    }
    fun setTelegram(address : String) {
        telegram.value = address
    }
    suspend fun loadStartData(){
        userRepository.currentUser?.let { startInteractor.setupData(it) }
    }
    fun createUser(id : String): Flow<Resource<DocumentReference>> {
        val user = User(
            id,
            userName.value,
            type.value,
            birthday.value.toString(),
            telegram.value
        )
//        userRepository.auth(user)
        return userRepository.createUser(user)
    }
}