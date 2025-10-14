package com.asphalt.android.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.asphalt.commonui.R
import kotlinx.serialization.Serializable


@Serializable
object LoginScreenNavKey : NavKey
@Serializable
object LoginSuccessScreenNavKey : NavKey
@Serializable
data object WelcomeFeatureNavKey : NavKey
@Serializable
data object SplashKey : NavKey
@Serializable
data object DashboardNavKey : NavKey, BottomNavItems {
    override val icon: Int = R.drawable.ic_home
    override val title: String = "Home"
}

@Serializable
object RidersKey : NavKey
//@Serializable
//object QueriesKey : NavKey, BottomNavItems {
//    override val icon: Int = R.drawable.ic_queries
//    override val title: String = "Rides"
//}

@Serializable
object ProfileKey : NavKey, BottomNavItems {
    override val icon: Int = R.drawable.ic_profile
    override val title: String = "Profile"
}
@Serializable
object RidesScreenNav : NavKey , BottomNavItems {
    override val icon: Int = R.drawable.ic_rides
    override val title: String = "Rides"
}
