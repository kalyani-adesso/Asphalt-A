package com.asphalt.commonui.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat


@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    context: Context
) {


    // 1. Create a launcher for the permission request
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Permission was granted
                onPermissionGranted()
            } else {
                // Permission was denied
                onPermissionDenied()
            }
        }
    )

    // 2. Check the current permission status
    when (PackageManager.PERMISSION_GRANTED) {
        // Permission is already granted
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) -> {
            onPermissionGranted()
        }

        // Permission is NOT granted, so launch the request dialog
        else -> {
            // Use LaunchedEffect to ensure the request is only launched once
            // (or when a necessary key changes)
            LaunchedEffect(Unit) {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
}