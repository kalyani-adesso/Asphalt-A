package com.asphalt.android.network.rides

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.rides.CreateRideRoot
import com.asphalt.android.model.rides.UserInvites

interface RidesApIService {
    suspend fun createRide(
        createRideRoot: CreateRideRoot
    ): APIResult<GenericResponse>

    suspend fun getAllRide() :  APIResult<Map<String, CreateRideRoot>>
    suspend fun changeRideInviteStatus(rideID:String,userID:String,userInvites: UserInvites): APIResult<Unit>

    suspend fun getSingleRide(rideID:String) : APIResult<CreateRideRoot>
}