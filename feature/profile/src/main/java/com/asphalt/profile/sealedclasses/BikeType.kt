package com.asphalt.profile.sealedclasses

import com.asphalt.commonui.R
import com.asphalt.profile.constants.ProfileConstants

sealed class BikeType(val typeRes: Int, val imageRes: Int, val id: Int) {

    // Existing object
    object SportsBike : BikeType(
        R.string.bike_type_sport_bike,
        R.drawable.sports_bike, ProfileConstants.SPORTS_BIKE
    )

    // New objects
    object NakedBike : BikeType(
        R.string.bike_type_naked_bike,
        R.drawable.naked_bike,
        ProfileConstants.NAKED_BIKE
    )

    object AdventureBike : BikeType(
        R.string.bike_type_adventure_bike,
        R.drawable.adventure_bike,
        ProfileConstants.ADVENTURE_BIKE
    )

    object Cruiser : BikeType(
        R.string.bike_type_cruiser,
        R.drawable.cruiser,
        ProfileConstants.CRUISER
    )

    object TouringBike : BikeType(
        R.string.bike_type_touring_bike,
        R.drawable.touring_bike,
        ProfileConstants.TOURING_BIKE
    )

    object Scooter : BikeType(
        R.string.bike_type_scooter,
        R.drawable.sports_bike,
        ProfileConstants.SCOOTER
    )

    object Electric : BikeType(
        R.string.bike_type_electric,
        R.drawable.naked_bike, ProfileConstants.ELECTRIC
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
            )
        }

        fun getBikeTypeById(id: Int): Int? {
            return getAllTypes().find { bikeType ->
                bikeType.id == id
            }?.typeRes
        }
    }
}