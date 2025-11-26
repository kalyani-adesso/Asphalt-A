package com.asphalt.commonui.utils

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberImagePicker(
    maxItems: Int = 10
): Pair<List<Uri>, () -> Unit> {

    val context = LocalContext.current
    var selectedUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems),
        onResult = { uris ->

            // Release old permissions
            selectedUris.forEach { oldUri ->
                runCatching {
                    context.contentResolver.releasePersistableUriPermission(
                        oldUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
            }

            selectedUris = uris

            // Request persistent permissions for new URIs
            uris.forEach { uri ->
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: SecurityException) {
                    Log.e("ImagePicker", "Failed to persist permission for $uri", e)
                }
            }
        }
    )

    // Function to launch gallery
    val openGallery = remember {
        {
            launcher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    return Pair(selectedUris, openGallery)
}