package com.asphalt.android.mappers

import com.asphalt.android.constants.APIConstants
import com.asphalt.android.model.rides.RideInvitesDomain
import com.asphalt.android.model.rides.RidesData

fun RidesData.getUserInviteStatus(userID: String): Int? {
    val participantData = participants.find { it.userId == userID }
    return participantData?.inviteStatus
}

fun RidesData.isUserInvited(userID: String): Boolean {
    return with(this) {
        getUserInviteStatus(userID) == APIConstants.RIDE_INVITED
    }
}

fun List<RidesData>.toRideInviteListDomain(userID: String): List<RideInvitesDomain> {
    return with(this) {
        mapNotNull { ridesData ->
            if (ridesData.isUserInvited(userID)) ridesData.toRideInvitesDomain() else null
        }
    }
}

private fun RidesData.toRideInvitesDomain(): RideInvitesDomain {
    return with(this) {
        RideInvitesDomain(
            ridesID.orEmpty(),
            this.createdBy.orEmpty(),
            startLocation.orEmpty(),
            endLocation.orEmpty(),
            startDate,
            participants.mapNotNull {
                if (it.inviteStatus == APIConstants.RIDE_ACCEPTED)
                    it.userId
                else null
            }
        )
    }
}
