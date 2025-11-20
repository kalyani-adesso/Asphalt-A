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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asphalt.android.location.LocationProvider
import com.asphalt.commonui.PermissionHandler
import com.asphalt.commonui.theme.PrimaryBrighterLightW75
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Composable
fun CurrentLocationMapScreen(locationProvider: LocationProvider) {


    PermissionHandler(
        permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION),
        onAllGranted = {
            MapWithCurrentLocation(locationProvider = locationProvider)
        },
        onRequest = { request ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Location permission is required to show your curret location on th map")
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = {request()}) {
                    Text(text = "Grant location permissions")
                }
            }
        }
    )
}

@Composable
fun MapWithCurrentLocation(locationProvider: LocationProvider) {

    val locations = listOf(
        LatLng(18.5204,73.8567), // pune
        LatLng(19.0760,72.8777), // mumbai
        LatLng(12.9716,77.5946), // banglore
        LatLng(28.6139,77.2090) // delhi
    )

    val coroutineScope = rememberCoroutineScope()
    var cameraPositionState = rememberCameraPositionState()

    var userLocation  by remember { mutableStateOf<LatLng?>(null) }
    var loading by remember { mutableStateOf(true) }

    // calculate distance
    val totalDistance = FloatArray(2)
    Location.distanceBetween(
        locations[0].latitude, locations[0].longitude,
        locations[1].latitude, locations[1].longitude,
        totalDistance
    )

    val distanceKm = totalDistance[0]/1000 // convert meters -> km


    LaunchedEffect(Unit) {
        loading = true
        userLocation = locationProvider.getCurrentLocation()
    }

    if (userLocation != null) {
        cameraPositionState = rememberCameraPositionState {
          //  position = CameraPosition.fromLatLngZoom(userLocation!!,18f)
            position = CameraPosition.fromLatLngZoom(locations.first(),5f)
        }
        try {
        }catch (t: Throwable) {
            Log.d("TAG", "MapWithCurrentLocation: ${t.message}")
            userLocation = null
        }
        finally {
            loading = false
        }
    }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState) {

            locations.forEach { latlng ->
                Marker(state = rememberUpdatedMarkerState(
                    position = latlng), title = "You are here ${latlng.latitude},${latlng.longitude}",
                    snippet = "Example location")
            }

            Polyline(
                points = locations,
                color = PrimaryBrighterLightW75,
                width = 8f
            )

            Log.d("TAG", "MapWithCurrentLocation: distance $distanceKm")
          //  Log.d("TAG", "MapWithCurrentLocation: ${userLocation!!.latitude},${userLocation!!.longitude}")

            if (loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text("Locading location...")
                }
            }
    }
}