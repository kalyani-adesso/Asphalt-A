package com.asphalt.commonui.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.utils.Constants

@Composable
fun BorderedButton(
    borderColor: Color = PrimaryDarkerLightB75,
    onClick: () -> Unit,
    buttonHeight: Dp = Constants.DEFAULT_BUTTON_HEIGHT,
    buttonRadius: Dp = Constants.DEFAULT_CORNER_RADIUS,
    borderStroke: Dp = Constants.DEFAULT_BORDER_STROKE,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = Modifier
            .height(buttonHeight)
            .border(
                width = borderStroke,
                color = borderColor,
                shape = RoundedCornerShape(buttonRadius)
            ),
        shape = RoundedCornerShape(buttonRadius),
        content = content
    )
}
