package com.asphalt.joinaride

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
@Composable
fun RidersDummyMapScreen() {

    // Sample rider locations
    val riders = listOf(
        Rider(1, 37.7749, -122.4194),  // San Francisco
        Rider(2, 34.0522, -118.2437),  // Los Angeles
        Rider(3, 36.1699, -115.1398)   // Las Vegas
    )

    val cameraPositionState = rememberCameraPositionState()

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {

        // Add markers
        riders.forEach { rider ->
            Marker(
                state = MarkerState(
                    position = LatLng(rider.lat, rider.lng)
                ),
                title = "Rider ${rider.id}"
            )
        }
    }

    // Auto-zoom to include all riders
    LaunchedEffect(riders) {

        if (riders.isEmpty()) return@LaunchedEffect

        val boundsBuilder = LatLngBounds.Builder()

        riders.forEach { rider ->
            boundsBuilder.include(LatLng(rider.lat, rider.lng))
        }

        val bounds = boundsBuilder.build()

        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngBounds(bounds, 100),
            durationMs = 1000
        )
    }
}






data class Rider(
    val id: Int,
    val lat: Double,
    val lng: Double
)
