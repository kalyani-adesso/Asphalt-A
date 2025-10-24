package com.asphalt.dashboard.composables.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.VividRed
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.dashboard.composables.screens.sections.AdventureJourney
import com.asphalt.dashboard.composables.screens.sections.CreateOrJoinRide
import com.asphalt.dashboard.composables.screens.sections.DashboardUpcomingRide
import com.asphalt.dashboard.composables.screens.sections.PlacesVisitedGraph
import com.asphalt.dashboard.composables.screens.sections.RideStatsPerMonth
import com.asphalt.dashboard.viewmodels.NotificationViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.viewmodel.koinActivityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreen(
    upcomingRideClick: () -> Unit,
    androidUserVM: AndroidUserVM = koinViewModel(),
    notificationViewModel: NotificationViewModel = koinActivityViewModel(),
    setTopAppBarState: (AppBarState) -> Unit,
    notificationsClick: () -> Unit, creatRideClick: () -> Unit
) {
    val currentUser = androidUserVM.userState.collectAsState(null)
    val notificationList = notificationViewModel.notificationState.collectAsStateWithLifecycle()
    val hasUnreadNotifications: State<Boolean> = remember {
        derivedStateOf {
            notificationList.value.any {
                !it.isRead
            }
        }
    }
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
                    Box {
                        Icon(painter = painterResource(R.drawable.ic_notification), null)
                        if (hasUnreadNotifications.value)
                            RoundedBox(
                                backgroundColor = VividRed,
                                modifier = Modifier
                                    .size(Dimensions.size8)
                                    .align(Alignment.TopEnd)
                                    .offset(x = Dimensions.spacingNeg1),
                                cornerRadius = Dimensions.size8
                            ) {}
                    }
                }
            })
    )


    ComposeUtils.DefaultColumnRoot(

        0.dp,
        0.dp
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        CreateOrJoinRide({
            creatRideClick.invoke()
        }, {
            //TODO:Join Ride Navigation
        })
        RideStatsPerMonth()
        DashboardUpcomingRide(upcomingRideClick)
        AdventureJourney()
        PlacesVisitedGraph()
    }
}
