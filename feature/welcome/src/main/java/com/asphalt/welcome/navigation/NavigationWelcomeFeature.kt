package com.asphalt.welcome.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.SinglePaneSceneStrategy
import com.asphalt.commonui.utils.Constants
import com.asphalt.welcome.composables.GetStartedScreen
import com.asphalt.welcome.composables.SplashScreen
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

@Serializable
data object WelcomeFeatureNavKey : NavKey

@Composable
fun NavigationWelcomeFeature(
    onNavigateToRegister: () -> Unit = {}
) {
    val backStack = rememberNavBackStack(WelcomeFeatureNavKey)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        sceneStrategy = SinglePaneSceneStrategy(),
        entryProvider = entryProvider {
            entry<WelcomeFeatureNavKey> {
                var showSplash by remember { mutableStateOf(true) }
                LaunchedEffect(Unit) {
                    delay(Constants.SPLASH_TIMER)
                    showSplash = false
                }
                if (showSplash) {
                    SplashScreen()
                } else {
                    GetStartedScreen(
                        onNavigateToRegister = onNavigateToRegister
                    )
                }

            }
        }
    )
}