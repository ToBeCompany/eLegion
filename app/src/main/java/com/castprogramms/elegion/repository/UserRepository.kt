package com.castprogramms.elegion.repository

import com.castprogramms.elegion.data.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserRepository {
    companion object {
        const val COLLECTIONS_USERS = "users"
    }

    private val usersCollection = FirebaseFirestore.getInstance().collection(COLLECTIONS_USERS)

    fun createUser(user: User) = flow<Resource<DocumentReference>> {
        emit(Resource.Loading())
        val addressRef = usersCollection.add(user).await()
        emit(Resource.Success(addressRef))
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun getUser(userId: String) = flow<Resource<User>> {
        emit(Resource.Loading())
        val user = usersCollection.document(userId).get().await()
        if (user != null) {
            if (user.toObject(User::class.java) != null)
                emit(Resource.Success(user.toObject(User::class.java)!!))
            else
                emit(Resource.Error("Не тот тип данных"))
        } else {
            emit(Resource.Error("Нет такого пользователя"))
        }
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}
