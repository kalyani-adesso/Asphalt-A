package com.asphalt.android.repository.rides

import com.asphalt.android.mapApiResult
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.rides.CreateRideRoot
import com.asphalt.android.model.rides.ParticipantData
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.network.rides.RidesApIService

class RidesRepository(val apiService: RidesApIService) {
    suspend fun createRide(createRideRoot: CreateRideRoot): APIResult<GenericResponse> {
        return apiService.createRide(createRideRoot).mapApiResult {
            it
        }
    }

    suspend fun getAllRide(): APIResult<List<RidesData>> {
        return apiService.getAllRide().mapApiResult { response ->
            response?.toRides().orEmpty()
        }
    }

    fun Map<String, CreateRideRoot>?.toRides(): List<RidesData> {
        return this?.map { (id, rowData) ->
            RidesData(
                ridesID = id,
                userID = rowData.userID,
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
                endLongitude = rowData.endLongitude
            )

        } ?: emptyList()

    }
}