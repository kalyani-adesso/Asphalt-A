package com.asphalt.android.model.joinride

import kotlinx.serialization.Serializable

@Serializable
data class JoinRideModel(
    val rideType: String,
    val byWhom: String,
    val description: String,
    val destination : String,
    val distance : String,
    val dateTime: String,
    val riders : String
)