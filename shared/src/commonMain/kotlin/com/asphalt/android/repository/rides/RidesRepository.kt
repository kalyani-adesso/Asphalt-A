package com.asphalt.android.repository.rides

import com.asphalt.android.mapApiResult
import com.asphalt.android.mappers.toRideInviteListDomain
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.rides.CreateRideRoot
import com.asphalt.android.model.rides.ParticipantData
import com.asphalt.android.model.rides.RideInvitesDomain
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.model.rides.UserInvites
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

    suspend fun getRideInvites(userID: String): APIResult<List<RideInvitesDomain>> {
        return apiService.getAllRide().mapApiResult {
            it.toRides().toRideInviteListDomain(userID)
        }
    }

    suspend fun getSingeRide(rideID: String): APIResult<RidesData> {
        return apiService.getSingleRide(rideID).mapApiResult {
            it.toSingleRide(rideID)
        }
    }

    suspend fun changeRideInviteStatus(
        rideID: String,
        currentUid: String,
        inviteStatus: Int
    ): APIResult<Unit> {
        return apiService.changeRideInviteStatus(
            rideID,
            currentUid,
            UserInvites(acceptInvite = inviteStatus)
        )
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

    fun CreateRideRoot.toSingleRide(rideId: String): RidesData {
        return RidesData(
            ridesID = rideId,
            createdBy = this.userID,
            rideType = this.rideType,
            rideTitle = this.rideTitle,
            description = this.description,
            startDate = this.startDate,
            startLocation = this.startLocation,
            endLocation = this.endLocation,
            createdDate = this.createdDate,
            participants = this.participants?.map { (id, data) ->
                ParticipantData(
                    userId = id,
                    inviteStatus = data.acceptInvite
                )
            } ?: emptyList(),
            startLatitude = this.startLatitude,
            startLongitude = this.startLongitude,
            endLatitude = this.endLatitude,
            endLongitude = this.endLongitude,
            rideDistance = this.distance
        )


    }

}