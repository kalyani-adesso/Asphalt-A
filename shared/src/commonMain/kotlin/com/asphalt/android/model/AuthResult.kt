package com.asphalt.android.model

import kotlinx.serialization.Serializable

interface LoginResult {
    val isSuccess: Boolean
    val errorMessage: String?
    val name: String?
    val email: String?
    val uid: String?
}

@Serializable
class AuthResultimpl(
    override val isSuccess: Boolean,
    override val errorMessage: String? = null,
    override val name: String? = null,
    override val email: String? = null, override val uid: String? = null
) : LoginResult {

}

@Serializable
class CurrentUser(
    val isSuccess: Boolean,
    val errorMessage: String? = null,
    val name: String? = null,
    val email: String? = null,
    val uid: String? = null
)