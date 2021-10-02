package com.castprogramms.elegion.ui.checklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castprogramms.elegion.data.CheckItem
import com.castprogramms.elegion.repository.Resource
import com.castprogramms.elegion.repository.TaskRepository
import com.castprogramms.elegion.repository.UserRepository
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class CheckViewModel(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    fun createTask(checkItem: CheckItem): Flow<Resource<DocumentReference>> {
        return userRepository.currentUser?.let {
            val task = checkItem.copy(
                hostId = it.userId
            )
            taskRepository.createTask(task)
        } ?: flowOf(Resource.Error("USER NOT AUTH"))
    }

    fun loadTasks(): Flow<Resource<List<CheckItem>>> {
        return taskRepository.loadAllTasks(userRepository.currentUser?.userId ?: "")
    }

    fun changeTaskStatus(checkItem: CheckItem, status: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.changeStatus(checkItem, status)
        }
    }
}