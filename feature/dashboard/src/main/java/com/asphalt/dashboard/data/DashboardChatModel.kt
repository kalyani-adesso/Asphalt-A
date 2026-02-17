package com.asphalt.dashboard.data

data class DashboardChatModel(
    val rideID: String,
    val isGroup: Boolean,
    val organiserID: String,
    val title: String = "",
    val members: List<String>,
    val isSoloRide: Boolean = false
)
