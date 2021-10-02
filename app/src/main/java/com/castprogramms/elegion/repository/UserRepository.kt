package com.castprogramms.elegion.repository

import com.castprogramms.elegion.data.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class UserRepository {
    companion object {
        const val COLLECTIONS_USERS = "users"
    }

    private val usersCollection = FirebaseFirestore.getInstance().collection(COLLECTIONS_USERS)

    var currentUser: User? = null
        private set

    fun createUser(user: User) = flow<Resource<DocumentReference>> {
        emit(Resource.Loading())
        val addressRef = usersCollection.add(user).await()
        emit(Resource.Success(addressRef))
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    suspend fun hasUser(userId: String): User? = try {
        usersCollection.whereEqualTo(User::userId.name, userId).get().await()
            .toObjects(User::class.java).first()
    } catch (e: Exception) {
        null
    }

    suspend fun getUser(userId: String) =
        usersCollection.whereEqualTo(User::userId.name, userId).get().await()
            .toObjects(User::class.java).first()

    fun auth(user: User) {
        currentUser = user
    }

    fun singOut() {
        currentUser = null
    }

}
