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


@Composable
fun RequestPermissions(
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