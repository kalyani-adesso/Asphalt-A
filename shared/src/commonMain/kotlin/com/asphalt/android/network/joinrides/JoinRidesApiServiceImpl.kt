package com.asphalt.android.network.joinrides

import com.asphalt.android.constants.APIConstants.RIDES_URL
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.network.BaseAPIService
import com.asphalt.android.network.KtorClient
import io.ktor.client.call.body

class JoinRidesApiServiceImpl(client: KtorClient) : BaseAPIService(client) ,JoinRidesApiService {

    override suspend fun getAllRide(): List<RidesData> {
        val response : Map<String, RidesData> = get(url = RIDES_URL).body()
        return response.values.toList()
    }
}