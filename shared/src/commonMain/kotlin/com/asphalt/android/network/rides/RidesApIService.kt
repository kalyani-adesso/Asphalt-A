package com.asphalt.android.network.rides

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.dashboard.DashboardDTO
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.connectedride.ConnectedRideRoot
import com.asphalt.android.model.connectedride.FirebasePushResponse
import com.asphalt.android.model.rides.CreateRideRoot
import  com.asphalt.android.model.message.MessageRoot
import com.asphalt.android.model.rides.UserInvites

interface RidesApIService {
    suspend fun createRide(
        createRideRoot: CreateRideRoot
    ): APIResult<GenericResponse>

    suspend fun getAllRide() :  APIResult<Map<String, CreateRideRoot>>
    suspend fun changeRideInviteStatus(rideID:String,userID:String,userInvites: UserInvites): APIResult<Unit>

    suspend fun joinRide(joinRide: ConnectedRideRoot): APIResult<FirebasePushResponse>

    suspend fun rejoinRide(connectedRide: ConnectedRideRoot,ongoingRideId: String): APIResult<FirebasePushResponse>

    suspend fun getJoinedRides(rideId: String): APIResult<Map<String, ConnectedRideRoot>>

    suspend fun endRide(rideId: String, rideJoinedId: String): APIResult<Unit>

    suspend fun updateOrganizerStatus(rideId:String, rideStatus:Int): APIResult<Unit>


    suspend fun rateYourRide(rideId:String, userId:String, stars:Int, comments:String): APIResult<Unit>

    suspend fun getSingleRide(rideID:String) : APIResult<CreateRideRoot>

    suspend fun endRideSummary(userID: String, endRide: DashboardDTO) :  APIResult<FirebasePushResponse>

    suspend fun getRideSummary(userID: String) :  APIResult<Map<String, DashboardDTO>>?

    suspend fun sendMessage(message: MessageRoot) : APIResult<Unit>

}