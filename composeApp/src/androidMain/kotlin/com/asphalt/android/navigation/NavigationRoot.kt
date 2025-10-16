package com.asphalt.android.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.asphalt.android.navigation.AppNavKey.SplashKey
import com.asphalt.commonui.R
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("FunctionName")
@Composable
fun NavigationRoot(
) {
    val backStack = rememberNavBackStack(AppNavKey.DashboardNavKey)

    var showBottomBar by remember { mutableStateOf(false) }
    var navigationDrawer by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val items = listOf(
        BottomNavItems(AppNavKey.DashboardNavKey, title = "Home", Icons.Filled.Home),
        BottomNavItems(AppNavKey.RidesScreenNav, title = "Rides", Icons.Filled.Home),
        BottomNavItems(AppNavKey.QueriesKey, title = "Queries", Icons.Filled.Home),
        BottomNavItems(AppNavKey.ProfileKey, title = "Profile", Icons.Filled.Home),
    )
    var selectedKey by remember { mutableStateOf<AppNavKey>(AppNavKey.DashboardNavKey) }


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
    ModalNavigationDrawer(
        drawerState= drawerState,
        drawerContent = {
            ModalDrawerSheet{
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(14.dp)
                ) {
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_connected_rides),
                                contentDescription = "error",
                                tint = Color.Black)
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        },
                        label = {Text("Connected Rides")},
                        selected = false,
                        onClick = {}
                    )

                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_knowledge),
                                contentDescription = "error",
                                tint = Color.Black)
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        },
                        label = {Text("Knowledge Circle")},
                        selected = false,
                        onClick = {}
                    )

                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_market),
                                contentDescription = "error",
                                tint = Color.Black)
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        },
                        label = {Text("Marketplace")},
                        selected = false,
                        onClick = {}
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_settings),
                                contentDescription = "error",
                                tint = Color.Black)
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        },
                        label = {Text("Settings")},
                        selected = false,
                        onClick = {}
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_send),
                                contentDescription = "error",
                                tint = Color.Black)
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        },
                        label = {Text("Refer a Friend")},
                        selected = false,
                        onClick = {}
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(16.dp),
                        thickness = 0.6.dp,
                        color = Color.Gray
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_logout),
                                contentDescription = "error",
                                tint = Color.Red)
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Red
                            )
                        },
                        label = {Text("Logout", color = Color.Red)},
                        selected = false,
                        onClick = {}
                    )
                }
            }
        }
    ) {
        Scaffold (

            topBar = {
                if (navigationDrawer) {
                    TopAppBar(
                        title = { Text("My App") },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                }
            },

            bottomBar = {
                if (showBottomBar) {
                    BottomBar(
                        items = items,
                        selectedKey = selectedKey,
                        onItemSelected = {
                            selectedKey = it
                            backStack.add(selectedKey)
                        }
                    )
                }
            }
        ) { innerpadding ->

            NavDisplay(
                modifier = Modifier
                    .padding(bottom = innerpadding.calculateBottomPadding()),
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
                    entry<AppNavKey.WelcomeFeatureNavKey> { key ->
                        NavigationWelcomeFeature(
                            onNavigateToRegister = {
                                backStack.remove(AppNavKey.WelcomeFeatureNavKey)
                                backStack.add(AppNavKey.LoginScreenNavKey)
                            }
                        )
                    }

                    entry<SplashKey> { key ->
                        NavigationSplashScreen(
                            onNavigateToLogin = {
                                backStack.remove(SplashKey)
                                backStack.add(AppNavKey.LoginScreenNavKey)
                            },
                            onNavigateToWelcome = {
                                backStack.remove(SplashKey)
                                backStack.add(AppNavKey.WelcomeFeatureNavKey)
                                backStack.add(AppNavKey.WelcomeFeatureNavKey)
                            },
                            onNavigateToDashboard = {
                                backStack.remove(SplashKey)
                                backStack.add(AppNavKey.DashboardNavKey)
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

                    entry<RegistrationDetailsNavKey> {
                        NavigationRegistrationDetails(
                            onNavigateToDashboard = { //password ->
                                //  backStack.add(RegistrationDetailsNavKey(password))
                                backStack.remove(SplashKey)

                            },
                            onBackPressed = { onBackPressed() }
                        )
                    }

                    entry<AppNavKey.LoginScreenNavKey> { key ->
                        LoginScreen(onSignInClick = {

                            backStack.add(AppNavKey.LoginSuccessScreenNavKey)
                            backStack.remove(AppNavKey.LoginScreenNavKey)
                        }, onSignUpClick = {
                            backStack.add(RegistrationDetailsNavKey)
                            //backStack.add(LoginSuccessScreenNavKey)
                        }, onDashboardNav = {
                            backStack.add(AppNavKey.DashboardNavKey)
                            backStack.remove(AppNavKey.LoginScreenNavKey)
                        })
                    }

                    entry<AppNavKey.LoginSuccessScreenNavKey> { key ->
                        LoginSuccessScreen(exploreClick = {
                            backStack.remove(AppNavKey.LoginSuccessScreenNavKey)
                            backStack.add(AppNavKey.DashboardNavKey)

                        })
                    }

                    entry<AppNavKey.DashboardNavKey> { key ->
                        showBottomBar = true
                        navigationDrawer = true
                        DashBoardScreen(upcomingRideClick = {
                            backStack.add(AppNavKey.RidesScreenNav)
                        },
                            paadingValues = innerpadding)
                    }
                    entry<AppNavKey.RidesScreenNav> { key ->
                        showBottomBar = true
                        navigationDrawer = false
                        RidesScreen()
                    }
                }
            )
        }
    }


}

class TopLevelBackStack<T : NavKey>(startKey : T) {

}