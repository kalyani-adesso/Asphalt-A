package com.asphalt.android.model.createride

import kotlinx.serialization.Serializable

@Serializable
class CreateRideRoot(
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
    var endLocation: String? = null
)