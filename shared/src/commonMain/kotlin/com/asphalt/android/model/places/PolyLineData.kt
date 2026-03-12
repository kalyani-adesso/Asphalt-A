package com.asphalt.android.model.places

import kotlinx.serialization.Serializable

@Serializable
data class OSRMResponse(
    val routes: List<Route>
)

@Serializable
data class Route(
    val geometry: Geometry
)

@Serializable
data class Geometry(
    val coordinates: List<List<Double>>,
    val type: String
)