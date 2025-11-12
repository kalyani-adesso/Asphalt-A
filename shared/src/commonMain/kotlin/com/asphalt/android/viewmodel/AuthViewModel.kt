package com.asphalt.android.viewmodel

import androidx.lifecycle.ViewModel
import com.asphalt.android.model.AuthResultimpl
import com.asphalt.android.model.LoginResult
import com.asphalt.android.model.User
import com.asphalt.android.repository.AuthenticatorImpl

class AuthViewModel(private val authenticator: AuthenticatorImpl) : ViewModel() {

    suspend fun SignUp(user: User) : Result<String> {

        return try {
            authenticator.signUp(user)
        } catch (e: Exception) {
            Result.failure(exception = e)
        }
    }

    suspend fun signIn(email:String,password:String) : LoginResult {
        return try {
            authenticator.signIn(email,password)
        } catch (e: Exception) {
            return AuthResultimpl(false,"Something went wrong")
        }
    }
}