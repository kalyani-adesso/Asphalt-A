package com.asphalt.registration.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.SinglePaneSceneStrategy
import com.asphalt.registration.RegistrationDetailsScreen
import kotlinx.serialization.Serializable

@Serializable
data object RegistrationDetailsNavKey : NavKey

@Composable
fun NavigationRegistrationDetails(
    onBackPressed: () -> Unit = {},
    onNavigateToDashboard: () -> Unit = {}
) {
    RegistrationDetailsScreen(
       // onBackPressed = onBackPressed,
        onNavigateToDashboard = onNavigateToDashboard)
}

// use data class when need to pass the data
//data class RegistrationDetailsNavKey(val password:String) : NavKey
