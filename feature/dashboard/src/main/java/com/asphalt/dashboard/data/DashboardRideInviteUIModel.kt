package com.asphalt.dashboard.data

data class DashboardRideInviteUIModel(
    val rideID: String,
    val inviterName: String,
    val inviterProfilePicUrl: String,
    val startPoint: String,
    val destination: String,
    val dateTime: String,
    val inviteesProfilePicUrls: List<String>,
    val rideTitle:String,
    val isOrganiser:Boolean
)
