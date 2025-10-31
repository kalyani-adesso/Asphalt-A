package com.asphalt.android.network.rides

import com.asphalt.android.constants.APIConstants.QUERIES_URL
import com.asphalt.android.constants.APIConstants.RIDES_URL
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.rides.CreateRideRoot
import com.asphalt.android.network.BaseAPIService
import com.asphalt.android.network.KtorClient
import io.ktor.client.call.body

class RidesApiServiceImpl(client: KtorClient) : BaseAPIService(client), RidesApIService {
    override suspend fun createRide(createRideRoot: CreateRideRoot): APIResult<GenericResponse> {
        return safeApiCall {
            post(createRideRoot, RIDES_URL).body()
        }
    }

    override suspend fun getAllRide() : APIResult<Map<String, CreateRideRoot>> {
       return safeApiCall {
            get(url = RIDES_URL).body()
        }
    }
}