package com.asphalt.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.registration.RegistrationCodeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier
                ) {
                }
            }
            AsphaltTheme {
              //  AsphaltApp()
                RegistrationCodeScreen()
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    AsphaltTheme {
        AsphaltApp()
    }
}