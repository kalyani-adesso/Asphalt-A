package com.asphalt.android.repository.joinride

import com.asphalt.android.mapApiResult
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.joinride.JoinRideModel
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.network.joinrides.JoinRidesApiService
import kotlin.collections.orEmpty

class JoinRideRepository(val apiService: JoinRidesApiService) {


    private val riders = listOf(
        JoinRideModel(
            rideType = "Weekend Coast Ride", byWhom = "by Sooraj",
            destination = "Kochi-KanyaKumari", distance = "280km",
            dateTime = "Sun, Nov-9 - 9:00AM", riders = "3/8 Riders",
            description = ""
        ),
        JoinRideModel(
            rideType = "Solo Ride", byWhom = "by Sooraj",
            destination = "Kochi-KanyaKumari", distance = "280km",
            dateTime = "Sun, Nov-9 - 9:00AM", riders = "3/8 Riders",
            description = ""
        )
    )
   // fun getAllRiders() : List<JoinRideModel> = riders

    suspend fun getAllRiders() :List<RidesData> {
        return apiService.getAllRide()
    }
}