package com.asphalt.dashboard.composables.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.ui.BarPreview
import com.asphalt.commonui.constants.Constants
import com.asphalt.dashboard.composables.components.PlacesVisitedGraph
import com.asphalt.dashboard.composables.screens.sections.*

@Composable
fun DashBoardScreen(upcomingRideClick:()->Unit) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = Constants.DEFAULT_SCREEN_HORIZONTAL_PADDING,
                    end = Constants.DEFAULT_SCREEN_HORIZONTAL_PADDING
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Dimensions.size20)
        ) {
            CreateOrJoinRide({}, {})
            RideStatsPreview()
            UpcomingRide(upcomingRideClick)
            AdventureJourneyPreview()
            PlacesVisitedGraph()
        }

    }
}