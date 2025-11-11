package com.asphalt.dashboard.utils

import com.asphalt.android.constants.APIConstants
import com.asphalt.android.model.UserDomain
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.utils.Utils
import com.asphalt.dashboard.constants.RideStatConstants.QUEUE
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
                            QUEUE
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
                date = ride.startDate?.let { Utils.getDateWithTime(ride.startDate) } ?: "",
                riders = ride.participants.size,
                createdBy = ride.createdBy,
                startDate = ride.startDate
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
                    riders = ride.participants.size,
                    createdBy = ride.createdBy,
                    createdUSerName = userDomain?.name ?: "",
                    profileImageUrl = userDomain?.profilePic
                )
            } else {
                null // Skip if participant not found
            }

        }
    }
}