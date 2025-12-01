package com.asphalt.dashboard.sealedclasses

import androidx.compose.ui.graphics.Color
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.BrightTeal
import com.asphalt.commonui.theme.PaleLavender
import com.asphalt.commonui.theme.PrimaryBrighterLightW50
import com.asphalt.commonui.theme.PrimaryBrighterLightW60

sealed class RideStatType(val iconRes: Int, val bgColor: Color, val descriptionRes: Int) {
    object TotalRides :
        RideStatType(R.drawable.ic_path, PaleLavender, R.string.total_rides)

    object Locations :
        RideStatType(R.drawable.ic_location, PaleLavender, R.string.locations)

    object TotalKms : RideStatType(R.drawable.ic_navigate, PaleLavender, R.string.total_kms)
}