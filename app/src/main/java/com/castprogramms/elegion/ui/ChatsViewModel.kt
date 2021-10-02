package com.castprogramms.elegion.ui

import androidx.lifecycle.ViewModel
import com.castprogramms.elegion.repository.AddressRepository
import com.castprogramms.elegion.data.TelegramAddress

class ChatsViewModel(
        private val addressRepository: AddressRepository
) : ViewModel() {

    fun getChatByIndex(index : Int) = addressRepository.getAddress(index)

    fun loadAllPosts() = addressRepository.loadAllChats()

    fun addPost(address: TelegramAddress) = addressRepository.addAddress(address)
}