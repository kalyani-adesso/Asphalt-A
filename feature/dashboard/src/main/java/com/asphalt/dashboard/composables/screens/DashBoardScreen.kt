package com.asphalt.dashboard.composables.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.dashboard.composables.components.PlacesVisitedGraph
import com.asphalt.dashboard.composables.screens.sections.AdventureJourney
import com.asphalt.dashboard.composables.screens.sections.CreateOrJoinRide
import com.asphalt.dashboard.composables.screens.sections.DashboardUpcomingRide

import com.asphalt.dashboard.composables.screens.sections.RideStatsPerMonth
import com.asphalt.dashboard.viewmodels.DashboardViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreen(upcomingRideClick:()->Unit,dashboardViewModel: DashboardViewModel = koinViewModel()) {
    val currentUser = dashboardViewModel.user.collectAsState(null)

    Scaffold(topBar = {
        TopAppBar(title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    currentUser.value?.name ?: ""
                )
            }

        })
    }) { paddingValues ->
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
            CreateOrJoinRide({
                //TODO:Create Ride Navigation
            }, {
                //TODO:Join Ride Navigation
            })
            RideStatsPerMonth()
            DashboardUpcomingRide(upcomingRideClick)
            AdventureJourney()
            PlacesVisitedGraph()
        }

    }
}