package com.castprogramms.elegion

import com.castprogramms.elegion.data.TelegramAddress
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseReference {
    fun addTelegramAddress(ta: TelegramAddress, db: FirebaseFirestore){
        db.collection("telegramms")
            .add(ta)
    }
}