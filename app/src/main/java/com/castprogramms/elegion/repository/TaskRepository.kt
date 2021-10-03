package com.castprogramms.elegion.repository

import android.util.Log
import com.castprogramms.elegion.data.CheckItem
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TaskRepository {
    companion object {
        private const val COLLECTION_TASK = "tasks"
    }

    private val startedTasks = listOf(
        CheckItem(title = "Добавьтесь в чаты в телеграмме"),
        CheckItem(title = "Ознакомтесь с информацией в Welcome book"),
        CheckItem(title = "Ознакомтесь с красной книгой языков программирования"),
    )

    private val taskCollection = FirebaseFirestore.getInstance().collection(COLLECTION_TASK)

    suspend fun loadStartedTasks(userId: String) = withContext(Dispatchers.IO) {
        startedTasks.map {
            it.copy(hostId = userId)
        }.forEach {
            taskCollection.add(it)
        }
    }

    fun loadAllTasks(userId: String): Flow<Resource<List<CheckItem>>> =
        flow<Resource<List<CheckItem>>> {
            emit(Resource.Loading())
            val snapshot = taskCollection.whereEqualTo(CheckItem::hostId.name, userId).get().await()
            val task =
                snapshot.map { it.toObject(CheckItem::class.java).copy(path = it.reference.id) }
            Log.d("OHOHO", task.toList().toString())
            emit(Resource.Success(task))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun createTask(check: CheckItem) = flow<Resource<DocumentReference>> {
        emit(Resource.Loading())
        val addressRef = taskCollection.add(check).await()
        emit(Resource.Success(addressRef))
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun changeStatus(check: CheckItem, status: Boolean): Task<Void> {
        Log.d("OHOHO", check.toString())

        return taskCollection.document(check.path).update(CheckItem::complete.name, status)
    }

}