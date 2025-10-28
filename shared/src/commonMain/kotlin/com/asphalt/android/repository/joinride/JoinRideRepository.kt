package com.asphalt.android.repository.joinride

import com.asphalt.android.model.joinride.JoinRideModel

class JoinRideRepository {

    private val riders = listOf(
        JoinRideModel(
            rideType = "Weekend Coast Ride", byWhom = "by Sooraj",
            destination = "Kochi-KanyaKumari", distance = "280km",
            dateTime = "Sun, Nov-9 - 9:00AM", riders = "3/8 Riders"
        ),

        JoinRideModel(
            rideType = "Solo Ride", byWhom = "by Sooraj",
            destination = "Kochi-KanyaKumari", distance = "280km",
            dateTime = "Sun, Nov-9 - 9:00AM", riders = "3/8 Riders"
        )
    )

    fun getAllRiders() : List<JoinRideModel> = riders

}