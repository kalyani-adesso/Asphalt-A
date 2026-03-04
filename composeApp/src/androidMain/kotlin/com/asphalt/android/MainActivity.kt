package com.asphalt.android

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation3.ui.DialogSceneStrategy.Companion.dialog
import com.asphalt.android.navigation.NavigationRoot
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.profile.screens.ProfileScreen

class MainActivity : ComponentActivity() {
    private lateinit var locationManager: LocationManager

    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
                checkGpsAndShowDialog()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Check once when activity opens
        checkGpsAndShowDialog()

        setContent {
            AsphaltTheme {
                Surface {
                    NavigationRoot()
//                    ProfileScreen()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(
            locationReceiver,
            IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        )
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(locationReceiver)
    }

    private fun isLocationEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            locationManager.isLocationEnabled
        } else {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }
    }


    private fun checkGpsAndShowDialog() {
        if (!isLocationEnabled()) {
            showGpsDialog()
        }
    }


    private fun showGpsDialog() {
        AlertDialog.Builder(this)
            .setTitle("GPS Required")
            .setMessage("GPS is turned off. Please enable it to continue.")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                //dialog.dismiss()
                if (!isLocationEnabled()) {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                } else {
                    dialog.dismiss()
                }

            }
            .show()
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    AsphaltTheme {
        // AsphaltApp()
        NavigationRoot()
    }
}