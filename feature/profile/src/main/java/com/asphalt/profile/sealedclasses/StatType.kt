package com.asphalt.profile.sealedclasses

import com.asphalt.commonui.R

sealed class StatType(val iconRes: Int, val statTypeResId: Int, val statDescriptionResId: Int) {
    object PlacesStatType : StatType(
        R.drawable.ic_location_purple,
        R.string.cities, R.string.places_explored
    )

    object RidesStatType : StatType(
        R.drawable.ic_path_orange,
        R.string.rides, R.string.total_rides
    )
}