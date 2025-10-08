package com.asphalt.commonui.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.utils.Constants

@Composable
fun GradientButton(
    modifier: Modifier = Modifier,
    startColor: Color = PrimaryDarkerLightB75,
    endColor: Color = PrimaryDarkerLightB50,
    onClick: () -> Unit,
    buttonHeight: Dp = Constants.DEFAULT_BUTTON_HEIGHT,
    buttonRadius: Dp = Constants.DEFAULT_CORNER_RADIUS,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(
            startColor,
            endColor
        )
    )
    Box(
        modifier = modifier
            .wrapContentSize()
            .background(brush = gradient, shape = RoundedCornerShape(size = buttonRadius))

    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            modifier = Modifier
                .height(buttonHeight),
            shape = RoundedCornerShape(buttonRadius),
            contentPadding = contentPadding,
            content = content
        )


    }
}