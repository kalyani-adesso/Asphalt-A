package com.asphalt.joinaride.message

import kotlinx.serialization.Serializable

@Serializable
data class MessageDriverUiState(
    val driverName: String = "Sooraj Rajan",
    val delayMinutes: Int = 15,
    val customMessage: String = "",
    val isSending: Boolean = false
)
