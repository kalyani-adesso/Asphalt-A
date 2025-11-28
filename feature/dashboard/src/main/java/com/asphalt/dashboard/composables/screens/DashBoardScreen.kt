package com.asphalt.dashboard.composables.screens

import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralTaupe20
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.commonui.utils.RequestLocationPermission
import com.asphalt.commonui.utils.Utils
import com.asphalt.dashboard.composables.screens.sections.AdventureJourney
import com.asphalt.dashboard.composables.screens.sections.CreateOrJoinRide
import com.asphalt.dashboard.composables.screens.sections.DashboardUpcomingRide
import com.asphalt.dashboard.composables.screens.sections.PlacesVisitedGraph
import com.asphalt.dashboard.composables.screens.sections.RideStatsPerMonth
import com.asphalt.dashboard.viewmodels.DashboardRideSummaryVM
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreen(
    upcomingRideClick: () -> Unit,
    androidUserVM: AndroidUserVM = koinViewModel(),
    setTopAppBarState: (AppBarState) -> Unit,
    creatRideClick: () -> Unit,
    joinRideClick: () -> Unit,
    dashboardRideSummaryVM: DashboardRideSummaryVM = koinViewModel()
) {

    val context = LocalContext.current
    var locationStatus by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    RequestLocationPermission(
        onPermissionGranted = {
            scope.launch {
                locationStatus = ""
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            val lat = location.latitude
                            val lon = location.longitude

                            locationStatus = Utils.getLocationRegion(context, lat, lon)
                        } else {
                            locationStatus = ""
                        }
                    }
            }
        },
        onPermissionDenied = {
            locationStatus = ""
        }, context
    )
    val currentUser = androidUserVM.userState.collectAsState(null)

    val helloUser = stringResource(R.string.hello)
    LaunchedEffect(currentUser, locationStatus) {
        val userData = currentUser.value?.uid?.let { androidUserVM.getUser(it) }

        setTopAppBarState(
            AppBarState(
//                subtitle = locationStatus
                dashboardHeader = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
//                                vertical = Dimensions.padding30,
                                horizontal = Dimensions.padding20
                            ),
                        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing20),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RoundedBox(
                            modifier = Modifier.size(Dimensions.size71),
                            borderStroke = Dimensions.size4,
                            borderColor = NeutralTaupe20, cornerRadius = Dimensions.size71
                        ) {

                            CircularNetworkImage(
                                modifier = Modifier
                                    .align(Alignment.Center),
                                imageUrl = userData?.profilePic ?: "", size = Dimensions.size63
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(Dimensions.spacing5)) {
                            Text(helloUser, fontFamily = FontFamily(Font(R.font.klavika_light)), fontSize = Dimensions.textSize16, lineHeight = 18.sp,)
                            Text(
                                currentUser.value?.name ?: "", style = TypographyBold.bodyMedium,
                                fontSize = Dimensions.textSize28
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(Dimensions.size4)) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_rider_level),
                                    contentDescription = null,
                                    tint = Color.Unspecified
                                )
                                Text(
                                    "Level 4 - Rider",
                                    style = Typography.bodyMedium,
                                    fontSize = Dimensions.textSize16
                                )
                            }


                        }
                    }
                }
            )
        )
    }
    LaunchedEffect(Unit) {
        dashboardRideSummaryVM.getRidesData()
    }
    val dashboardSummary = dashboardRideSummaryVM.dashboardSummary.collectAsStateWithLifecycle()


    ComposeUtils.DefaultColumnRoot(

        0.dp,
        0.dp
    ) {
        CreateOrJoinRide({
            creatRideClick.invoke()
        }, {
            joinRideClick.invoke()
        })
        RideStatsPerMonth(dashboardSummary.value)
        DashboardUpcomingRide(upcomingRideClick)
        AdventureJourney(dashboardSummary.value)
        PlacesVisitedGraph(dashboardSummary.value)
    }
}
