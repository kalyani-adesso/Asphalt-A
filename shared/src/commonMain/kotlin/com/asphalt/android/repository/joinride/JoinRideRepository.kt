package com.asphalt.android.repository.joinride

import com.asphalt.android.mapApiResult
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.connectedride.ConnectedRideRoot
import com.asphalt.android.model.joinride.JoinRideModel
import com.asphalt.android.model.rides.CreateRideRoot
import com.asphalt.android.model.rides.ParticipantData
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.network.joinrides.JoinRidesApiService
import com.asphalt.android.repository.rides.RidesRepository
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.orEmpty

class JoinRideRepository(val apiService: JoinRidesApiService) {
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