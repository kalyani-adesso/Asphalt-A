package com.asphalt.joinaride

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.android.location.AndroidLocationProvider
import com.asphalt.android.location.LocationProvider
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.BannerType
import com.asphalt.commonui.R
import com.asphalt.commonui.StatusBanner
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GreenDark
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.joinaride.models.RideSummaryData
import com.asphalt.joinaride.viewmodel.JoinRideViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.absoluteValue

@Composable
fun ConnectedRideGoogleMapScreen(
    setTopAppBarState: (AppBarState) -> Unit,
    androidUserVM: AndroidUserVM = koinViewModel(),
    onClick : (RideSummaryData) -> Unit,
    locationProvider: LocationProvider,
    ridesData: RidesData,
    rideViewModel: JoinRideViewModel = koinViewModel()
    ) {
    val context = LocalContext.current
    val locationProvider = AndroidLocationProvider(context)
    var showBanner by remember {  mutableStateOf(true) }
    val currentUser = androidUserVM.userState.collectAsState(null)
    val scrollState = rememberScrollState()
    var isMapTouched by remember { mutableStateOf(false) }
    val view = LocalView.current
    val elapsedTime by rideViewModel.elapsedTime.collectAsState()

    LaunchedEffect(ridesData.ridesID) {
        rideViewModel.startRideTimer()
    }
    LaunchedEffect(ridesData.ridesID) {
        val userData = currentUser.value?.uid?.let { androidUserVM.getUser(it) }
        rideViewModel.observeRideLocations(ridesData.ridesID.toString())
        Log.d("TAG", "RidersGroupStatus userData: $userData")
    }

    val rideId = ridesData.ridesID
    Log.d("TAG", "ConnectedRideMapScreen: $rideId")

    if (rideId != null) {
//        val details = rideViewModel.getOnGoingRides(rideId ?: "")
//        Log.d("TAG", "ConnectedRideMapScreen details: $details")
    }

//    DisposableEffect(Unit) {
//        onDispose {
//            rideViewModel.stopRide()
//        }
//    }

    setTopAppBarState(
        AppBarState(
            title = stringResource(R.string.connected_ride),
            subtitle = ridesData.rideTitle?:"",
            isCenterAligned = false,
            actions = {
                RoundedBox(
                    borderColor = GreenDark,
                    borderStroke = Dimensions.padding1,
                    cornerRadius = Dimensions.size10,
                    modifier = Modifier
                        .padding(end = Dimensions.padding15)
//                        .clickable {
//                            showQueryPopup = true
//                        }
                ) {
                        //live icon
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .height(Dimensions.padding30),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_location),
                                tint = GreenDark,
                                contentDescription = null
                            )
                            Spacer(Modifier.width(Dimensions.spacing5))
                            Text(
                                "Live",
                                color = GreenDark,
                                fontSize = Dimensions.textSize12,
                                style = TypographyBold.titleMedium
                            )
                        }
                }
                // timer
                RoundedBox(
                    borderColor = PrimaryDarkerLightB75,
                    borderStroke = Dimensions.padding1,
                    cornerRadius = Dimensions.size10,
                    modifier = Modifier
                        .padding(end = Dimensions.padding15)
//                        .clickable {
//                            showQueryPopup = true
//                        }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .height(Dimensions.padding30),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_clock),
                            tint = PrimaryDarkerLightB75,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(Dimensions.spacing5))
                        Text(
                            formatTime(elapsedTime.absoluteValue),
                            color = PrimaryDarkerLightB75,
                            fontSize = Dimensions.textSize12,
                            style = TypographyBold.titleMedium
                        )
                    }
                }
            })
    )
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState, enabled = !isMapTouched),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Map container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)

                    .pointerInput(Unit) {

                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                val pressed = event.changes.any { it.pressed }
                                isMapTouched = pressed
                            }
                        }
                    }
            ) {
                CurrentLocationMapScreen(
                    locationProvider = locationProvider,
                    ridesData = ridesData,
                    rideViewModel = rideViewModel
                )
            }

            // Other UI
            RideProgress(
                androidUserVM = androidUserVM,
                viewmodel = rideViewModel,
                onClickEndRide = onClick,
                ridesData = ridesData
            )
            RidersGroupStatus(ridesData,rideViewModel, androidUserVM)
            EmergecyActions()
        }
        if (showBanner) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.align(Alignment.TopCenter)) {
                    Spacer(modifier = Modifier.height(26.dp))

                    StatusBanner(
                        type = BannerType.SUCCESS,
                        message = "Ride started! Navigation active",
                        showBanner = showBanner,
                        autoDismissMillis = 2000L,
                        {
                            showBanner = false
                        }
                    )
                }
            }
        }
    }
}

fun formatTime(totalSeconds: Long): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

@Preview(showBackground = true)
@Composable
fun ConnectedRideMapreview() {

   //ConnectedRideMap {  }
}