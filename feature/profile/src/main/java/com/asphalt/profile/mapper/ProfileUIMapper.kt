package com.asphalt.profile.mapper

import com.asphalt.android.model.profile.BikeDomain
import com.asphalt.profile.data.VehicleData

fun BikeDomain.toBikeUIModel(): VehicleData {
    return VehicleData(
        id = bikeId,
        type = type, make = make, model = model
    )
}

fun List<BikeDomain>.toBikeListUIModel(): List<VehicleData> {
    return map {
        it.toBikeUIModel()
    }
}