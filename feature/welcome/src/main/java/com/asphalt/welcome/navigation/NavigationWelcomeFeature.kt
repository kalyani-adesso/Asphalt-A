package com.asphalt.welcome.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.SinglePaneSceneStrategy
import com.asphalt.welcome.composables.GetStartedScreen
import kotlinx.serialization.Serializable

@Serializable
data object WelcomeFeatureNavKey : NavKey

@Serializable
data object WelcomeScreenNavKey : NavKey

@Composable
fun NavigationWelcomeFeature(
    onNavigateToRegister: () -> Unit = {}
) {
    val backStack = rememberNavBackStack(WelcomeScreenNavKey)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        sceneStrategy = SinglePaneSceneStrategy(),
        entryProvider = entryProvider {
            entry<WelcomeScreenNavKey> {
                GetStartedScreen(
                    onNavigateToRegister = onNavigateToRegister
                )
            }
        }
    )
}