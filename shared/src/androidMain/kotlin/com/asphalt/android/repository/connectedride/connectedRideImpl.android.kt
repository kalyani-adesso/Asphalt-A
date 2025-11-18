package com.asphalt.android.repository.connectedride

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.connectedride.ConnectedRideDTO
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow

actual class ConnectedRideImpl {
    actual suspend fun  getOngoingRides(rideId: String): Flow<APIResult<List<ConnectedRideDTO>>> {
        TODO("Not yet implemented")
    }
}