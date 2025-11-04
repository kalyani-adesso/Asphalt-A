package com.asphalt.commonui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun PermissionHandler(
    permissions : Array<String>,
    onAllGranted: @Composable () -> Unit,
    onRequest : @Composable (request : () -> Unit) -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity

    // simple aggregate check
    fun allGranted() : Boolean =
        permissions.all {
            ContextCompat.checkSelfPermission(context,it) ==
                    PackageManager.PERMISSION_GRANTED
        }

    var granted by remember { mutableStateOf(allGranted()) }
    var showRationale by remember {
        mutableStateOf(
            permissions.any {
                ActivityCompat.shouldShowRequestPermissionRationale(activity,it)
            }
        )
    }
    //launcher for multiple permussisons(preferred more than one)
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { results ->

        granted = results.values.all { it }
        //recompute reationale after result(some may be permanetly denied)
        showRationale = permissions.any {
            ActivityCompat.shouldShowRequestPermissionRationale(activity,it)
        }
    }
    if (granted) {
        onAllGranted()
        return
    }

    // if permissions denied
    val permanentlyDenied = !granted && !showRationale

    if (permanentlyDenied) {
        //show a dialog telling to open settings
        AlertDialog(
            onDismissRequest = {/*Do nothing"*/},
            title = { Text(text = "Permission Required") },
            text = { Text("Permissions was denied. Please open app settings & grant permission.")},
            confirmButton = {
                Button(
                    onClick = {
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package",context.packageName,null)
                        ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
                        context.startActivity(intent)
                    }
                ) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                Button(onClick = {
                    // may close screen or show alernative UI
                }) {
                    Text("Close")
                }
            }
        )
        return
    }
    // show deny UI with a button to requrest permissions
    onRequest {
        launcher.launch(permissions)
    }
}