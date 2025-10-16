package com.asphalt.dashboard.composables.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.dashboard.composables.screens.sections.AdventureJourney
import com.asphalt.dashboard.composables.screens.sections.CreateOrJoinRide
import com.asphalt.dashboard.composables.screens.sections.DashboardUpcomingRide
import com.asphalt.dashboard.composables.screens.sections.PlacesVisitedGraph

import com.asphalt.dashboard.composables.screens.sections.RideStatsPerMonth
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreen(upcomingRideClick: () -> Unit,
                    androidUserVM: AndroidUserVM = koinViewModel(),
                    paadingValues: PaddingValues) {
    val currentUser = androidUserVM.userState.collectAsState(null)

            ComposeUtils.DefaultColumnRootWithScroll(

                paadingValues.calculateTopPadding(),
                0.dp
            ) {
                Spacer(modifier = Modifier.height(25.dp))
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
