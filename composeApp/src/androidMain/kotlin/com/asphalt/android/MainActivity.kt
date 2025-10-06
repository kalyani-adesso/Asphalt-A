package com.asphalt.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.asphalt.android.navigation.NavigationRoot
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.login.ui.LoginScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        setContent {
//            AsphaltTheme {
//                LoginScreen()
//                //LoginSuccessScreen()
//                //AsphaltApp()
//            }
            MaterialTheme {
                Surface(
                    modifier = Modifier
                ) {

                    NavigationRoot()
                }
            }
//            AsphaltTheme {
//              //  AsphaltApp()
//              //  RegistrationCodeScreen()
//
//            }
        }
    }
}

@Composable
fun StartScreen(
    navigateOnRegistrationClick: () -> Unit,
    navigateOnRegistrationPasswordClick : () -> Unit,
    navigateOnButtonWelcomeClick : () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
    ) {

        Button(onClick = navigateOnButtonWelcomeClick) {
            Text(text = "Get Started")
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    //App()
    AsphaltTheme {
       // AsphaltApp()
        NavigationRoot()
    }
}