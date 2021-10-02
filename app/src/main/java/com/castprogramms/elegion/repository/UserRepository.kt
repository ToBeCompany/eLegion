package com.castprogramms.elegion.repository

import com.castprogramms.elegion.data.User
import com.castprogramms.elegion.data.UserType
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

    var currentUser : User? = null//User("123456", "name", UserType.BEGINNER, "123124", "tk")
        private set

    fun createUser(user: User) = flow<Resource<DocumentReference>> {
        emit(Resource.Loading())
        val addressRef = usersCollection.add(user).await()
        emit(Resource.Success(addressRef))
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    suspend fun hasUser(userId: String) : User? = try {
        usersCollection.whereEqualTo(User::userId.name, userId).get().await().toObjects(User::class.java).first()
    } catch (e : Exception) {
        null
    }

    fun getUser(userId: String) = flow<Resource<User>> {
        emit(Resource.Loading())
        val user = usersCollection.whereEqualTo(User::userId.name, userId).get().await()
        if (user != null) {
            if (user.toObjects(User::class.java).first() != null)
                emit(Resource.Success(user.first().toObject(User::class.java)))
            else
                emit(Resource.Error("Не тот тип данных"))
        } else {
            emit(Resource.Error("Нет такого пользователя"))
        }
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun auth(user : User){
        currentUser = user
    }

    fun singOut() {
        currentUser = null
    }

}
