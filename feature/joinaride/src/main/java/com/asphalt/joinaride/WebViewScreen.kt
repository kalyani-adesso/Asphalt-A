package com.asphalt.joinaride

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.google.android.gms.common.wrappers.Wrappers.packageManager

@Composable
fun WebViewMap(
    setTopAppBarState: (AppBarState) -> Unit,
    destinationLat: Double =10.1484083,
    destinationLng: Double = 76.2249185
) {
    setTopAppBarState(
        AppBarState(
            title = stringResource(R.string.messages),
        )
    )
    val context = LocalContext.current
    val gmmIntentUri = Uri.parse(
        "https://www.google.com/maps/dir/?api=1&origin=My+Location&destination=$destinationLat,$destinationLng&travelmode=driving"
    )
    //val gmmIntentUri = Uri.parse("google.navigation:q=$destinationLat,$destinationLng&mode=d")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    // Check if Maps app is available, then start
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        // Fallback: open in browser
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$destinationLat,$destinationLng&travelmode=driving")
        )
        context.startActivity(browserIntent)
    }

    // Using the official Google Maps Directions URL
   /* val url = remember(destinationLat, destinationLng) {
        "https://www.google.com/maps/dir/?api=1&destination=$destinationLat,$destinationLng&travelmode=driving"
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(), // Use the modifier here for full screen
        factory = { context ->
            WebView(context).apply {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    // Support zooming
                    setSupportZoom(true)
                    builtInZoomControls = true
                    displayZoomControls = false
                }
                webViewClient = WebViewClient()
                loadUrl(url)
            }
        },
        update = { webView ->
            // Only reload if the URL actually changed to prevent flickering
            if (webView.url != url) {
                webView.loadUrl(url)
            }
        }
    )*/
}