package com.asphalt.dashboard.data

import com.asphalt.dashboard.sealedclasses.RideStatType

data class RideStatDataUIModel(val type: RideStatType, val value: Int = 0)
data class RideDateDto(val month: Int, val year: Int)
data class RidePerMonthDto(
    val rideDate: RideDateDto,
    val perMonthRideStatistics: PerMonthRideStatisticsDto
)

data class PerMonthRideStatisticsDto(val totalRides: Int, val locations: Int, val totalKms: Int)

