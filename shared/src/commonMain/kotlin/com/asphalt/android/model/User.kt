package com.asphalt.android.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("email")
    val email: String? = null,
    @SerialName("password")
    val password: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("confirmPassword")
    val confirmPassword: String? = null
)

data class UserDomain(
    val uid: String,
    val email: String,
    val name: String,
    val profilePic: String,
    val isMechanic:Boolean
)

