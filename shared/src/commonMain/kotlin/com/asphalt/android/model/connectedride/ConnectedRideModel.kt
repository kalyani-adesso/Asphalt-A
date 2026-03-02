package com.asphalt.android.model.connectedride

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ConnectedRideRoot(
    @SerialName("rideID") val rideID: String? = null,

    @SerialName("userID") val userID: String? = null,

    @SerialName("currentLat") val currentLat: Double? = null,

    @SerialName("currentLong") val currentLong: Double? = null,

    @SerialName("speedInKph") val speedInKph: Double? = null,

    @SerialName("status") val status: String? = null,

    @SerialName("dateTime") val dateTime: Long? = null,

    @SerialName("isRejoined") val isRejoined: Boolean? = null,

    @SerialName("rideJoinedID") val rideJoinedID: String? = null,
    @SerialName("rideStartedTime") val rideStartedTime: Long = 0

)

@Serializable
data class ConnectedRideDTO(
    val rideJoinedID: String,
    val rideID: String,
    val userID: String,
    val currentLat: Double = 0.0,
    val currentLong: Double = 0.0,
    val speedInKph: Double = 0.0,
    val status: String,
    val dateTime: Long,
    val isRejoined: Boolean,
    val distanceTravelled: Double = 0.0,
    val rideStartedTime: Long = 0,
    var canTrack: Boolean = true,
)

@Serializable
data class FirebasePushResponse(
    val name: String // Firebase returns this when you use `.push()`
)




