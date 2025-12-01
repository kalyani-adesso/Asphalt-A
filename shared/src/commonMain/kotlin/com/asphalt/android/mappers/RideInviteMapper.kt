@file:OptIn(ExperimentalTime::class)

package com.asphalt.android.mappers

import com.asphalt.android.constants.APIConstants
import com.asphalt.android.model.rides.RideInvitesDomain
import com.asphalt.android.model.rides.RidesData
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

fun RidesData.getUserInviteStatus(userID: String): Int? {
    val participantData = participants.find { it.userId == userID }
    return participantData?.inviteStatus
}

fun RidesData.isUserInvited(userID: String): Boolean {
    return with(this) {
        getUserInviteStatus(userID) == APIConstants.RIDE_INVITED
    }
}

fun RidesData.isOrganiser(userID: String): Boolean {
    return with(this) {
        this.createdBy == userID
    }
}

fun List<RidesData>.toRideInviteListDomain(userID: String): List<RideInvitesDomain> {
    return with(this) {
        mapNotNull { ridesData ->
            ridesData.startDate?.let {
                if (it > Clock.System.now().toEpochMilliseconds()) {
                    val organiser = ridesData.isOrganiser(userID)
                    if (ridesData.isUserInvited(userID) || organiser) ridesData.toRideInvitesDomain(
                        organiser
                    ) else null
                } else null
            }
        }.sortedBy { it.startDateTime }
    }
}

private fun RidesData.toRideInvitesDomain(organiser: Boolean): RideInvitesDomain {
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
            },
            isOrganiser = organiser,
            rideTitle = this.rideTitle.orEmpty()
        )
    }
}
