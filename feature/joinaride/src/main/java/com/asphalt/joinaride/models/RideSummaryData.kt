package com.asphalt.joinaride.models

import kotlinx.serialization.Serializable

@Serializable
data class RideSummaryData(val startDateTime: Long, val distanceTravelled: Double, val noOfRiders: Int)
