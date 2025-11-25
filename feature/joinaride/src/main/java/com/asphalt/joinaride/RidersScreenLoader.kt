package com.asphalt.joinaride

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.asphalt.android.model.connectedride.ConnectedRideRoot
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.AppLoader
import com.asphalt.commonui.BannerType
import com.asphalt.commonui.R
import com.asphalt.commonui.StatusBanner
import com.asphalt.joinaride.viewmodel.JoinRideViewModel
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RidersScreenLoader(
    setTopAppBarState: (AppBarState) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToMapScreen : () -> Unit,
    rideViewModel: JoinRideViewModel = koinViewModel()
) {

    var isLoading by remember { mutableStateOf(true) }
    val loadingTitle = "Starting Connected Ride"
    val loadingDesc = "Initializing navigation and group cordination"
    val logoRes = R.drawable.ic_app_icon

    var showBanner by remember {  mutableStateOf(false) }

    //JoinRides(ridersList = ridersList, rideViewModel = rideViewModel)

    LaunchedEffect(Unit) {
        delay(3000)
        isLoading = false
        showBanner = true
        delay(1000)
        onNavigateToMapScreen.invoke()
    }

    setTopAppBarState(
        AppBarState(title = stringResource(R.string.connected_ride)
        )
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            Spacer(modifier = Modifier.height(30.dp))

            StatusBanner(
                type = BannerType.SUCCESS,
                message = "Successfully joined the ride!",
                showBanner = showBanner,
                onDismiss = { showBanner = false}
            )
        }
    }

    Box(
        modifier = modifier.fillMaxSize()) {
        if (isLoading) {
            AppLoader(
                logoRes = logoRes,
                title = loadingTitle,
                description = loadingDesc,
                showProgress = true
            )
        }
    }
}

@Composable
fun JoinRides(ridersList: ConnectedRideRoot,
                rideViewModel: JoinRideViewModel) {

    val request = ConnectedRideRoot(
        rideID = ridersList.rideID,
        currentLat = ridersList.currentLat,
        currentLong = ridersList.currentLong,
        dateTime = ridersList.dateTime,
        isRejoined = ridersList.isRejoined,
        status = ridersList.status
    )
    rideViewModel.joinRide(joinRide = request)

}