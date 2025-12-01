package com.asphalt.android.navigation

import androidx.navigation3.runtime.NavKey
import com.asphalt.android.model.connectedride.ConnectedRideRoot
import com.asphalt.android.model.rides.RidesData
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
    data class JoinRideNavKey(val ridesData: RidesData) : AppNavKey

    @Serializable
    data class ConnectedRideMapNavKey(val ridesData: RidesData) : AppNavKey

    @Serializable
    data class JoinRideDetails(val connectedRideRoot: ConnectedRideRoot) : AppNavKey

    @Serializable
    data class ConnectedRideNavKey(val ridesData: RidesData) : AppNavKey

    @Serializable
    data class RideProgressNavKey(val ridesData: RidesData) : AppNavKey

    @Serializable
    object ConnectedRideEndNavKey : AppNavKey

    @Serializable
    object RatingRideNavKey : AppNavKey

    @Serializable
    data class RatingRide(val ridesID: String? = null,
        val userId: String? = null) : AppNavKey

    @Serializable
    data class JoinRideConnectedRideMapNavKey(val joinRide: ConnectedRideRoot) : AppNavKey

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

