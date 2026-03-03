package com.asphalt.joinaride.message

import kotlinx.serialization.Serializable

@Serializable
data class MessageDriverUiState(
    val driverName: String,
    val delayMinutes: Int,
    val customMessage: String,
    val isSending: Boolean = false
)
