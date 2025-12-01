package com.asphalt.joinaride

import android.util.Log
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.android.location.AndroidLocationProvider
import com.asphalt.android.location.LocationProvider
import com.asphalt.android.model.connectedride.ConnectedRideRoot
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.viewmodel.joinridevm.RidersGroupViewModel
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
import com.asphalt.joinaride.viewmodel.JoinRideViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ConnectedRideMapScreen(
    setTopAppBarState: (AppBarState) -> Unit,
    viewModel: RidersGroupViewModel = koinViewModel(),
    androidUserVM: AndroidUserVM = koinViewModel(),
    onClick : () -> Unit,
    locationProvider: LocationProvider,
    ridesData: RidesData,
    rideViewModel: JoinRideViewModel = koinViewModel()
    ) {
    val context = LocalContext.current
    val locationProvider = AndroidLocationProvider(context)
    var showBanner by remember {  mutableStateOf(true) }

    val rideId = rideViewModel.getRideId()
    Log.d("TAG", "ConnectedRideMapScreen: $rideId")

    if (rideId != null) {
        val details = rideViewModel.getOnGoingRides(rideId ?: "")
        Log.d("TAG", "ConnectedRideMapScreen details: $details")
    }

    setTopAppBarState(
        AppBarState(
            title = stringResource(R.string.connected_ride),
            subtitle = "Weekend Coast Ride",
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
                        //live
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
                                stringResource(R.string.live).uppercase(),
                                color = GreenDark,
                                fontSize = Dimensions.textSize12,
                                style = TypographyBold.titleMedium
                            )
                        }
                }
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
                            stringResource(R.string.timer).uppercase(),
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
        ComposeUtils.DefaultColumnRoot(
            topPadding = Dimensions.padding,
            bottomPadding = Dimensions.padding,
            isScrollable = true
        ) {
            Column(
                modifier = Modifier.height(400.dp)
            ) {
                CurrentLocationMapScreen(locationProvider=locationProvider)
            }
            RideProgress(androidUserVM = androidUserVM,
                onClickEndRide = onClick,
                ridesData = ridesData)
            RidersGroupStatus(viewModel)
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

@Preview(showBackground = true)
@Composable
fun ConnectedRideMapreview() {

   //ConnectedRideMap {  }
}