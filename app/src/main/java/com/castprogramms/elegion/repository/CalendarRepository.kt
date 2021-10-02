package com.castprogramms.elegion.repository

import com.castprogramms.elegion.data.CalendarEvent
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class CalendarRepository {
    companion object {
        private const val COLLECTION_CALENDAR = "calendar"
    }

    private val calendarCollection = FirebaseFirestore.getInstance().collection(COLLECTION_CALENDAR)

    fun loadAllEvents(): Flow<Resource<List<CalendarEvent>>> =
        flow<Resource<List<CalendarEvent>>> {
            emit(Resource.Loading())

            val snapshot = calendarCollection.get().await()
            val event = snapshot.toObjects(CalendarEvent::class.java)
            emit(Resource.Success(event))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun addEvent(event: CalendarEvent) = flow<Resource<DocumentReference>> {
        emit(Resource.Loading())
        val eventRef = calendarCollection.add(event).await()
        emit(Resource.Success(eventRef))
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}