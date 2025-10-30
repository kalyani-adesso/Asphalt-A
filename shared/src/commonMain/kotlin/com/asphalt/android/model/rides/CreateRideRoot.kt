package com.asphalt.android.model.rides

import kotlinx.serialization.Serializable

@Serializable
class CreateRideRoot(
    var userID: String? = null,
    var rideType: String? = null,
    var rideTitle: String? = null,
    var description: String? = null,
    var startDate: Long? = null,
    var hour: Int? = null,
    var mins: Int? = null,
    var isAm: Boolean = false,
    var startLocation: String? = null,
    var endLocation: String? = null,
    var createdDate: Long? = null,
    var invites: Map<String, UserInvites>? = null
)