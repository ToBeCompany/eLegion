package com.castprogramms.elegion.repository

import com.castprogramms.elegion.data.CheckItem
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class TaskRepository {
    companion object {
        private const val COLLECTION_TASK = "tasks"
    }

    private val taskCollection = FirebaseFirestore.getInstance().collection(COLLECTION_TASK)

    fun loadAllTasks(userId : String): Flow<Resource<List<CheckItem>>> =
        flow<Resource<List<CheckItem>>> {
            emit(Resource.Loading())
            val snapshot = taskCollection.whereEqualTo(CheckItem::hostId.name,userId).get().await()
            val task = snapshot.toObjects(CheckItem::class.java)
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

    fun complete(check: CheckItem) = flow<Resource<Boolean>> {
        emit(Resource.Loading())
        taskCollection.document().update("isComplete", true).await()
        emit(Resource.Success(true))
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}