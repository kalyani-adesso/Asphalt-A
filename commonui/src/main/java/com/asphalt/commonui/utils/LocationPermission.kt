package com.asphalt.commonui.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat


//@Composable
//fun RequestLocationPermission(
//    onPermissionGranted: () -> Unit,
//    onPermissionDenied: () -> Unit,
//    context: Context
//)
@Composable

fun RequestPermission(

    onPermissionsGranted: () -> Unit,

    onPermissionsDenied: (deniedPermissions: List<String>) -> Unit,

    context: Context

) {

    // 1. Determine which permissions are required

    val permissions = mutableListOf<String>()

    permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)

    // Only add POST_NOTIFICATIONS for Android 13+

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

        permissions.add(Manifest.permission.POST_NOTIFICATIONS)

    }

    // 2. Create a launcher for multiple permission requests

    val permissionLauncher = rememberLauncherForActivityResult(

        contract = ActivityResultContracts.RequestMultiplePermissions(),

        onResult = { results ->

            val denied = results.filter { !it.value }.map { it.key }

            if (denied.isEmpty()) {

                onPermissionsGranted()

            } else {

                onPermissionsDenied(denied)

            }

        }

    )

    // 3. Check which permissions are already granted

    val allGranted = permissions.all { perm ->

        ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED

    }

    if (allGranted) {

        // All required permissions are already granted

        onPermissionsGranted()

    } else {

        // Launch the permission request dialog

        LaunchedEffect(Unit) {

            permissionLauncher.launch(permissions.toTypedArray())

        }

    }

}

//

//{
//
//
//    // 1. Create a launcher for the permission request
//    val locationPermissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission(),
//        onResult = { isGranted ->
//            if (isGranted) {
//                // Permission was granted
//                onPermissionGranted()
//            } else {
//                // Permission was denied
//                onPermissionDenied()
//            }
//        }
//    )
//
//    // 2. Check the current permission status
//    when (PackageManager.PERMISSION_GRANTED) {
//        // Permission is already granted
//        ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) -> {
//            onPermissionGranted()
//        }
//
//        // Permission is NOT granted, so launch the request dialog
//        else -> {
//            // Use LaunchedEffect to ensure the request is only launched once
//            // (or when a necessary key changes)
//            LaunchedEffect(Unit) {
//                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//            }
//        }
//    }
//}