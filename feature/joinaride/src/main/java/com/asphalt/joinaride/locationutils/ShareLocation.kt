package com.asphalt.joinaride.locationutils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices

object ShareLocation {
    fun shareCurrentLocation(context: Context) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)


        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(context, "Location permission not granted", Toast.LENGTH_SHORT).show()
            return
        }


        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude

                val message = "My current location: https://maps.google.com/?q=$latitude,$longitude"

                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, message)
                    type = "text/plain"
                }

                val chooser = Intent.createChooser(intent, "Share location via")
                context.startActivity(chooser)
            } else {
                Toast.makeText(context, "Unable to get location", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(context, "Location failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun openGoogleMapsNavigation(
        context: Context,
        destinationLat: Double,
        destinationLng: Double
    ) {
        val gmmIntentUri = Uri.parse(
            "https://www.google.com/maps/dir/?api=1&destination=$destinationLat,$destinationLng&travelmode=driving"
        )

        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage("com.google.android.apps.maps")
        }

        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            val browserIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            context.startActivity(browserIntent)
        }
    }
}