package com.asphalt.android.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.SinglePaneSceneStrategy
import com.asphalt.android.navigation.NavKey.LoginScreenNavKey
import com.asphalt.dashboard.composables.screens.DashBoardScreen
import com.asphalt.dashboard.composables.screens.RidesScreen
import com.asphalt.login.ui.LoginScreen
import com.asphalt.login.ui.LoginSuccessScreen
import com.asphalt.registration.navigation.NavigationRegistrationCode
import com.asphalt.registration.navigation.NavigationRegistrationDetails
import com.asphalt.registration.navigation.RegistrationCodeNavKey
import com.asphalt.registration.navigation.RegistrationDetailsNavKey
import com.asphalt.registration.navigation.RegistrationPasswordNavKey
import com.asphalt.welcome.navigation.NavigationSplashScreen
import com.asphalt.welcome.navigation.NavigationWelcomeFeature
import java.util.Map.entry

@Suppress("FunctionName")
@Composable
fun NavigationRoot(
) {
    val backStack = rememberNavBackStack(NavKey.DashboardNavKey)

    val bottomNavITems = listOf(NavKey.DashboardNavKey, NavKey.RidersKey)

    fun onBackPressed() {
        println("Back Nav from ${backStack.lastOrNull()}")
        if (backStack.size > 1) {
            val key = backStack.lastOrNull()

            when (key) {
                is RegistrationPasswordNavKey -> {
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
            entry<NavKey.WelcomeFeatureNavKey> { key ->
                NavigationWelcomeFeature(
                    onNavigateToRegister = {
                        backStack.remove(NavKey.WelcomeFeatureNavKey)
                        backStack.add(LoginScreenNavKey)
                    }
                )
            }

            entry<NavKey.SplashKey> { key ->
                NavigationSplashScreen(
                    onNavigateToLogin = {
                        backStack.add(LoginScreenNavKey)
                    },
                    onNavigateToWelcome = {

                        backStack.add(NavKey.WelcomeFeatureNavKey)
                        backStack.add(NavKey.WelcomeFeatureNavKey)
                    },
                    onNavigateToDashboard = {
                        backStack.add(NavKey.DashboardNavKey)
                    }
                )
                backStack.remove(NavKey.SplashKey)
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

            entry<RegistrationDetailsNavKey> {
                NavigationRegistrationDetails(
                    onNavigateToDashboard = { //password ->
                        //  backStack.add(RegistrationDetailsNavKey(password))
                    },
                    onBackPressed = { onBackPressed() }
                )
            }

            entry<LoginScreenNavKey> { key ->
                LoginScreen(onSignInClick = {
                    backStack.add(NavKey.LoginSuccessScreenNavKey)
                    backStack.remove(LoginScreenNavKey)
                }, onSignUpClick = {
                    backStack.add(RegistrationDetailsNavKey)
                    //backStack.add(LoginSuccessScreenNavKey)
                }, onDashboardNav = {
                    backStack.add(NavKey.DashboardNavKey)
                    backStack.remove(LoginScreenNavKey)
                })
            }

            entry<NavKey.LoginSuccessScreenNavKey> { key ->
                LoginSuccessScreen(exploreClick = {
                    backStack.remove(NavKey.LoginSuccessScreenNavKey)
                    backStack.add(NavKey.DashboardNavKey)

                })
            }

            entry<NavKey.DashboardNavKey> { key ->
                DashBoardScreen()
                entry<NavKey.DashboardNavKey> { key ->
                    DashBoardScreen(upcomingRideClick = {
                        backStack.add(NavKey.RidesScreenNav)
                    })
                }
                entry<NavKey.RidesScreenNav> { key ->
                    RidesScreen()
                }
            }
        }
    )
}
