package com.asphalt.commonui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

@Composable
fun CombinedCameraGalleryLauncher(
    trigger: (() -> Unit) -> Unit,
    onMediaPicked: (Uri?) -> Unit
) {
    val context = LocalContext.current

    // 1. Create temporary file URI
    val tempCameraFileUri = remember {
        val tempFile = File(context.cacheDir, "temp_photo_${System.currentTimeMillis()}.jpg")
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            tempFile
        )
    }

    // 2. The main launcher for the chooser result
    val chooserLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val uri: Uri? =
                result.data?.data ?:
                if (result.resultCode == android.app.Activity.RESULT_OK) tempCameraFileUri else null

            // Check if it's a successful Gallery pick result (not the camera temp file)
            if (uri != null && uri != tempCameraFileUri && result.resultCode == android.app.Activity.RESULT_OK) {

                // A. Check for the temporary read permission grant flag on the result Intent.
                // We must have this flag to call takePersistableUriPermission without a SecurityException.
                val grantFlags = result.data!!.flags and Intent.FLAG_GRANT_READ_URI_PERMISSION

                if (grantFlags != 0) {
                    try {
                        // B. Take persistable permission for the main URI
                        context.contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )

                        // C. Persist permission for all URIs in ClipData (essential for Google Photos/multi-select)
                        result.data!!.clipData?.let { clipData ->
                            for (i in 0 until clipData.itemCount) {
                                clipData.getItemAt(i).uri?.let { clipUri ->
                                    context.contentResolver.takePersistableUriPermission(
                                        clipUri,
                                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    )
                                }
                            }
                        }
                    } catch (e: SecurityException) {
                        // Log this, but the URI might still be usable for immediate action.
                        println("Failed to persist permission for URI: ${e.message}")
                    }
                } else {
                    println("Permission Warning: Result Intent lacks FLAG_GRANT_READ_URI_PERMISSION. Cannot persist access.")
                }
            }
            onMediaPicked(uri)
        }
    )

    // 3. Helper function to create and launch the chooser (only called AFTER permission check)
    val launchChooserIfPermissionGranted: () -> Unit = {
        // A. Intent to select an image from the Gallery (Base intent)
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }

        // B. Intent to capture an image with the Camera
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, tempCameraFileUri)

            // Grant temporary read/write permissions to the camera app
            val flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            addFlags(flags)
        }

        // C. Create the Chooser Intent
        val chooserIntent = Intent.createChooser(galleryIntent, "Select Image Source").apply {
            // Add the camera intent as an option that appears side-by-side with the gallery
            putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        }

        chooserLauncher.launch(chooserIntent)
    }

    // 4. Launcher for requesting CAMERA permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // If permission is granted, launch the chooser
                launchChooserIfPermissionGranted()
            } else {
                // Permission denied
                onMediaPicked(null)
            }
        }
    )

    // 5. The function exposed to the trigger, handles permission check
    val launchChooser: () -> Unit = {
        when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
            PackageManager.PERMISSION_GRANTED -> {
                // Permission already granted, launch chooser directly
                launchChooserIfPermissionGranted()
            }
            else -> {
                // Request permission, launchChooserIfPermissionGranted will run on result
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    trigger(launchChooser)
}
