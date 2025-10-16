package com.asphalt.profile.sealedclasses

import com.asphalt.commonui.R

sealed class BikeType(val typeRes: Int, val descriptionRes: Int, val imageRes: Int) {

    // Existing object
    object SportsBike : BikeType(
        R.string.bike_type_sport_bike,
        R.string.sports_bike_description,
        R.drawable.sports_bike
    )

    // New objects
    object NakedBike : BikeType(
        R.string.bike_type_naked_bike,
        R.string.naked_bike_description,
        R.drawable.sports_bike // Placeholder drawable
    )

    object AdventureBike : BikeType(
        R.string.bike_type_adventure_bike,
        R.string.adventure_bike_description,
        R.drawable.sports_bike // Placeholder drawable
    )

    object Cruiser : BikeType(
        R.string.bike_type_cruiser,
        R.string.cruiser_description,
        R.drawable.sports_bike // Placeholder drawable
    )

    object TouringBike : BikeType(
        R.string.bike_type_touring_bike,
        R.string.touring_bike_description,
        R.drawable.sports_bike // Placeholder drawable
    )

    object Scooter : BikeType(
        R.string.bike_type_scooter,
        R.string.scooter_description,
        R.drawable.sports_bike // Placeholder drawable
    )

    object Electric : BikeType(
        R.string.bike_type_electric,
        R.string.electric_description,
        R.drawable.sports_bike // Placeholder drawable
    )

    object Other : BikeType(
        R.string.bike_type_other,
        R.string.other_description,
        R.drawable.sports_bike // Placeholder drawable
    )

    companion object {
        fun getAllTypes(): List<BikeType> {
            return listOf(
                SportsBike,
                NakedBike,
                AdventureBike,
                Cruiser,
                TouringBike,
                Scooter,
                Electric,
                Other
            )
        }
    }
}