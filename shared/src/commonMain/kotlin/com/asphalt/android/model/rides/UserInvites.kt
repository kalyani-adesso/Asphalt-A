package com.asphalt.android.model.rides

import com.asphalt.android.constants.APIConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
 data class UserInvites(

    @SerialName("acceptInvite")
    var acceptInvite: Int = APIConstants.RIDE_INVITED // 0 - invite sent,1- invite accept, 2- invite declined, 3 - participant joined.
)

@Serializable
data class Ratings (
    @SerialName("stars")
    var stars: Int = 0,
)

data class RideInvitesDomain(
    val rideID: String,
    val inviter: String,
    val startLocation: String,
    val destination: String,
    val startDateTime: Long?,
    val acceptedParticipants: List<String>
)