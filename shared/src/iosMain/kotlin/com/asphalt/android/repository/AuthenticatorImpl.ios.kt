package com.asphalt.android.repository

import com.asphalt.android.model.AuthResultimpl
import com.asphalt.android.model.LoginResult
import com.asphalt.android.model.User

actual class AuthenticatorImpl actual constructor() {

    actual suspend fun signUp(user: User): Result<String> {

        return Result.failure(Exception(message = "ios firebase not implemented"))
    }

    actual suspend fun signIn(email: String, password: String): LoginResult {

        return AuthResultimpl(false, "IOs implmentation not done")
    }
}