package com.asphalt.android.network.rides

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.rides.CreateRideRoot

interface RidesApIService {
    suspend fun createRide(
        createRideRoot: CreateRideRoot
    ): APIResult<GenericResponse>

    suspend fun getAllRide() :  APIResult<Map<String, CreateRideRoot>>
}