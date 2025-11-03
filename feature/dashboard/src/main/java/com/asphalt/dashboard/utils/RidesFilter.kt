package com.asphalt.dashboard.utils

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
                ride.createdBy == userId -> QUEUE

                else -> {
                    val participant = ride.participants.find { it.userId == userId }
                    participant?.let {
                        when (it.inviteStatus) {
                            1 -> UPCOMING
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
                createdBy = ride.createdBy
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

            // Step 3: check if participant exists and inviteStatus == 1
            if (participant != null && participant.inviteStatus == 0) {
                YourRideDataModel(
                    ridesId = ride.ridesID,
                    title = "",
                    place = (ride.startLocation ?: "") + "-" + (ride.endLocation ?: ""),
                    date = ride.startDate?.let { Utils.getDateWithTime(ride.startDate) } ?: "",
                    riders = ride.participants.size,
                    createdBy = ride.createdBy,
                    createdUSerName = androidUserVM.getUser(ride.createdBy ?: "")?.name ?: ""
                )
            } else {
                null // Skip if participant not found or inviteStatus != 1
            }

        }
    }
}