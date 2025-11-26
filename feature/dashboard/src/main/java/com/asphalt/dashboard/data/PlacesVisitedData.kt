package com.asphalt.dashboard.data

data class PlacesVisitedGraphData(
    val month: Int,
    val year: Int,
    val count: Int
)
data class PlacesVisitedGraphUIModel(
    val month: String,
    val count: Int
)

