package com.asphalt.android.navigation

import androidx.navigation3.runtime.NavKey
import com.asphalt.android.model.connectedride.ConnectedRideDTO
import com.asphalt.android.model.connectedride.ConnectedRideRoot
import com.asphalt.android.model.rides.RidesData
import com.asphalt.chat.model.ChatParamsModel
import com.asphalt.joinaride.models.RideSummaryData
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
    data class MessageUiScreenKey(val ridesData: ConnectedRideDTO) : AppNavKey
    @Serializable
    data class ConnectedRideEndNavKey(val ridesData: RidesData,val summaryData: RideSummaryData) : AppNavKey

    @Serializable
    data class RatingRideNavKey(val ridesData: RidesData) : AppNavKey

    @Serializable
    data class RatingRide(val ridesID: String? = null,
        val userId: String? = null) : AppNavKey

    @Serializable
    data class JoinRideConnectedRideMapNavKey(val joinRide: ConnectedRideRoot) : AppNavKey

    @Serializable
    data class EndRideLoaderNavKey(val ridesData: RidesData,val rideSummaryData: RideSummaryData) : AppNavKey

    @Serializable
    data class RideDetails(val ridesID: String? = null) : AppNavKey

    @Serializable
    object ChatListNavaKey : AppNavKey

    @Serializable
    data class ChatScreenNavaKey(
        val ids: List<String>? = null,
        val ridesData: ChatParamsModel? = null,
        val isGroupChat: Boolean = false
    ) :
        AppNavKey

    @Serializable
    object CreateAd : AppNavKey
@Serializable
object ProductDetails : AppNavKey
}



data class BottomNavItems(
    val key: AppNavKey,
    val title: String,
    val iconRes: Int
)

