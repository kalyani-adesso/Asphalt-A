package com.asphalt.joinaride

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.asphalt.android.location.LocationProvider
import com.asphalt.android.model.rides.RidesData
import com.asphalt.commonui.PermissionHandler
import com.asphalt.commonui.theme.PrimaryBrighterLightW75
import com.asphalt.joinaride.viewmodel.JoinRideViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CurrentLocationMapScreen(
    locationProvider: LocationProvider,
    ridesData: RidesData,
    rideViewModel: JoinRideViewModel = koinViewModel()
) {
    val rideId = rideViewModel.getRideId()

    LaunchedEffect(rideId) {
        rideId?.let {
            rideViewModel.observeRideLocations(it)
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
                Text("Location permission is required")
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = request) {
                    Text("Grant permission")
                }
            }
        }
    )
}


@Composable
fun MapWithCurrentLocation(
    locationProvider: LocationProvider,
    ridesData: RidesData,
    rideViewModel: JoinRideViewModel
) {
    val riders by rideViewModel.joinedUsers.collectAsState()

    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val cameraPositionState = rememberCameraPositionState()

    // Get current location
    LaunchedEffect(Unit) {
        userLocation = locationProvider.getCurrentLocation()
        userLocation?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 14f)
        }
        isLoading = false
    }

    Box(modifier = Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        ) {

            // 🔵 Current user marker
            userLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "You",
                    snippet = "Current location"
                )
            }

            // 🟢 Joined riders markers
            riders.forEach { rider ->
                if (rider.currentLat != 0.0 && rider.currentLong != 0.0) {

                    Log.d(
                        "MAP",
                        "Marker: ${rider.userID} ${rider.currentLat}, ${rider.currentLong}"
                    )

                    Marker(
                        state = MarkerState(
                            position = LatLng(
                                rider.currentLat,
                                rider.currentLong
                            )
                        ),
                        title = rider.userID
                    )
                }
            }

            // 🔴 Route polyline (start → end)
            val routePoints = listOf(
                LatLng(ridesData.currentLat, ridesData.currentLong),
                LatLng(ridesData.endLatitude, ridesData.endLongitude)
            )

            Polyline(
                points = routePoints,
                color = Color.Blue,
                width = 8f
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
