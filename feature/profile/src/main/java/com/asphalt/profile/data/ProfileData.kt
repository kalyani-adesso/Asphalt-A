package com.asphalt.profile.data

data class ProfileData(
    val uid: String?,
    val profilePicUrl: String,
    val username: String,
    val userEmail: String,
    val phoneNumber: String,
    val isMechanic: Boolean,
    val drivingLicenseNumber: String,
    val emergencyContactNumber: String
)