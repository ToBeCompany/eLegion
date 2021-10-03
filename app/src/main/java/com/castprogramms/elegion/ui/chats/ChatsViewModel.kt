package com.castprogramms.elegion.ui.chats

import androidx.lifecycle.ViewModel
import com.castprogramms.elegion.data.TelegramAddress
import com.castprogramms.elegion.repository.AddressRepository

class ChatsViewModel(
        private val addressRepository: AddressRepository
) : ViewModel() {
    fun loadAllPosts() = addressRepository.loadAllChats()

    fun addPost(address: TelegramAddress) = addressRepository.addAddress(address)
}