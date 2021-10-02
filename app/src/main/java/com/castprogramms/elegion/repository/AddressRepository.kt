package com.castprogramms.elegion.repository

import com.castprogramms.elegion.data.TelegramAddress
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class AddressRepository {
    companion object {
        private const val COLLECTION_CHATS = "telegramms"
    }

    private val telegramCollection = FirebaseFirestore.getInstance().collection(COLLECTION_CHATS)

    private var chatcache: MutableList<TelegramAddress> = mutableListOf()
    fun getAddress(index: Int) = chatcache.getOrNull(index)

    fun loadAllChats(): Flow<Resource<List<TelegramAddress>>> =
        flow<Resource<List<TelegramAddress>>> {
            emit(Resource.Loading())

            val snapshot = telegramCollection.get().await()
            val address = snapshot.toObjects(TelegramAddress::class.java)
            chatcache = address
            emit(Resource.Success(address))
        }.catch {
            chatcache.let {
                if (it.isNotEmpty())
                    emit(Resource.Loading(it))
            }
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun addAddress(telegramAddress: TelegramAddress) = flow<Resource<DocumentReference>> {
        emit(Resource.Loading())
        val addressRef = telegramCollection.add(telegramAddress).await()
        emit(Resource.Success(addressRef))
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}