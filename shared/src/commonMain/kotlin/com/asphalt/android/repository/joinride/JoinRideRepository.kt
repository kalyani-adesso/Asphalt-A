package com.asphalt.android.repository.joinride

import com.asphalt.android.mapApiResult
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.joinride.JoinRideModel
import com.asphalt.android.model.rides.CreateRideRoot
import com.asphalt.android.model.rides.ParticipantData
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.network.joinrides.JoinRidesApiService
import kotlin.collections.component1
import kotlin.collections.component2
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

    suspend fun getAllRidesList() : APIResult<List<RidesData>> {
        return apiService.getAllRiders().mapApiResult { response ->
            response?.toRides().orEmpty()
        }
    }

    fun Map<String, CreateRideRoot>?.toRides(): List<RidesData> {
        return this?.map { (id, rowData) ->
            RidesData(
                ridesID = id,
                createdBy = rowData.userID,
                rideTitle = rowData.rideTitle,
                description = rowData.description,
                startDate = rowData.startDate,
                startLocation = rowData.startLocation,
                endLocation = rowData.endLocation,
                createdDate = rowData.createdDate,
                participants = rowData.participants?.map { (id, data) ->
                    ParticipantData(userId = id, inviteStatus = data.acceptInvite)
                } ?: emptyList(),
                startLatitude = rowData.startLatitude,
                startLongitude = rowData.startLongitude,
                endLatitude = rowData.endLatitude,
                endLongitude = rowData.endLongitude,
                rideDistance = rowData.distance
            )

        } ?: emptyList()

    }
}