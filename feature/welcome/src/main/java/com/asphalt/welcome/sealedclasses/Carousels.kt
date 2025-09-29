package com.asphalt.welcome.sealedclasses

import com.asphalt.commonui.R
import com.asphalt.welcome.data.CarouselItem

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