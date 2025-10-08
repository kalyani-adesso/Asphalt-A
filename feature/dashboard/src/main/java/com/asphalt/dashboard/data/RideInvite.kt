package com.asphalt.dashboard.data

data class RideInvite(
    val inviterName: String,
    val inviterProfilePicUrl: String,
    val startPoint: String,
    val destination: String,
    val dateTime: String,
    val inviteesProfilePicUrls: List<String>
)
