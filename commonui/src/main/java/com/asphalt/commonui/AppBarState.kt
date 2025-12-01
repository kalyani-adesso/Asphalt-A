package com.asphalt.commonui

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

data class AppBarState(
    val title: String = "",
    val isCenterAligned: Boolean = true,
    val actions: @Composable RowScope.() -> Unit = {},
    val subtitle: String = "",
    val dashboardHeader: @Composable BoxScope.() -> Unit = {}
)