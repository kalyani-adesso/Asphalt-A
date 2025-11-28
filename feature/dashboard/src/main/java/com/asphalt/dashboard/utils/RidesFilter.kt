package com.asphalt.dashboard.utils

import com.asphalt.android.constants.APIConstants
import com.asphalt.android.model.UserDomain
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.utils.Utils
import com.asphalt.dashboard.constants.RideStatConstants.HISTORY
import com.asphalt.dashboard.constants.RideStatConstants.UPCOMING
import com.asphalt.dashboard.data.YourRideDataModel

object RidesFilter {
    fun getUComingRides(
        allRides: List<RidesData>,
        userId: String,
        androidUserVM: AndroidUserVM
    ): List<YourRideDataModel> {
        return allRides.mapNotNull { ride ->

            val rideStatus: String? = when {
                ride.createdBy == userId -> {
                    if (ride.participants.isNullOrEmpty()) {
                        UPCOMING
                    } else {
                        val participant =
                            ride.participants.find { it.inviteStatus == APIConstants.RIDE_INVITED }
                        if (participant == null) {
                            UPCOMING
                        } else {
                            UPCOMING // previously it was "QUEUE " as per new discussion show all the rides with upcoming, may be there will be a logic change here
                        }

                    }

                }

                else -> {
                    val participant = ride.participants.find { it.userId == userId }
                    participant?.let {
                        when (it.inviteStatus) {
                            APIConstants.RIDE_ACCEPTED -> UPCOMING
                            else -> null
                        }
                    }
                }
            }


            if (rideStatus == null) return@mapNotNull null

            YourRideDataModel(
                ridesId = ride.ridesID,
                title = ride.rideTitle ?: "",
                place = (ride.startLocation ?: "") + "-" + (ride.endLocation ?: ""),
                rideStatus = rideStatus,
                date = ride.startDate?.let { Utils.getDateWithOutTime(ride.startDate) } ?: "",
                riders = ride.participants.size + 1,// need to count the organizer
                createdBy = ride.createdBy,
                startDate = ride.startDate,
                startTime = ride.startDate?.let { Utils.getTime(ride.startDate) } ?: "",
                endDateDisplay = ride.endDate?.let { Utils.getDateWithOutTime(ride.endDate) } ?: "",
                endDate = ride.startDate,
                endTime = ride.endDate?.let { Utils.getTime(ride.endDate) } ?: "",

                )
        }
    }

    fun getInvites(
        allRides: List<RidesData>,
        userId: String, androidUserVM: AndroidUserVM
    ): List<YourRideDataModel> {
        return allRides.mapNotNull { ride ->

            // Step 1: skip if user is the owner
            if (ride.createdBy == userId) return@mapNotNull null

            // Step 2: find participant matching current user
            val participant = ride.participants.find { it.userId == userId }

            // Step 3: check if participant exists and inviteStatus == 0
            if (participant != null && participant.inviteStatus == APIConstants.RIDE_INVITED) {
                val userDomain: UserDomain? = androidUserVM.getUser(ride.createdBy ?: "")
                YourRideDataModel(
                    ridesId = ride.ridesID,
                    title = "",
                    place = (ride.startLocation ?: "") + "-" + (ride.endLocation ?: ""),
                    date = ride.startDate?.let { Utils.getDateWithTime(ride.startDate) } ?: "",
                    riders = ride.participants.size + 1,// need to count the organizer
                    createdBy = ride.createdBy,
                    createdUSerName = userDomain?.name ?: "",
                    profileImageUrl = userDomain?.profilePic
                )
            } else {
                null // Skip if participant not found
            }

        }
    }


    fun getHistoryRide(
        rides: List<RidesData>,
        userId: String
    ): List<YourRideDataModel> {
        return rides.filter { ride ->
            (ride.createdBy == userId && ride.rideStatus == APIConstants.END_RIDE) ||
                    (ride.participants.any { it.userId == userId && it.inviteStatus == APIConstants.END_RIDE })
        }.map { ride ->
            YourRideDataModel(
                ridesId = ride.ridesID,
                title = ride.rideTitle,
                place = (ride.startLocation ?: "") + "-" + (ride.endLocation ?: ""),
                rideStatus = HISTORY,
                date = ride.startDate?.let { Utils.getDateWithTime(ride.startDate) } ?: "",
                riders = ride.participants.size + 1,// need to count the organizer
                createdBy = ride.createdBy,
                startDate = ride.startDate
            )
        }
    }
}