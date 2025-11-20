package com.asphalt.dashboard.data

import com.asphalt.dashboard.sealedclasses.RideGraphLegend

data class JourneyDataUIModel(val legend: RideGraphLegend, val value: Float)


data class AdventureTimeFrameChoiceDto(
    val choiceId: Int,
    val data: AdventureJourneyDto?
)

data class AdventureJourneyDto(
    val totalRides: Int,
    val placesExplored: Int,
    val rideGroups: Int,
    val rideInvites: Int
)