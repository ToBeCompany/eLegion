package com.castprogramms.elegion.repository

sealed class AuthenticationResult {
    class UserExistYet(): AuthenticationResult()
    class UserDontExist(): AuthenticationResult()
}