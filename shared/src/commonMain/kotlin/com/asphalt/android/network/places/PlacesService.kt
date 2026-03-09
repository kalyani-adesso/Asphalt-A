package com.asphalt.android.network.places

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.places.OSRMResponse
import com.asphalt.android.model.places.PlaceData

interface PlacesService {
     suspend fun getAllPlaces(query: String) : APIResult<List<PlaceData>>
     suspend fun getPolyLine(startLat:Double, startLon: Double,endLat:Double,endLon:Double) : APIResult<OSRMResponse>
}