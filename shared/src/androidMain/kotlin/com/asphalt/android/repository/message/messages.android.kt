package com.asphalt.android.repository.message

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.message.MessageDTO
import kotlinx.coroutines.flow.Flow

actual class MessageImpl {
    actual suspend fun receiveMessage(rideId: String): Flow<APIResult<List<MessageDTO>>> {
        TODO("Not yet implemented")
    }
}