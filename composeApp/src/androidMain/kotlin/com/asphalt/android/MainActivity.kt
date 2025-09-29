package com.asphalt.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.asphalt.registration.RegistrationCodeScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.asphalt.commonui.theme.AsphaltTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
           // App()
            MaterialTheme {
                Surface(
                    modifier = Modifier
                ) {
                }
                RegistrationCodeScreen()
            }
            AsphaltTheme {
                AsphaltApp()
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    //App()
}