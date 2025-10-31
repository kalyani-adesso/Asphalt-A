package com.asphalt.android.model.rides

import kotlinx.serialization.Serializable

@Serializable
class CreateRideRoot(
    var userID: String? = null,
    var rideType: String? = null,
    var rideTitle: String? = null,
    var description: String? = null,
    var startDate: Long? = null,
    var startLocation: String? = null,
    var endLocation: String? = null,
    var createdDate: Long? = null,
    var participants: Map<String, UserInvites>? = null,
    var startLatitude: Double = 0.0,
    var startLongitude: Double = 0.0,
    var endLatitude: Double = 0.0,
    var endLongitude: Double = 0.0
)