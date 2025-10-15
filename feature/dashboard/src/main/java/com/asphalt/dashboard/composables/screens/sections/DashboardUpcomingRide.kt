package com.asphalt.dashboard.composables.screens.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.dashboard.composables.components.DashboardRideInviteList

@Composable
fun DashboardUpcomingRide(upcomingRideClick:()->Unit) {
    Column {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(
                stringResource(R.string.upcoming_ride),
                style = TypographyBold.headlineLarge,
                fontSize = Dimensions.textSize18
            )
            Text(
                stringResource(R.string.view_all),
                color = PrimaryDarkerLightB75,
                style = TypographyBold.headlineLarge,
                fontSize = Dimensions.textSize14,
                modifier = Modifier.clickable {
                    upcomingRideClick.invoke()
                }
            )
        }
        Spacer(Modifier.height(Dimensions.spacing19))
        DashboardRideInviteList()
    }
}

