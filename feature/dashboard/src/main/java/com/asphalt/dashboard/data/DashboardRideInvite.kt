package com.asphalt.dashboard.data

data class DashboardRideInvite(
    val inviterName: String,
    val inviterProfilePicUrl: String,
    val startPoint: String,
    val destination: String,
    val dateTime: String,
    val inviteesProfilePicUrls: List<String>
)
