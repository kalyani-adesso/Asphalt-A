package com.asphalt.registration.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import com.asphalt.registration.RegistrationPasswordScreen
import kotlinx.serialization.Serializable

@Composable
fun NavigationRegistrationPassword(
    id: String = "",
    onBackPressed: () -> Unit = {},
    onNavigationToPostRegistration: (Boolean) -> Unit = {}
) {
    RegistrationPasswordScreen(
//        id = id,
//        code = code,
//        onBackPressed = onBackPressed,
//        onNavigationToPostRegistration = onNavigationToPostRegistration
    )
}

@Serializable
data class RegistrationPasswordNavKey(val id: String) : NavKey //, val code: String