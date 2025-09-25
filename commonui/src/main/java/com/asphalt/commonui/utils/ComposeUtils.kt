package com.asphalt.commonui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object ComposeUtils {
    @Composable
    fun getScreenHeightPx(): Int {
        val windowInfo = LocalWindowInfo.current
        val screenHeightPx = windowInfo.containerSize.height
        return screenHeightPx
    }

    @Composable
    fun calculateHeightDpForPercentage(percentage: Float): Dp {
        val screenHeightPx = getScreenHeightPx()
        val density = LocalResources.current.displayMetrics.density
        return ((screenHeightPx * percentage) / density).dp
    }
}