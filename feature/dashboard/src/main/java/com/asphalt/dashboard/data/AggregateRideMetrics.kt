package com.asphalt.dashboard.data

data class AggregatedRideMetrics(
    val totalRides: Int,
    val totalDistance: Double,
    val totalParticipantGroupRides: Int,
    val totalOrganiserGroupRides: Int,
    val uniqueEndLocations: Int
)