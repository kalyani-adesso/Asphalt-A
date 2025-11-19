package com.asphalt.android.model.dashboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class DashboardDTO(
    @SerialName("ride_ID")
    val rideID: String? = null,

    @SerialName("ride_Distance")
    val rideDistance: Double? = null,

    @SerialName("is_GroupRide")
    val isGroupRide: Boolean? = false,

    @SerialName("start_Location")
    val startLocation: String? = null,

    @SerialName("end_Location")
    val endLocation: String? = null,

    @SerialName("is_OrganiserGroupRide")
    val isOrganiserGroupRide: Boolean? = false,

    @SerialName("is_ParticipantGroupRide")
    val isParticipantGroupRide: Boolean? = false,

    @SerialName("endRide_Date")
    val endRideDate: String? = null,
)
data class DashboardDomain(val monthYear: RideDate,val perMonthData: List<PerMonthRideDataDomain>)
data class PerMonthRideDataDomain (
    var ridesID: String? = null,
    var rideDistance: Double? = 0.0,
    var isGroupRide: Boolean? = false,
    var startLocation: String? = null,
    var endLocation: String? = null,
    var isOrganiserGroupRide: Boolean? = false,
    var isParticipantGroupRide: Boolean? = false,
    var endRideDate: String? = null
)

data class RideDate(val month: Int, val year: Int)

