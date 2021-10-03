package com.castprogramms.elegion.interactors

import com.castprogramms.elegion.data.TelegramAddress
import com.castprogramms.elegion.data.User
import com.castprogramms.elegion.repository.AddressRepository
import com.castprogramms.elegion.repository.TaskRepository

class StartInteractor(
    private val taskRepository: TaskRepository,
    private val chatsViewModel: AddressRepository
) {
    suspend fun setupData(user: User){
        taskRepository.loadStartedTasks(user.userId)
        chatsViewModel.createAddress(TelegramAddress(user.name, user.contact))
    }
}