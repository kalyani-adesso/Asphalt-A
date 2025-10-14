package com.asphalt.android.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.SinglePaneSceneStrategy
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
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource

@Suppress("FunctionName")
@Composable
fun NavigationRoot(
) {
    val backStack = rememberNavBackStack(SplashKey)

    //val bottomTabItems = rememberNavBackStack(DashboardNavKey)

    val bottomNavItems = listOf(DashboardNavKey, RidesScreenNav, ProfileKey)

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

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 2.dp
            ) {
                bottomNavItems.forEach { item ->
                  // val selected = true
                    NavigationBarItem(
                        selected = false,
                        onClick = {

                        },
                        label = {
                            Text(text = item.title)
//                                color = if (selected)
//                                    MaterialTheme.colorScheme.primary
//                                else
//                                    Color.Gray

                        },
                        icon = {
                            Image(painter = painterResource(id = item.icon),
                                contentDescription = item.title)

                        },
                        alwaysShowLabel = true,
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.primary
                                .copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }
    ) { innerpadding ->

        NavDisplay(
            modifier = Modifier.padding(innerpadding),
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
                entry<WelcomeFeatureNavKey> { key ->
                    NavigationWelcomeFeature(
                        onNavigateToRegister = {
                            backStack.remove(WelcomeFeatureNavKey)
                            backStack.add(LoginScreenNavKey)
                        }
                    )
                }

                entry<SplashKey> { key ->
                    NavigationSplashScreen(
                        onNavigateToLogin = {
                            backStack.add(LoginScreenNavKey)
                        },
                        onNavigateToWelcome = {

                            backStack.add(WelcomeFeatureNavKey)
                            backStack.add(WelcomeFeatureNavKey)
                        },
                        onNavigateToDashboard = {
                            backStack.add(DashboardNavKey)
                        }
                    )
                    backStack.remove(SplashKey)
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
                        backStack.add(LoginSuccessScreenNavKey)
                        backStack.remove(LoginScreenNavKey)
                    }, onSignUpClick = {
                        backStack.add(RegistrationDetailsNavKey)
                        //backStack.add(LoginSuccessScreenNavKey)
                    }, onDashboardNav = {
                        backStack.add(DashboardNavKey)
                        backStack.remove(LoginScreenNavKey)
                    })
                }

                entry<LoginSuccessScreenNavKey> { key ->
                    LoginSuccessScreen(exploreClick = {
                        backStack.remove(LoginSuccessScreenNavKey)
                        backStack.add(DashboardNavKey)

                    })
                }

                entry<DashboardNavKey> { key ->
                    DashBoardScreen(upcomingRideClick = {
                        backStack.add(RidesScreenNav)
                    })
                }
                entry<RidesScreenNav> { key ->
                    RidesScreen()
                }
            }
        )
    }
}

interface BottomNavItems {
    val icon: Int
    val title: String
}

class TopLevelBackStack<T : NavKey>(startKey : T) {

}