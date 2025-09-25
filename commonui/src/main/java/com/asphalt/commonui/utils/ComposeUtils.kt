package com.asphalt.commonui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object ComposeUtils {
    @Composable
    fun getScreenHeight(): Int {
        val windowInfo = LocalWindowInfo.current
        val screenHeight = windowInfo.containerSize.height
        return screenHeight
    }

    @Composable
    fun calculateHeightDpForPercentage(percentage: Float): Dp {
        val screenHeight = getScreenHeight()
        val density = LocalResources.current.displayMetrics.density
        return ((screenHeight * percentage) / density).dp
    }
}