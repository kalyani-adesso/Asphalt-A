package com.asphalt.joinaride.message

import com.asphalt.android.model.message.MessageDTO
import com.asphalt.android.model.message.MessageRoot

fun MessageDTO.toMessageRoot(): MessageRoot {
    return MessageRoot(
        senderID = senderID,
        senderName = senderName,
        receiverID = receiverID,
        receiverName = receiverName,
        message = message,
        onGoingRideID = onGoingRideID,
        timeStamp = timeStamp,
        isRideOnGoing = isRideOnGoing
    )
}