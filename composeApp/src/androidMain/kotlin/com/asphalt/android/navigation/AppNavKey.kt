package com.asphalt.android.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface AppNavKey : NavKey {

    @Serializable
    object LoginScreenNavKey : AppNavKey

    @Serializable
    object LoginSuccessScreenNavKey : AppNavKey

    @Serializable
    data object WelcomeFeatureNavKey : AppNavKey

    @Serializable
    data object SplashKey : AppNavKey

    @Serializable
    object DashboardNavKey : AppNavKey

    @Serializable
    object QueriesKey : AppNavKey

    @Serializable
    object ProfileKey : AppNavKey

    @Serializable
    object RidesScreenNav : AppNavKey

    @Serializable
    object CreateRideNav : AppNavKey

    @Serializable
    object NotificationNav : AppNavKey

    @Serializable
    object ForgotPasswordNav : AppNavKey

    @Serializable
    data class VerifyPassCodeNav(val emailId: String) : AppNavKey

    @Serializable
    object CreatPasswordNav : AppNavKey

    @Serializable
    object JoinRideNavKey : AppNavKey

    @Serializable
    object ConnectedRideMapNavKey : AppNavKey

    @Serializable
    object ConnectedRideNavKey : AppNavKey

    @Serializable
    object RideProgressNavKey : AppNavKey

    @Serializable
    object ConnectedRideEndNavKey : AppNavKey

    @Serializable
    object RatingRideNavKey : AppNavKey

    @Serializable
    data class RatingRide(val ridesID: String? = null,
        val userId: String? = null) : AppNavKey

    @Serializable
    object EndRideLoaderNavKey : AppNavKey

    @Serializable
    data class RideDetails(val ridesID: String? = null) : AppNavKey
}

data class BottomNavItems(
    val key: AppNavKey,
    val title: String,
    val iconRes: Int
)