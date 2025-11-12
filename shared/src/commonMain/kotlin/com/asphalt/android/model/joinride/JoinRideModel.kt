package com.asphalt.android.model.joinride

data class JoinRideModel(
    val rideType: String,
    val byWhom: String,
    val destination : String,
    val distance : String,
    val dateTime: String,
    val riders : String
)