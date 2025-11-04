package com.asphalt.android.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.SinglePaneSceneStrategy
import com.asphalt.android.datastore.DataStoreManager
import com.asphalt.android.navigation.AppNavKey.SplashKey
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.BannerType
import com.asphalt.commonui.R
import com.asphalt.commonui.StatusBanner
import com.asphalt.commonui.UIState
import com.asphalt.commonui.UIStateHandler
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.constants.PreferenceKeys
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralGrey80
import com.asphalt.commonui.theme.NeutralLightGray25
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.NeutralWhite40
import com.asphalt.commonui.theme.PaleMintyBlue30
import com.asphalt.commonui.theme.VividRed
import com.asphalt.commonui.ui.BouncingCirclesLoader
import com.asphalt.commonui.ui.LoaderPopup
import com.asphalt.createride.ui.CreateRideScreen
import com.asphalt.dashboard.composables.screens.DashBoardScreen
import com.asphalt.dashboard.composables.screens.NotificationScreen
import com.asphalt.dashboard.composables.screens.RidesScreen
import com.asphalt.joinaride.ConnectedRideScreen
import com.asphalt.joinaride.JoinRideScreen
import com.asphalt.login.ui.LoginScreen
import com.asphalt.login.ui.LoginSuccessScreen
import com.asphalt.profile.screens.ProfileScreen
import com.asphalt.queries.screens.QueriesScreen
import com.asphalt.registration.navigation.NavigationRegistrationCode
import com.asphalt.registration.navigation.NavigationRegistrationDetails
import com.asphalt.registration.navigation.RegistrationCodeNavKey
import com.asphalt.registration.navigation.RegistrationDetailsNavKey
import com.asphalt.registration.navigation.RegistrationPasswordNavKey
import com.asphalt.resetpassword.screens.CreatePasswordScreen
import com.asphalt.resetpassword.screens.ForgotPasswordScreen
import com.asphalt.resetpassword.screens.VerifyScreen
import com.asphalt.welcome.navigation.NavigationSplashScreen
import com.asphalt.welcome.navigation.NavigationWelcomeFeature
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("FunctionName")
@Composable
fun NavigationRoot(
    androidUserVM: AndroidUserVM = koinViewModel()
) {
    val backStack = rememberNavBackStack(AppNavKey.SplashKey)
    val datastore: DataStoreManager = koinInject()
    var showLoader by remember { mutableStateOf(false) }
    var showBanner by remember { mutableStateOf(false) }
    var bannerMsg by remember { mutableStateOf("") }
    var bannerType by remember { mutableStateOf(BannerType.SUCCESS) }
    val density = LocalDensity.current
    LaunchedEffect(Unit) {

        UIStateHandler.event.collect { state ->
            when (state) {
                UIState.DismissLoader -> showLoader = false
                is UIState.Error -> {
                    showBanner = true
                    bannerType = state.type
                    bannerMsg = state.errorMsg
                }

                UIState.Loading -> showLoader = true
                is UIState.SUCCESS -> {
                    showBanner = true
                    bannerType = state.type
                    bannerMsg = state.successMsg
                }
            }

        }
    }

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
        AppNavKey.CreateRideNav,
        AppNavKey.NotificationNav,
        AppNavKey.JoinRideNavKey,
        AppNavKey.ConnectedRideNavKey
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
                    backStack.add(RegistrationCodeNavKey(id = key.id))
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

    @Composable
    fun AppContent() {
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
            val bannerOffsetDp = paddingValues.calculateTopPadding() + Dimensions.padding30

            val bannerPixelYOffset: Int = with(density) {
                bannerOffsetDp.roundToPx()
            }

            if (showBanner)
                Popup(offset = IntOffset(0,bannerPixelYOffset)) {
                    StatusBanner(message = bannerMsg, type = bannerType, showBanner = showBanner) {
                        showBanner = false
                    }
                }
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
                            onNavigateToLogin = { //password ->
                                //  backStack.add(RegistrationDetailsNavKey(password))
                                backStack.remove(SplashKey)
                                backStack.add(AppNavKey.LoginScreenNavKey)
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
                        }, onForgotClick = {
                            backStack.add(AppNavKey.ForgotPasswordNav)
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
                                backStack.add(AppNavKey.NotificationNav)

                            }, creatRideClick = {
                                backStack.add(AppNavKey.CreateRideNav)
                            },
                            joinRideClick = {
                                backStack.add(AppNavKey.JoinRideNavKey)
                            }
                        )
                    }
                    entry<AppNavKey.RidesScreenNav> { key ->
                        RidesScreen(setTopAppBarState=setTopAppBarState)
                    }
                    entry<AppNavKey.QueriesKey> { key ->
                        QueriesScreen(setTopAppBarState = setTopAppBarState)
                    }
                    entry<AppNavKey.ProfileKey> { key ->
                        ProfileScreen(setTopAppBarState = setTopAppBarState)
                    }
                    entry<AppNavKey.CreateRideNav> { key ->
                        CreateRideScreen(setTopAppBarState = setTopAppBarState, clickDone = {
                            backStack.remove(AppNavKey.CreateRideNav)
                            backStack.add(AppNavKey.RidesScreenNav)
                            selectedKey = AppNavKey.RidesScreenNav
                        })
                    }
                    entry<AppNavKey.NotificationNav> { key ->
                        NotificationScreen(setTopAppBarState = setTopAppBarState)
                    }
                    entry(AppNavKey.JoinRideNavKey) { key ->
                        JoinRideScreen(
                            setTopAppBarState = setTopAppBarState,
                            navigateToConnectedRide = {
                                backStack.add(AppNavKey.ConnectedRideNavKey)
                                // backStack.add(AppNavKey.DashboardNavKey)
                            })
                    }
                    entry<AppNavKey.ForgotPasswordNav> { key ->
                        ForgotPasswordScreen(onSendClick = { emailId ->
                            backStack.remove(AppNavKey.ForgotPasswordNav)
                            backStack.add(AppNavKey.VerifyPassCodeNav(emailId))
                        })
                    }
                    entry<AppNavKey.VerifyPassCodeNav> { key ->
                        VerifyScreen(key.emailId, onVerifyClick = {
                            backStack.remove(AppNavKey.VerifyPassCodeNav(key.emailId))
                            backStack.add(AppNavKey.CreatPasswordNav)
                        })
                    }
                    entry<AppNavKey.CreatPasswordNav> { key ->
                        CreatePasswordScreen(onUpdateClick = {
                            onBackPressed()
                        }, onBackClick = {
                            onBackPressed()
                        })
                    }
                    entry(AppNavKey.ConnectedRideNavKey) { key ->
                        ConnectedRideScreen(
                            setTopAppBarState = setTopAppBarState,
                            onBackButton = {
                                backStack.remove(AppNavKey.ConnectedRideNavKey)

                            }
                        )
                    }
                }
            )
            if (showLoader){
//                Box(modifier = Modifier.background(NeutralWhite40).padding(paddingValues).fillMaxSize()){
                    BouncingCirclesLoader()
//                }
            }
        }
    }
    if (showTopAppBar)
        RidersClubSideMenu(drawerState, itemClick = { item ->
            when (item) {
                Constants.LOGOUT_CLICK -> {
                    scope.launch {
                        datastore.saveValue(PreferenceKeys.USER_DETAILS, "")
                        androidUserVM.initialiseUserData()
                        datastore.saveValue(PreferenceKeys.REMEMBER_ME, false)
                        backStack.clear()
                        backStack.add(AppNavKey.LoginScreenNavKey)
                        drawerState.close()
                    }
                }
            }
        }) {
            AppContent()
        }
    else AppContent()

}
