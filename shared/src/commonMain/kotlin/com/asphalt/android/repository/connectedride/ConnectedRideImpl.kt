package com.asphalt.android.repository.connectedride
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.connectedride.ConnectedRideDTO
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow

expect class ConnectedRideImpl {
  suspend fun getOngoingRides(rideId: String): Flow<APIResult<List<ConnectedRideDTO>>>
}