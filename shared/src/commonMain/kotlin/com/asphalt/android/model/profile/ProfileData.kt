package com.asphalt.android.model.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDTO(
    @SerialName("user_name")
    val userName: String = "",
    val email: String = "",
    @SerialName("phone_number")
    val phoneNumber: String = "",
    @SerialName("emergency_contact")
    val emergencyContact: String = "",
    @SerialName("driving_license")
    val drivingLicense: String = "",
    @SerialName("is_mechanic")
    val isMechanic: Boolean = false,
    val bikes: Map<String, BikeDTO>? = null
)

@Serializable
data class BikeDTO(
    @SerialName("bike_id")
    val bikeId: String = "",
    @SerialName("bike_type")
    val bikeType: String = "",
    val make: String = "",
    val model: String = ""
)

data class ProfileDomain(
    val userName: String,
    val email: String,
    val phoneNumber: String,
    val emergencyContact: String,
    val drivingLicense: String,
    val isMechanic: Boolean,
    val bikes: List<BikeDomain>
)

data class BikeDomain(
    val bikeId: String,
    val type: String,
    val make: String,
    val model: String
)