package com.asphalt.android.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.SinglePaneSceneStrategy
import com.asphalt.android.StartScreen
import com.asphalt.registration.navigation.NavigationRegistrationCode
import com.asphalt.registration.navigation.NavigationRegistrationDetails
import com.asphalt.registration.navigation.RegistrationCodeNavKey
import com.asphalt.registration.navigation.RegistrationDetailsNavKey
import com.asphalt.registration.navigation.RegistrationPasswordNavKey
import com.asphalt.welcome.navigation.NavigationWelcomeFeature
import com.asphalt.welcome.navigation.WelcomeFeatureNavKey
import kotlinx.serialization.Serializable

@Suppress("FunctionName")
@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(WelcomeFeatureNavKey)

    fun onBackPressed() {
        println("Back Nav from ${backStack.lastOrNull()}")
        if (backStack.size > 1) {
            val key = backStack.lastOrNull()
            when (key) {
                is RegistrationPasswordNavKey -> {
                    backStack.removeLastOrNull()
                    backStack.removeLastOrNull()
                    backStack.add(
                        RegistrationCodeNavKey(
                            id = key.id,
                        )
                    )
                }
                else -> {
                    backStack.removeLastOrNull()
                }
            }
        } else {
            backStack.removeLastOrNull()
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = {
            onBackPressed()
        },
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        sceneStrategy = SinglePaneSceneStrategy(),
        entryProvider = entryProvider {
            entry<StartScreenNavKey> {
                StartScreen(
                    navigateOnRegistrationClick = {
                        backStack.add(
                            RegistrationCodeNavKey(
                                id = ""
                            )
                        )
                    },
                    navigateOnButtonWelcomeClick = { backStack.add(WelcomeFeatureNavKey) },
                    navigateOnRegistrationPasswordClick = {
                        backStack.add(RegistrationPasswordNavKey(id = ""))
                    },
                )
            }

            entry<WelcomeFeatureNavKey> { key ->
                NavigationWelcomeFeature(
                    onNavigateToRegister = {
                        backStack.add(RegistrationDetailsNavKey)
                    }
                )
            }

            entry<RegistrationCodeNavKey> { key ->
                NavigationRegistrationCode(
                    id = key.id,
                    onBackPressed = { backStack.removeLastOrNull() },
                    onNavigateToRegistrationPassword = { id ->
                        backStack.add(RegistrationPasswordNavKey(id = id))
                    }
                )
            }
//            entry<RegistrationPasswordNavKey> {
//                NavigationRegistrationPassword(
//                    id = it.id,
//                    onNavigationToRegistrationDetails = { password ->
//                        backStack.add(RegistrationDetailsNavKey(password = password))
//                    },
//                    onBackPressed = { onBackPressed() }
//                )
//            }

            entry<RegistrationDetailsNavKey> {
                NavigationRegistrationDetails(
                    //id = it.password,
                    onNavigateToDashboard = { //password ->
                      //  backStack.add(RegistrationDetailsNavKey(password))
                    },
                    onBackPressed = { onBackPressed() }
                )
            }
        }
    )
}

@Serializable
data object StartScreenNavKey : NavKey