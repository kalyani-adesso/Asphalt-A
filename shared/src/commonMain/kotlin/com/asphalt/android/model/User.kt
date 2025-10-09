package com.asphalt.android.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val password: String,
    val userName:String,
    val confirmPassword: String
)

