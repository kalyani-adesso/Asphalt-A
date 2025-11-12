package com.asphalt.welcome.navigation

import androidx.compose.runtime.Composable
import com.asphalt.welcome.composables.GetStartedScreen


@Composable
fun NavigationWelcomeFeature(
    onNavigateToRegister: () -> Unit = {}
) {
    GetStartedScreen(
        onNavigateToRegister = onNavigateToRegister
    )
}