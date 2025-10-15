package com.asphalt.dashboard.data

import kotlinx.serialization.Serializable

@Serializable
class YourRideDataModel(
    var title: String? = "",
    var place: String? = "",
    var rideStatus: String? = "",
    var date: String? = "",
    var riders: String? = ""
) {
}