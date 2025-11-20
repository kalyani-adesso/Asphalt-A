package com.asphalt.dashboard.data

import com.asphalt.android.model.dashboard.RideDate

data class DashboardSummaryUI(
    val monthYear: RideDate,
    val totalRides: Int,
    val totalDistance: Double,
    val totalParticipantGroupRides: Int,
    val totalOrganiserGroupRides: Int,
    val uniqueEndLocations: Int
)