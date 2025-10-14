package com.asphalt.android.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable



sealed class NavKey(val route: String) {
    @Serializable
    object LoginScreenNavKey : NavKey

    @Serializable
    object LoginSuccessScreenNavKey : NavKey

    @Serializable
    data object WelcomeFeatureNavKey : NavKey

    @Serializable
    data object SplashKey : NavKey

    @Serializable
    data object DashboardNavKey : NavKey

    @Serializable
    object RidersKey : NavKey

    @Serializable
    object QueriesKey : NavKey

    @Serializable
    object ProfileKey : NavKey

    @Serializable
    object RidesScreenNav : NavKey

    companion object {
        fun all() = listOf(DashboardNavKey, RidersKey, QueriesKey, ProfileKey)
    }
}
