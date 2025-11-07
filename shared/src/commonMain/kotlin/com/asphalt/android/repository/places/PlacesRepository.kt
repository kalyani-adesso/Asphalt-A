package com.asphalt.android.repository.places

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.places.PlaceData
import com.asphalt.android.network.places.PlacesService

class PlacesRepository(private val apiService: PlacesService) {
    suspend fun getPlaces(location: String): APIResult<List<PlaceData>> {
        return apiService.getAllPlaces(location)
    }


}