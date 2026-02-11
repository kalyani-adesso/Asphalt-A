package com.asphalt.chat.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatParamsModel(var rideId: String, var members: List<String>, var title: String) {

}
