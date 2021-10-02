package com.castprogramms.elegion.ui.registration

import androidx.lifecycle.ViewModel
import com.castprogramms.elegion.data.User
import com.castprogramms.elegion.data.UserType
import com.castprogramms.elegion.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

class RegistrationViewModel(
    private val userRepository: UserRepository
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

    fun createUser() {
        //TODO take uuid from google acc
        val user = User(
            UUID.randomUUID().toString(),
            userName.value,
            type.value,
            birthday.value.toString(),
            telegram.value
        )
        userRepository.createUser(user)
    }
}