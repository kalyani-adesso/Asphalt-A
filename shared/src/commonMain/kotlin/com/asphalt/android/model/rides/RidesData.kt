package com.asphalt.android.model.rides

import com.asphalt.android.constants.APIConstants
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
class RidesData(
    var ridesID: String? = null,
    val rideJoinedID: String? = null, // not required
    var createdBy: String? = null,
    var rideType: String? = null,
    var rideTitle: String? = null,
    var description: String? = null,
    var startDate: Long? = null,
    var startLocation: String? = null,
    var endLocation: String? = null,
    var createdDate: Long? = null,
    var participants: List<ParticipantData> = emptyList(),
    var startLatitude: Double = 0.0,
    var startLongitude: Double = 0.0,
    var endLatitude: Double = 0.0,
    var endLongitude: Double = 0.0,
    var rideDistance: Double = 0.0,
    var rideStatus: Int = 0,
    var currentLat: Double = 0.0,
    var currentLong: Double = 0.0,
    var endDate: Long? = null,


    var hasAssemblyPoint: Boolean = false,
    var assemblyPoint: String? = null,
    var assemblyLat: Double = 0.0,
    var assemblyLon: Double = 0.0,
    var ratings: List<RatingsData> = emptyList(),
    var images: List<ImageData> = emptyList(),
)

fun RidesData.showRejoinButton(userId: String): Boolean {

    val isCreator = createdBy == userId

    val isParticipantWithStatus3 = participants.any {
        it.userId == userId && it.inviteStatus == APIConstants.RIDE_JOINED
    }

    return (isCreator && rideStatus == APIConstants.RIDE_JOINED) ||
            (!isCreator && isParticipantWithStatus3)
}