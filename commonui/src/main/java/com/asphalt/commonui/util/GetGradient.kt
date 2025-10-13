package com.asphalt.commonui.util


import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75

@Composable
fun GetGradient(
    startColor: Color = PrimaryDarkerLightB75,
    endColor: Color = PrimaryDarkerLightB50
): Brush {
    val gradient = Brush.horizontalGradient(
        colors = listOf(
            startColor,
            endColor
        )
    )
    return gradient
}