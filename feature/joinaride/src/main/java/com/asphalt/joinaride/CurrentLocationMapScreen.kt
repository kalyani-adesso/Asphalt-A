package com.asphalt.joinaride

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asphalt.android.location.LocationProvider
import com.asphalt.android.model.connectedride.ConnectedRideDTO
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.PermissionHandler
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GrayLite25
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.utils.ImageUtils
import com.asphalt.commonui.utils.ImageUtils.bitmapDescriptorFromVector
import com.asphalt.commonui.utils.Utils.generateUserColor
import com.asphalt.joinaride.locationutils.CurrentLocationUpdates
import com.asphalt.joinaride.locationutils.ShareLocation
import com.asphalt.joinaride.viewmodel.JoinRideViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun CurrentLocationMapScreen(
    locationProvider: LocationProvider,
    ridesData: RidesData,
    rideViewModel: JoinRideViewModel = koinViewModel(),
) {

    //val rideId = rideViewModel.getRideId()
    val context = LocalContext.current
    val rideId = rideViewModel.getRideId()
    Log.d("TAG", "ConnectedRideMapScreen: $rideId")
    LaunchedEffect("Test") {
        rideViewModel.getPolyLines(
            ridesData.startLatitude,
            ridesData.startLongitude,
            ridesData.endLatitude,
            ridesData.endLongitude
        )
    }

    LaunchedEffect(rideId) {
        rideId?.let {
            rideViewModel.observeRideLocations(it)
        }
    }
    val ongoingRideID by rideViewModel.ongoingRideUpdate.collectAsStateWithLifecycle()
    LaunchedEffect(ongoingRideID) {
        if (ongoingRideID.isNotEmpty()) {
            ridesData.ridesID?.let {
                CurrentLocationUpdates.startRideTracking(
                    context,
                    it,
                    ongoingRideID,
                )
            }
        }
    }

    PermissionHandler(
        permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ),
        onAllGranted = {
            MapWithCurrentLocation(
                locationProvider = locationProvider,
                ridesData = ridesData,
                rideViewModel = rideViewModel
            )
        },
        onRequest = { request ->
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Location permission is required")
                Spacer(modifier = Modifier.height(height = 12.dp))
                Button(onClick = request) {
                    Text(text = "Grant permission")
                }
            }
        }
    )
}


@Composable
fun MapWithCurrentLocation(
    locationProvider: LocationProvider,
    ridesData: RidesData,
    rideViewModel: JoinRideViewModel, androidUserVM: AndroidUserVM = koinViewModel()
) {
    val context = LocalContext.current
    val refreshScope = rememberCoroutineScope()

    val riders by rideViewModel.joinedUsers.collectAsState()
    val polyline by rideViewModel.polyLine.collectAsState()

    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var mapLoaded by remember { mutableStateOf(false) }
    val currentUserConnectedRideData by rideViewModel.currentUserConnectedRideData.collectAsStateWithLifecycle()
    var isFollowingUser by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState()
    LaunchedEffect(Unit) {
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    ridesData.startLatitude,
                    ridesData.startLongitude
                ), 12f
            )
        )

    }
    LaunchedEffect(mapLoaded) {
        if (mapLoaded) {
            val boundsBuilder = LatLngBounds.Builder()
            boundsBuilder.include(LatLng(ridesData.startLatitude, ridesData.startLongitude))
            boundsBuilder.include(LatLng(ridesData.endLatitude, ridesData.endLongitude))

            // Include user if available
            userLocation?.let { boundsBuilder.include(it) }

            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 200)
            )
        }
    }
    LaunchedEffect(currentUserConnectedRideData?.currentLat, currentUserConnectedRideData?.currentLong) {
        val lat = currentUserConnectedRideData?.currentLat ?: return@LaunchedEffect
        val lng = currentUserConnectedRideData?.currentLong ?: return@LaunchedEffect

        if (isFollowingUser) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 16f)
            )
        }
    }
    LaunchedEffect(cameraPositionState.isMoving) {
        if (cameraPositionState.isMoving) {
            if (cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) {
                isFollowingUser = false
            }
        }
    }

//    LaunchedEffect(riders) {
//        if (riders.isEmpty()) return@LaunchedEffect
//
//        val bounds = LatLngBounds.Builder()
//
//        riders.forEach {
//            bounds.include(LatLng(it.currentLat, it.currentLong))
//        }
//
//        userLocation?.let { bounds.include(it) }
//
////        cameraPositionState.animate(
////            CameraUpdateFactory.newLatLngBounds(bounds.build(), 120)
////        )
//    }

    // Get current location
    LaunchedEffect(Unit) {
        userLocation = locationProvider.getCurrentLocation()
        isLoading = false
    }

    // start
//    val start = remember(ridesData) {
//        if (ridesData.startLatitude != 0.0 && ridesData.startLongitude != 0.0)
//            LatLng(ridesData.startLatitude, ridesData.startLongitude)
//        else null
//    }
//
//    // end
//    val end = remember(ridesData) {
//        if (ridesData.endLatitude != 0.0 && ridesData.endLongitude != 0.0)
//            LatLng(ridesData.endLatitude, ridesData.endLongitude)
//        else null
//    }

    // Move camera when map + data ready
    LaunchedEffect(mapLoaded, userLocation) {
        if (!mapLoaded) return@LaunchedEffect

        when {
//            start != null && end != null -> {
//                val bounds = LatLngBounds.Builder()
//                    .include(start)
//                    .include(end)
//                    .build()
//
//                cameraPositionState.animate(
//                    CameraUpdateFactory.newLatLngBounds(bounds, 150)
//                )
//            }

            userLocation != null -> {
//                cameraPositionState.animate(
//                    CameraUpdateFactory.newLatLngZoom(userLocation!!, 14f)
//                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = { mapLoaded = true },
            properties = MapProperties(isMyLocationEnabled = false),
            uiSettings = MapUiSettings(
                tiltGesturesEnabled = true,
                rotationGesturesEnabled = true,
                scrollGesturesEnabled = true,
                zoomControlsEnabled = true,      // + / - buttons
                compassEnabled = true,           // Compass icon
                myLocationButtonEnabled = false,  // My location button
                mapToolbarEnabled = true,      // Navigation icon (open in Google Maps)

            )
        ) {
            val startMarkerState = remember(ridesData.startLatitude, ridesData.startLongitude) {
                MarkerState(
                    position = LatLng(ridesData.startLatitude, ridesData.startLongitude)
                )
            }
            val customStartPin = remember(context) {
                bitmapDescriptorFromVector(context, com.asphalt.commonui.R.drawable.ic_start_pin)
            }
            Marker(
                state = startMarkerState,
                title = "Start: ${ridesData.startLocation}",
                icon = customStartPin

                // snippet = "${rider.speedInKph} km/h"
            )
            val endMarkerState = remember(ridesData.endLatitude, ridesData.endLongitude) {
                MarkerState(
                    position = LatLng(ridesData.endLatitude, ridesData.endLongitude)
                )
            }
            val customDestinationPin = remember(context) {
                bitmapDescriptorFromVector(
                    context,
                    com.asphalt.commonui.R.drawable.ic_destination_pin
                )
            }

            Marker(
                state = endMarkerState,
                title = "Destination: ${ridesData.endLocation}",
                icon = customDestinationPin
                // snippet = "${rider.speedInKph} km/h"
            )
            // User marker
            userLocation?.let {
//                Marker(
//                    state = MarkerState(it),
//                    title = "You"
//                )
                Log.d("TAG", "MapWithCurrentLocation User: $userLocation")
            }

            // Joined riders
            riders.forEach { rider ->
                if (rider.currentLat != 0.0 && rider.currentLong != 0.0) {
                    val state = rememberUpdatedMarkerState(
                        position = LatLng(rider.currentLat, rider.currentLong)
                    )
                    val userHeading = currentUserConnectedRideData?.bearing ?: 0f
                    val correctedRotation = (userHeading.toFloat() - 35f + 360) % 360
                    if (rider.canTrack) {
                        if (rider.userID == currentUserConnectedRideData?.userID) {
                            Marker(
                                state = state,
                                flat = true,
                                title = androidUserVM.getUser(rider.userID)?.name ?: "Unknown User",
                                rotation = correctedRotation,
                                anchor = Offset(0.5f, 0.5f),
                                icon = ImageUtils.bitmapDescriptorFromVector(
                                    context,
                                    com.asphalt.commonui.R.drawable.ic_current_user
                                )
                            )
                        } else RiderMarker(ride = rider, state = state)
                    }
//                    Marker(
//                        state = MarkerState(
//                            LatLng(rider.currentLat, rider.currentLong)
//                        ),
//                        title = rider.userID,
//                        snippet = "${rider.speedInKph} km/h"
//                    )
                    Log.d("TAG", "MapWithCurrentLocation UserId: ${rider.userID}")
                    Log.d(
                        "TAG",
                        "MapWithCurrentLocation Rider: ${rider.currentLat} ${rider.currentLong}"
                    )
                }
            }

            if (polyline.isNotEmpty()) {
                Polyline(
                    points = polyline,
                    color = Color.Blue,
                    width = 8f
                )
            }
            // Start & End markers
//            start?.let {
//                Marker(
//                    state = MarkerState(it),
//                    title = ridesData.startLocation
//                )
//            }
//            end?.let {
//                Marker(
//                    state = MarkerState(it),
//                    title = ridesData.endLocation
//                )
//            }
//            // Polyline
//            if (start != null && end != null) {
//                Polyline(
//                    points = listOf(start, end),
//                    color = Color.Blue,
//                    width = 8f
//                )
//            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(
                    start = Dimensions.padding16,
                    bottom = Dimensions.padding16
                )
        ) {
            Card(
                modifier = Modifier
                    .height(Dimensions.size50)
                    .width(Dimensions.size58),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White // or use NeutralWhite
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentUserConnectedRideData?.speedInKph?.toString() ?: "0",
                        style = TypographyBold.bodyMedium,
                        fontSize = Dimensions.textSize19,
                        color = NeutralBlack
                    )
                    Text(
                        text = "kph",
                        style = TypographyMedium.bodyMedium,
                        color = GrayLite25
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        ) {
            // Spacer(modifier = Modifier.weight(1f))
            GradientButton(
                onClick = {
                    isFollowingUser = true
                    refreshScope.launch {
                        currentUserConnectedRideData?.let {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        it.currentLat,
                                        it.currentLong
                                    ), 16f
                                )
                            )
                        }
                    }

                },
                buttonRadius = Dimensions.size10,
                buttonHeight = Dimensions.radius40,
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(com.asphalt.commonui.R.drawable.ic_refresh),
                    contentDescription = ""
                )
            }
            Spacer(modifier = Modifier.width(Dimensions.size10))
            GradientButton(
                onClick = {

                    ShareLocation.openGoogleMapsNavigation(
                        context,
                        ridesData.endLatitude,
                        ridesData.endLongitude
                    )

                },
                buttonRadius = Dimensions.size10,
                buttonHeight = Dimensions.radius40,
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(com.asphalt.commonui.R.drawable.ic_navigate),
                    contentDescription = ""
                )
            }


        }
    }
}

@Composable
fun RiderMarker(
    ride: ConnectedRideDTO,
    state: MarkerState,
    androidUserVM: AndroidUserVM = koinViewModel()
) {
    val riderColor = remember(ride.userID) {
        generateUserColor(ride.userID)
    }

    val haloColor = remember(riderColor) {
        riderColor.copy(alpha = 0.25f)
    }

    val userName = remember(ride.userID) {
        androidUserVM.getUser(ride.userID)?.name ?: ""
    }

    val profileImage = remember(ride.userID) {
        ImageUtils.decodeBase64ToBitmap(androidUserVM.getUser(ride.userID)?.profilePic ?: "")
    }

    MarkerComposable(
        state = state,
        anchor = Offset(0.5f, 0.5f),
        title = userName
    ) {
        Box(
            modifier = Modifier.size(70.dp),
            contentAlignment = Alignment.Center
        ) {
            val imageModifier = Modifier
                .align(Alignment.Center)
                .offset(x = (-18).dp, y = (-18).dp) // Move top-left away from center
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .border(2.dp, riderColor, CircleShape)
            profileImage?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                Image(
                    painter = painterResource(com.asphalt.commonui.R.drawable.profile_placeholder),
                    contentDescription = null,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(haloColor, shape = CircleShape)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(riderColor, shape = CircleShape)
                        .border(2.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                )
            }
        }
    }
}