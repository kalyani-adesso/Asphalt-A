package com.asphalt.registration.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import com.asphalt.registration.RegistrationDetailsScreen
import kotlinx.serialization.Serializable

@Serializable
data object RegistrationDetailsNavKey : NavKey

@Composable
fun NavigationRegistrationDetails(
    onBackPressed: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    RegistrationDetailsScreen(
        // onBackPressed = onBackPressed,
        onNavigateToLogin = onNavigateToLogin
    )
}

// use data class when need to pass the data
//data class RegistrationDetailsNavKey(val password:String) : NavKey
