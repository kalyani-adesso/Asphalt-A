package com.asphalt.getstarted.sealedclasses

import com.asphalt.getstarted.data.CarouselItem
import com.asphalt.getstarted.R

sealed class Carousels(val carouselItem: CarouselItem) {
    object JoyRideCarousel : Carousels(
        CarouselItem(
            R.drawable.joy_riding,
            R.string.joy_riding,
            R.string.joy_riding_desc
        )
    )

    object CommunityFeatureCarousel : Carousels(
        CarouselItem(
            R.drawable.community_features,
            R.string.community_features,
            R.string.community_features_desc
        )
    )

    object RideTogetherCarousel : Carousels(
        CarouselItem(
            R.drawable.ride_together_safely,
            R.string.ride_together_safely,
            R.string.ride_together_safely_desc
        )
    )
}