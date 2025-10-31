package com.asphalt.android.repository.rides

import com.asphalt.android.mapApiResult
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.rides.CreateRideRoot
import com.asphalt.android.network.rides.RidesApIService

class RidesRepository(val apiService: RidesApIService) {
    suspend fun createRide(createRideRoot: CreateRideRoot): APIResult<GenericResponse> {
        return apiService.createRide(createRideRoot).mapApiResult {
            it
        }
    }
}