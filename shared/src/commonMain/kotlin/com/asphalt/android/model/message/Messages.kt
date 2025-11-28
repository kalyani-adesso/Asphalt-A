package com.asphalt.android.model.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageRoot(
    @SerialName("senderID")
    val senderID: String? = null,

    @SerialName("senderName")
    val senderName: String? = null,

    @SerialName("receiverID")
    val receiverID: String? = null,

    @SerialName("receiverName")
    val receiverName: String? = null,

    @SerialName("message")
    val message: String? = null,

    @SerialName("onGoingRideID")
    val onGoingRideID: String? = null,

    @SerialName("timeStamp")
    val timeStamp: Long? = null,

    @SerialName("isRideOnGoing")
    val isRideOnGoing: Boolean? = null
)

data class MessageDTO(
    val id: String,
    val senderID: String,
    val senderName: String,
    val receiverID: String,
    val receiverName: String,
    val message: String,
    val onGoingRideID: String,
    val timeStamp: Long,
    val isRideOnGoing: Boolean
)