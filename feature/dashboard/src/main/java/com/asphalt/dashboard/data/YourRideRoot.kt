package com.asphalt.dashboard.data

import kotlinx.serialization.Serializable

@Serializable
data class YourRideRoot(
    var upcoming: ArrayList<YourRideDataModel> = ArrayList(),
    var history: ArrayList<YourRideDataModel> = ArrayList(),
    var invite: ArrayList<YourRideDataModel> = ArrayList()
)