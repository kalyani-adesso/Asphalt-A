package com.asphalt.welcome.navigation

import androidx.compose.runtime.Composable
import com.asphalt.welcome.composables.SplashScreen


@Composable
fun NavigationSplashScreen(
    onNavigateToLogin: () -> Unit = {},
    onNavigateToWelcome: () -> Unit = {},
    onNavigateToDashboard: () -> Unit = {}
) {
    SplashScreen(onNavigateToLogin, onNavigateToWelcome,onNavigateToDashboard)
}