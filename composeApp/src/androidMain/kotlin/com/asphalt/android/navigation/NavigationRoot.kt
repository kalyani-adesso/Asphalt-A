package com.asphalt.android.navigation

import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.SinglePaneSceneStrategy
import com.asphalt.android.navigation.AppNavKey.SplashKey
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.createride.ui.CreateRideEntry
import com.asphalt.dashboard.composables.screens.DashBoardScreen
import com.asphalt.dashboard.composables.screens.RidesScreen
import com.asphalt.login.ui.LoginScreen
import com.asphalt.login.ui.LoginSuccessScreen
import com.asphalt.profile.screens.ProfileScreen
import com.asphalt.queries.screens.QueriesScreen
import com.asphalt.registration.navigation.NavigationRegistrationCode
import com.asphalt.registration.navigation.NavigationRegistrationDetails
import com.asphalt.registration.navigation.RegistrationCodeNavKey
import com.asphalt.registration.navigation.RegistrationDetailsNavKey
import com.asphalt.registration.navigation.RegistrationPasswordNavKey
import com.asphalt.welcome.navigation.NavigationSplashScreen
import com.asphalt.welcome.navigation.NavigationWelcomeFeature

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("FunctionName")
@Composable
fun NavigationRoot(
) {
    val backStack = rememberNavBackStack(AppNavKey.SplashKey)

//    var showAppBars by remember { mutableStateOf(false) }
//    var navigationDrawer by remember { mutableStateOf(false) }
    val showBottomBar = backStack.lastOrNull() in listOf(
        AppNavKey.DashboardNavKey,
        AppNavKey.RidesScreenNav,
        AppNavKey.QueriesKey,
        AppNavKey.ProfileKey
    )
    val showTopAppBar = backStack.lastOrNull() in listOf(
        AppNavKey.DashboardNavKey,
        AppNavKey.RidesScreenNav,
        AppNavKey.QueriesKey,
        AppNavKey.ProfileKey,
        AppNavKey.CreateRideNav

    )


    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val items = listOf(
        BottomNavItems(AppNavKey.DashboardNavKey, title = "Home", R.drawable.ic_home),
        BottomNavItems(AppNavKey.RidesScreenNav, title = "Rides", R.drawable.ic_rides),
        BottomNavItems(AppNavKey.QueriesKey, title = "Queries", R.drawable.ic_chat_bubble),
        BottomNavItems(AppNavKey.ProfileKey, title = "Profile", R.drawable.ic_person),
    )
    var selectedKey by remember { mutableStateOf<AppNavKey>(AppNavKey.DashboardNavKey) }
    val bottomNavItems = listOf(
        AppNavKey.DashboardNavKey, AppNavKey.RidesScreenNav, AppNavKey.QueriesKey,
        AppNavKey.ProfileKey
    )
    var topAppBarState by remember { mutableStateOf(AppBarState()) }

    // This is the callback function that screens will use to update the state
    val setTopAppBarState: (AppBarState) -> Unit = { newState ->
        topAppBarState = newState
    }

    fun manageSelectKeyOnBackPress() {
        if (backStack.isNotEmpty()) {
            val closestTabKey = backStack.reversed().firstOrNull { it in bottomNavItems }

            if (closestTabKey != null) {
                selectedKey = closestTabKey as AppNavKey
            }
        }
    }


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
                    manageSelectKeyOnBackPress()
                }
            }
        } else {
            backStack.removeLastOrNull()
            manageSelectKeyOnBackPress()
        }
    }
    RidersClubSideMenu(drawerState) {

        Scaffold(

            topBar = {
                if (showTopAppBar) {
                    RidersClubTopAppBar(
                        scope,
                        drawerState,
                        topAppBarState,
                        onBack = ::onBackPressed,
                        isDashboard = backStack.lastOrNull() == AppNavKey.DashboardNavKey
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
                            //TODO:Check how navigation is to be done in inner pages of tabs,
                            // then decide on backstack bottom bar handling, sample in commented line below
//                            if (backStack.contains(selectedKey) && selectedKey != AppNavKey.DashboardNavKey)
//                                backStack.remove(selectedKey)
                            if (backStack.lastOrNull() != selectedKey)
                                backStack.add(selectedKey)
                        },
                        modifier = Modifier.windowInsetsPadding(
                            ScaffoldDefaults.contentWindowInsets.only(
                                WindowInsetsSides.Bottom
                            )
                        )
                    )
                }
            }
        ) { paddingValues ->

            NavDisplay(
                modifier =
                    if (showBottomBar || showTopAppBar)
                        Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    else Modifier,
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
                                //backStack.add(AppNavKey.CreateRideNav)
                            },
                            onNavigateToWelcome = {
                                backStack.remove(SplashKey)
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
                        DashBoardScreen(
                            upcomingRideClick = {
                                backStack.add(AppNavKey.RidesScreenNav)
                                selectedKey = AppNavKey.RidesScreenNav
                            },
                            setTopAppBarState = setTopAppBarState, notificationsClick = {

                            }, creatRideClick = {
                                backStack.add(AppNavKey.CreateRideNav)
                            }
                        )
                    }
                    entry<AppNavKey.RidesScreenNav> { key ->
                        RidesScreen(setTopAppBarState)
                    }
                    entry<AppNavKey.QueriesKey> { key ->
                        QueriesScreen(setTopAppBarState=setTopAppBarState)
                    }
                    entry<AppNavKey.ProfileKey> { key ->
                        ProfileScreen(setTopAppBarState = setTopAppBarState)
                    }
                    entry<AppNavKey.CreateRideNav> { key ->
                        CreateRideEntry(setTopAppBarState=setTopAppBarState)
                    }
                }
            )

        }

    }
}