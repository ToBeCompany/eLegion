package com.castprogramms.elegion.data

data class User(
    val userId : String = "",
    val name : String = "",
    val userType: UserType = UserType.BEGINNER,
    val birthday : String = "",
    val contact : String = ""
)
