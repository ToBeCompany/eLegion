package com.castprogramms.elegion.repository

import com.castprogramms.elegion.data.User
import com.castprogramms.elegion.data.UserType
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class UserRepository {
    companion object {
        private const val COLLECTION_USERS = "users"
    }

    private val usersCollection = FirebaseFirestore.getInstance().collection(COLLECTION_USERS)

    //TODO replace init value to null
    var currentUser : User? = User("123456", "name", UserType.BEGINNER, "123124", "tk")
        private set

    fun createUser(user: User) = flow<Resource<DocumentReference>> {
        emit(Resource.Loading())
        val addressRef = usersCollection.add(user).await()
        emit(Resource.Success(addressRef))
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}
