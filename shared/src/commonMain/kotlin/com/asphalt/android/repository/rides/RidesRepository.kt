package com.asphalt.android.repository.rides

import com.asphalt.android.mapApiResult
import com.asphalt.android.mappers.mapAndGroupMonthData
import com.asphalt.android.mappers.toPerMonthRideDataDomain
import com.asphalt.android.mappers.toRideInviteListDomain
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.connectedride.ConnectedRideDTO
import com.asphalt.android.model.connectedride.ConnectedRideRoot
import com.asphalt.android.model.dashboard.DashboardDTO
import com.asphalt.android.model.dashboard.DashboardDomain
import com.asphalt.android.model.dashboard.PerMonthRideDataDomain
import com.asphalt.android.model.rides.CreateRideRoot
import com.asphalt.android.model.rides.ParticipantData
import com.asphalt.android.model.rides.RideInvitesDomain
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.model.rides.UserInvites
import com.asphalt.android.network.rides.RidesApIService
import io.ktor.util.date.getTimeMillis
import kotlinx.datetime.TimeZone

class RidesRepository(val apiService: RidesApIService) {
    suspend fun createRide(createRideRoot: CreateRideRoot): APIResult<GenericResponse> {
        return apiService.createRide(createRideRoot).mapApiResult {
            it
        }
    }

    suspend fun getAllRide(): APIResult<List<RidesData>> {
        return apiService.getAllRide().mapApiResult { response ->
            response.toRides().orEmpty()
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
                rideDistance = rowData.distance,
                rideStatus = rowData.rideStatus
            )
        } ?: emptyList()
    }

    suspend fun joinRide(joinRide: ConnectedRideRoot): APIResult<ConnectedRideDTO> {
        return apiService.joinRide(joinRide).mapApiResult { response ->
            ConnectedRideDTO(
                rideJoinedID = response.name,
                rideID = joinRide.rideID ?: "",
                userID = joinRide.userID ?: "",
                currentLat = joinRide.currentLat ?: 0.0,
                currentLong = joinRide.currentLong ?: 0.0,
                speedInKph = joinRide.speedInKph ?: 0.0,
                status = joinRide.status ?: "",
                dateTime = joinRide.dateTime ?: 0L,
                isRejoined = joinRide.isRejoined ?: false
            )
        }
    }

    suspend fun reJoinRide(
        rejoinRide: ConnectedRideRoot,
        ongoingRideId: String
    ): APIResult<ConnectedRideDTO> {
        return apiService.rejoinRide(rejoinRide, ongoingRideId).mapApiResult { response ->
            ConnectedRideDTO(
                rideJoinedID = response.name,
                rideID = rejoinRide.rideID ?: "",
                userID = rejoinRide.userID ?: "",
                currentLat = rejoinRide.currentLat ?: 0.0,
                currentLong = rejoinRide.currentLong ?: 0.0,
                speedInKph = rejoinRide.speedInKph ?: 0.0,
                status = rejoinRide.status ?: "",
                dateTime = rejoinRide.dateTime ?: 0L,
                isRejoined = rejoinRide.isRejoined ?: false
            )
        }
    }

    suspend fun endRide(rideId: String, rideJoinedId: String): APIResult<Unit> {
        return apiService.endRide(rideId, rideJoinedId)
    }

    suspend fun getOngoingRides(rideId: String): APIResult<List<ConnectedRideDTO>> {
        return apiService.getJoinedRides(rideId).mapApiResult { response ->
            response.map { (joinedRideId, data) ->
                ConnectedRideDTO(
                    rideJoinedID = joinedRideId,
                    rideID = data.rideID ?: "",
                    userID = data.userID ?: "",
                    currentLat = data.currentLat ?: 0.0,
                    currentLong = data.currentLong ?: 0.0,
                    speedInKph = data.speedInKph ?: 0.0,
                    status = data.status ?: "",
                    dateTime = data.dateTime ?: 0L,
                    isRejoined = data.isRejoined ?: false
                )
            }
        }
    }

    suspend fun endRideSummary(userID: String, endRide: DashboardDTO): APIResult<DashboardDTO> {
        return apiService.endRideSummary(userID, endRide).mapApiResult { response ->
            DashboardDTO(
                rideID = endRide.rideID,
                rideDistance = endRide.rideDistance ?: 0.0,
                isGroupRide = endRide.isGroupRide ?: false,
                startLocation = endRide.startLocation ?: "",
                endLocation = endRide.endLocation ?: "",
                isOrganiserGroupRide = endRide.isOrganiserGroupRide ?: false,
                isParticipantGroupRide = endRide.isParticipantGroupRide ?: false,
                endRideDate = endRide.endRideDate ?: 0
            )
        }
    }

    suspend fun getRideSummary(userID: String): APIResult<List<DashboardDomain>>? {
        return apiService.getRideSummary(userID)?.mapApiResult { response ->
            response.toDashboardDomain()
        }
    }

    fun Map<String, DashboardDTO>?.toDashboardDomain(): List<DashboardDomain> {
        return this.toPerMonthRideDataDomain().mapAndGroupMonthData(TimeZone.currentSystemDefault())
    }


    suspend fun updateOrganizerStatus(rideId: String, rideStatus: Int): APIResult<Unit> {
        return apiService.updateOrganizerStatus(rideId, rideStatus)
    }

    suspend fun rateYourRide(
        rideId: String,
        userId: String,
        stars: Int,
        comments: String
    ): APIResult<Unit> {
        return apiService.rateYourRide(rideId, userId, stars, comments)
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
            rideDistance = this.distance,
            rideStatus = this.rideStatus
        )
    }
}