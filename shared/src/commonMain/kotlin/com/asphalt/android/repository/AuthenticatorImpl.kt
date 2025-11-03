package com.asphalt.android.repository


import com.asphalt.android.model.LoginResult
import com.asphalt.android.model.User

expect class AuthenticatorImpl() {
    suspend fun signUp(user: User): Result<String>
    suspend fun signIn(email: String, password: String): LoginResult
    suspend fun resetPassword(email: String): Result<String>
    suspend fun logout(): Result<String>
}

