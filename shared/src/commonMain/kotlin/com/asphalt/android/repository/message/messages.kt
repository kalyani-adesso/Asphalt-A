package com.asphalt.android.repository.message

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.message.MessageDTO
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow

expect class MessageImpl {
    suspend fun receiveMessage(rideId: String): Flow<APIResult<List<MessageDTO>>>
}