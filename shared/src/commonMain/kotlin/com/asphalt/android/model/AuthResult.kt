package com.asphalt.android.model

interface LoginResult {
    val isSuccess: Boolean
    val errorMessage: String?
}

class AuthResultimpl(
    override val isSuccess: Boolean,
    override val errorMessage: String? = null
) : LoginResult {

}