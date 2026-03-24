package com.asphalt.android.network.places

import com.asphalt.android.constants.APIConstants.GET_POLY_LINE
import com.asphalt.android.constants.APIConstants.PLACE_SEARCH
import com.asphalt.android.constants.APIConstants.POLY_LINE_API
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.places.OSRMResponse
import com.asphalt.android.model.places.PhotonFeature
import com.asphalt.android.model.places.PhotonResponse
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

    override suspend fun getAllPlacesAutoComplete(query: String): APIResult<PhotonResponse> {
        return safeApiCall {
            getAutoCompletePlaces("?q=${query}&limit=${5}").body()
        }
    }

    override suspend fun getPolyLine(
        startLat: Double,
        startLon: Double,
        endLat: Double,
        endLon: Double
    ): APIResult<OSRMResponse> {
        return safeApiCall {
            getPolyLines(
                GET_POLY_LINE + "${startLon},${startLat};${endLon},${endLat}" +
                        "?overview=full&geometries=geojson"
            ).body()
        }
    }
}