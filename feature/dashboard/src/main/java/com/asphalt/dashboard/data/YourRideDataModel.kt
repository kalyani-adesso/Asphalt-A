package com.asphalt.dashboard.data

import kotlinx.serialization.Serializable

@Serializable
class YourRideDataModel(
    var ridesId: String? = null,
    var title: String? = "",
    var place: String? = "",
    var rideStatus: String? = "",
    var date: String? = "",
    var riders: Int? = 0,
    var createdBy: String? = null,
    var createdUSerName: String? = null,
    var profileImageUrl: String? = null,
    var startDate: Long? = null,
    var startTime: String? = null,
    var endDateDisplay: String? = "",
    var endDate: Long? = null,
    var endTime: String? = null,

    ) {
}