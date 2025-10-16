package com.asphalt.android.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
sealed class AppNavKey : NavKey {

    @Serializable
    object LoginScreenNavKey : AppNavKey()

    @Serializable
    object LoginSuccessScreenNavKey : AppNavKey()

    @Serializable
    data object WelcomeFeatureNavKey : AppNavKey()

    @Serializable
    data object SplashKey : AppNavKey()

    @Serializable
    object DashboardNavKey : AppNavKey()

    @Serializable
    object RidersKey : AppNavKey()
    @Serializable
    object QueriesKey : AppNavKey()
//object QueriesKey : NavKey, BottomNavItems {
//    override val icon: Int = R.drawable.ic_queries
//    override val title: String = "Rides"
//}

    @Serializable
    object ProfileKey : AppNavKey()

    @Serializable
    object RidesScreenNav : AppNavKey()

}

data class BottomNavItems(
    val key: AppNavKey,
    val title: String,
    val icon: ImageVector
)



