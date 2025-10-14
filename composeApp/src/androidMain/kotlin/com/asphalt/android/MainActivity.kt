package com.asphalt.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.asphalt.android.navigation.NavigationRoot
import com.asphalt.android.navigation.BotttomNavBar
import com.asphalt.commonui.theme.AsphaltTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            AsphaltTheme {
                Surface(
                    modifier = Modifier
                ) {

                    NavigationRoot()
                }
            }
        }
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