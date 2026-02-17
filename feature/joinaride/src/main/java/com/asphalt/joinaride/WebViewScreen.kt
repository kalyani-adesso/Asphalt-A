package com.asphalt.joinaride

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.google.android.gms.common.wrappers.Wrappers.packageManager

@Composable
fun WebViewMap(
    setTopAppBarState: (AppBarState) -> Unit,
    destinationLat: Double = 10.1484083,
    destinationLng: Double = 76.2249185,
    startLat: Double = 9.802147, startLng: Double = 76.601734,
    //mapurl:String = "https://www.google.com/maps/@40.7128,-74.0060,14z"
) {
    setTopAppBarState(
        AppBarState(
            title = stringResource(R.string.messages),
        )
    )
    //====================
    //var mapurl:String ="https://www.google.com/maps/dir/?api=1&origin=$startLat,$startLng&destination=$destinationLat,$destinationLng&travelmode=driving"
    val coordinatesString = "$destinationLat,$destinationLng"
    val coordinatesStringCurent = "$startLat,$startLng"
    val context = LocalContext.current
    //var mapurl:String ="https://www.google.com/maps?saddr=$origin&daddr=$destination&directionsmode=$mode"
    //var mapurl:String ="https://www.google.com/maps?daddr=$coordinatesString&directionsmode=d"
    //var mapurl:String ="https://www.google.com/maps?saddr=$coordinatesStringCurent&daddr=$coordinatesString&directionsmode=d"
    //var mapurl:String ="https://www.google.com/maps?saddr=$coordinatesStringCurent&daddr=$coordinatesString&directionsmode=d"
    val mapurl: String =
        "https://www.google.com/maps/dir/?api=1&origin=$coordinatesStringCurent&destination=$coordinatesString&travelmode=driving"

//==============
    val gmmIntentUri = Uri.parse(
        "https://www.google.com/maps/dir/?api=1&origin=My+Location&destination=$destinationLat,$destinationLng&travelmode=driving"
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                //val gmmIntentUri = Uri.parse("google.navigation:q=$destinationLat,$destinationLng&mode=d")

            }) {


        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    // Crucial: Maps requires JavaScript
                    settings.javaScriptEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.setGeolocationEnabled(true)
                    webViewClient = WebViewClient()
                    webChromeClient = object : WebChromeClient() {
                        override fun onGeolocationPermissionsShowPrompt(
                            origin: String,
                            callback: GeolocationPermissions.Callback
                        ) {
                            // This tells the WebView to allow location access
                            callback.invoke(origin, true, false)
                        }
                    }

                    loadUrl(mapurl)
                }
            },
            update = { webView ->
                webView.loadUrl(mapurl)
            }
        )


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
        Button(
            modifier = Modifier
                , onClick = {
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                // Check if Maps app is available, then start
                if (mapIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(mapIntent)
                } else {
                    // Fallback: open in browser
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        gmmIntentUri
                    )
                    context.startActivity(browserIntent)
                }
            }) { Text("Navigation", color = Color.White) }
    }
}

@Preview
@Composable
fun PreviewM(){
    WebViewMap({})
}