package com.asphalt.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.login.LoginScreen
import com.asphalt.login.LoginSuccessScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            AsphaltTheme {
                //LoginScreen()
                LoginSuccessScreen()
            }

        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {

    App()
}