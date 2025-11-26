package com.asphalt.createride.model

data class CreateRideModel(
    var rideType: String? = null,
    var rideTitle: String? = null,
    var description: String? = null,
    var dateMils: Long? = null,
    var dateString: String? = null,
    var hour: Int? = null,
    var mins: Int? = null,
    var isAm: Boolean = false,
    var displayTime: String? = null,
    var startLocation: String? = null,
    var endLocation: String? = null,

    var startLat: Double? = 0.0,
    var startLon: Double? = 0.0,
    var endLat: Double? = 0.0,
    var endLon: Double? = 0.0,
    var endDateString: String? = null,
    var endHour: Int? = null,
    var endMins: Int? = null,
    var endDateMils: Long? = null,
    var isEndAm: Boolean = false,
    var endDisplayTime: String? = null,

)