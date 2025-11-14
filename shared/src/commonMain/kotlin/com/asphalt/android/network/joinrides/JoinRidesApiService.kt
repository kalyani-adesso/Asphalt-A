package com.asphalt.android.network.joinrides

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.joinride.JoinRideModel
import com.asphalt.android.model.rides.CreateRideRoot
import com.asphalt.android.model.rides.RidesData

interface JoinRidesApiService  {

   // suspend fun getAllRide() :  List<RidesData>

    suspend fun getAllRiders() : APIResult<Map<String, CreateRideRoot>>

}