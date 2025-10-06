package com.asphalt.registration.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import com.asphalt.registration.RegistrationCodeScreen
import kotlinx.serialization.Serializable

@Serializable
data object NavigationRegistrationCodeKey : NavKey
@Composable
fun NavigationRegistrationCode(
    id: String,
    onBackPressed: () -> Unit = {},
    onNavigateToRegistrationPassword: (String) -> Unit = { _ -> }
) {
    RegistrationCodeScreen(
        id = id,
        onBackPressed = onBackPressed,
        onNavigateToRegistrationPassword = onNavigateToRegistrationPassword
    )
}


@Serializable
data class RegistrationCodeNavKey(val id: String) : NavKey