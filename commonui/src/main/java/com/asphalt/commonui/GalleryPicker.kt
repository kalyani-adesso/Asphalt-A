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

    val tempCameraFileUri = remember {
        val tempFile = File(context.cacheDir, "temp_photo_${System.currentTimeMillis()}.jpg")
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            tempFile
        )
    }

    val chooserLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val uri: Uri? =
                result.data?.data ?:
                if (result.resultCode == android.app.Activity.RESULT_OK) tempCameraFileUri else null

            if (uri != null && uri != tempCameraFileUri && result.resultCode == android.app.Activity.RESULT_OK) {

                val grantFlags = result.data!!.flags and Intent.FLAG_GRANT_READ_URI_PERMISSION

                if (grantFlags != 0) {
                    try {
                        context.contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )

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
                        println("Failed to persist permission for URI: ${e.message}")
                    }
                } else {
                    println("Permission Warning: Result Intent lacks FLAG_GRANT_READ_URI_PERMISSION. Cannot persist access.")
                }
            }
            onMediaPicked(uri)
        }
    )

    val launchChooserIfPermissionGranted: () -> Unit = {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, tempCameraFileUri)

            val flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            addFlags(flags)
        }

        val chooserIntent = Intent.createChooser(galleryIntent, "Select Image Source").apply {
            putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        }

        chooserLauncher.launch(chooserIntent)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                launchChooserIfPermissionGranted()
            } else {
                onMediaPicked(null)
            }
        }
    )

    val launchChooser: () -> Unit = {
        when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
            PackageManager.PERMISSION_GRANTED -> {
                launchChooserIfPermissionGranted()
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    trigger(launchChooser)
}
