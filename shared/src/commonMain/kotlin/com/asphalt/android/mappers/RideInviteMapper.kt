@file:OptIn(ExperimentalTime::class)

package com.asphalt.android.mappers

import com.asphalt.android.constants.APIConstants
import com.asphalt.android.model.rides.RideInvitesDomain
import com.asphalt.android.model.rides.RidesData
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
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
                val startOfToday = Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .date
                    .atStartOfDayIn(TimeZone.currentSystemDefault())
                    .toEpochMilliseconds()
                if (it > startOfToday) {
                    val organiser = ridesData.isOrganiser(userID)
                    if (organiser && ridesData.isRideEndedByOrganiser()) {
                        null
                    } else
                        if (ridesData.isUserInvited(userID) || organiser) ridesData.toRideInvitesDomain(
                            organiser
                        ) else null
                } else null
            }
        }.sortedBy { it.startDateTime }
    }
}

fun RidesData.isRideEndedByOrganiser(): Boolean {
    return with(this) {
        rideStatus == APIConstants.END_RIDE

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
            rideTitle = this.rideTitle.orEmpty(),
            allParticipants = participants.map { it.userId }.plus(this.createdBy.orEmpty()),
            rideType = rideType.orEmpty()
        )
    }
}
