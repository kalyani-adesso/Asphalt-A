package com.asphalt.dashboard.composables.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.dashboard.composables.screens.sections.AdventureJourney
import com.asphalt.dashboard.composables.screens.sections.CreateOrJoinRide
import com.asphalt.dashboard.composables.screens.sections.DashboardUpcomingRide
import com.asphalt.dashboard.composables.screens.sections.PlacesVisitedGraph
import com.asphalt.dashboard.composables.screens.sections.RideStatsPerMonth
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreen(
    upcomingRideClick: () -> Unit,
    androidUserVM: AndroidUserVM = koinViewModel(),
    setTopAppBarState: (AppBarState) -> Unit,
    notificationsClick: () -> Unit
) {
    val currentUser = androidUserVM.userState.collectAsState(null)
    setTopAppBarState(
        AppBarState(
            title = stringResource(
                R.string.hello_user,
                currentUser.value?.name.toString()
            ), subtitle = "Cochin, Infopark",
            actions = {
                IconButton(onClick = {
                    notificationsClick.invoke()
                }) {
                    Icon(painter = painterResource(R.drawable.ic_notification), null)
                }
            })
    )


    ComposeUtils.DefaultColumnRootWithScroll(

        0.dp,
        0.dp
    ) {
        Spacer(modifier = Modifier.height(12.dp))
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
