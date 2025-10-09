package com.asphalt.dashboard.sealedclasses

import androidx.compose.ui.graphics.Color
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.BrightTeal
import com.asphalt.commonui.theme.MagentaDeep
import com.asphalt.commonui.theme.PrimaryBrighterLightW50
import com.asphalt.commonui.theme.PrimaryBrighterLightW60
import com.asphalt.commonui.theme.PrimaryBrighterLightW90

sealed class RideGraphLegend(val color: Color,val nameRes: Int){
    object TotalRides: RideGraphLegend(BrightTeal, R.string.total_rides)
    object DistanceCovered: RideGraphLegend(PrimaryBrighterLightW50, R.string.distance_covered)
    object PlacesExplored: RideGraphLegend(MagentaDeep, R.string.places_explored)
    object RideGroups: RideGraphLegend(PrimaryBrighterLightW90, R.string.ride_groups)
    object RideInvites: RideGraphLegend(PrimaryBrighterLightW60, R.string.ride_invites)
}