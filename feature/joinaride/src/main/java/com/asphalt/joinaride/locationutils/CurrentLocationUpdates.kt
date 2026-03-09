package com.asphalt.joinaride.locationutils

import android.content.Context
import android.content.Intent
import android.os.Build
import com.asphalt.joinaride.services.CurrentLocationService

object CurrentLocationUpdates {

    fun startRideTracking(context: Context, rideId: String, ongoingRideId: String) {
        val intent = Intent(context, CurrentLocationService::class.java).apply {
            putExtra("RIDE_ID", rideId)
            putExtra("ONGOING_RIDE_ID", ongoingRideId)
        }

        // Foreground services require this specific start method
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    fun stopRideTracking(context: Context) {
        // This triggers the Service's onDestroy() method
        val intent = Intent(context, CurrentLocationService::class.java)
        context.stopService(intent)
    }
}