package com.asphalt.android.model
import kotlinx.serialization.Serializable

@Serializable
data class ProfileInfo(
    val userName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val emergencyContact: String = "",
    val drivingLicense: String = "",
    val isMechanic: String = "false",
    val bikes: Map<String, BikeInfo>? = null
)

@Serializable
data class BikeInfo(
    val bikeId: String = "",
    val bikeType: String,
    val make: String,
    val model: String
)

@Serializable
data class UserProfile(
    val profile: ProfileInfo? = null,
    val bikeInfo: Map<String, BikeInfo>? = null
)