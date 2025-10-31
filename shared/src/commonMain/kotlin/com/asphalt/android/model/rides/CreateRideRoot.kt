package com.asphalt.android.model.rides

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CreateRideRoot(
    @SerialName("userID")
    var userID: String? = null,

    @SerialName("rideType")
    var rideType: String? = null,

    @SerialName("rideTitle")
    var rideTitle: String? = null,

    @SerialName("description")
    var description: String? = null,

    @SerialName("startDate")
    var startDate: Long? = null,

    @SerialName("startLocation")
    var startLocation: String? = null,

    @SerialName("endLocation")
    var endLocation: String? = null,

    @SerialName("createdDate")
    var createdDate: Long? = null,

    @SerialName("participants")
    var participants: Map<String, UserInvites>? = null,

    @SerialName("startLatitude")
    var startLatitude: Double = 0.0,

    @SerialName("startLongitude")
    var startLongitude: Double = 0.0,

    @SerialName("endLatitude")
    var endLatitude: Double = 0.0,

    @SerialName("endLongitude")
    var endLongitude: Double = 0.0
)