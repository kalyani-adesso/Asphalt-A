package com.asphalt.android.model.connectedride

@kotlinx.serialization.Serializable
data class RatingRequest(
    val userId: String,
    val rideId: String,
    val stars: Int,
    val comments: String
)