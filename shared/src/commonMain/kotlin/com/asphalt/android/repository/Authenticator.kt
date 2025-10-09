package com.asphalt.android.repository

import com.asphalt.android.model.User

interface Authenticator {

    suspend fun signUpWithEmail(user: User) : Result<String>
}