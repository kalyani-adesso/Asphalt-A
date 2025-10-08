package com.asphalt.android.repository

import com.asphalt.android.model.User

expect class AuthenticatorImpl() {
    suspend fun signUp(user: User) : Result<String>
}