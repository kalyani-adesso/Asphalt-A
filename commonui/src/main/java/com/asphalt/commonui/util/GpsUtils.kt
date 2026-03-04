package com.asphalt.commonui.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings

object GpsUtils {
    /**
     * Check if GPS / Location is enabled
     */
    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            locationManager.isLocationEnabled
        } else {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }
    }

    /**
     * Show a dialog asking the user to enable GPS.
     * Automatically opens Location Settings if GPS is still OFF.
     */
    fun showGpsDialog(activity: Activity) {
        AlertDialog.Builder(activity)
            .setTitle("GPS Required")
            .setMessage("GPS is turned off. Please enable it to continue.")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                if (!isLocationEnabled(activity)) {
                    // Open Location Settings
                    activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                } else {
                    dialog.dismiss()
                }
            }
            .show()
    }
}