package com.asphalt.android.network.places

import com.asphalt.android.constants.APIConstants.PLACE_SEARCH
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.places.PlaceData
import com.asphalt.android.network.BaseAPIService
import com.asphalt.android.network.KtorClient
import io.ktor.client.call.body

class PlacesServiceImpl(client: KtorClient) : BaseAPIService(client), PlacesService {
    override suspend fun getAllPlaces(query: String): APIResult<List<PlaceData>> {
        return safeApiCall {
            getPlaces(PLACE_SEARCH + "?q=${query}&format=json&limit=${5}").body()
        }
    }
}