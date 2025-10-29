package com.asphalt.android.model
import kotlinx.serialization.Serializable

@Serializable
data class ProfileInfo(
    val phoneNumber: String,
    val emergencyContact: String,
    val drivingLicense: String,
    val isMechanic: Boolean
)

@Serializable
data class BikeInfo(
    val bikeType: String,
    val make: String,
    val model: String
)

@Serializable
data class UserProfile(
    val profile: ProfileInfo? = null,
    val bikeInfo: Map<String, BikeInfo>? = null
)