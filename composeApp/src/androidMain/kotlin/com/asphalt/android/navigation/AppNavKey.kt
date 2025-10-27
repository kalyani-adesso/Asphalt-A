package com.asphalt.android.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
sealed interface AppNavKey : NavKey {

sealed class AppNavKey : NavKey {
    @Serializable
    object LoginScreenNavKey : AppNavKey

    object LoginScreenNavKey : AppNavKey()
    @Serializable
    object LoginSuccessScreenNavKey : AppNavKey

    object LoginSuccessScreenNavKey : AppNavKey()
    @Serializable
    data object WelcomeFeatureNavKey : AppNavKey

    data object WelcomeFeatureNavKey : AppNavKey()
    @Serializable
    data object SplashKey : AppNavKey

    data object SplashKey : AppNavKey()
    @Serializable
    object DashboardNavKey : AppNavKey


    object DashboardNavKey : AppNavKey()
    @Serializable
    object QueriesKey : AppNavKey
//object QueriesKey : NavKey, BottomNavItems {
//    override val icon: Int = R.drawable.ic_queries
//    override val title: String = "Rides"
//}

    object QueriesKey : AppNavKey()
    @Serializable
    object ProfileKey : AppNavKey

    @Serializable
    object RidesScreenNav : AppNavKey

    object ProfileKey : AppNavKey()
    @Serializable
    object CreateRideNav : AppNavKey

    object RidesScreenNav : AppNavKey()
    @Serializable
    object NotificationNav : AppNavKey

    object CreateRideNav : AppNavKey()
    @Serializable
    object ForgotPasswordNav : AppNavKey
    @Serializable
    data class VerifyPassCodeNav (val emailId : String): AppNavKey
    @Serializable
    object CreatPasswordNav : AppNavKey

    @Serializable
    object JoinRideNavKey : AppNavKey()

    @Serializable
    object ConnectedRideNavKey : AppNavKey()

    @Serializable
    object ConnectedRideEndNavKey : AppNavKey()
}
data class BottomNavItems(
    val key: AppNavKey,
    val title: String,
    val iconRes: Int
)