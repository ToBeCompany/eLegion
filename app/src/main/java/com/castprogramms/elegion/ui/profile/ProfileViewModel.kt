package com.castprogramms.elegion.ui.profile

import androidx.lifecycle.ViewModel
import com.castprogramms.elegion.repository.UserRepository

class ProfileViewModel(
    private val userRepository: UserRepository
    ) : ViewModel() {
    fun getUser() = userRepository.currentUser
}