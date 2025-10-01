package com.asphalt.commonui.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.utils.Constants

@Composable
fun RoundedBox(
    modifier: Modifier = Modifier,
    backgroundColor: Color = NeutralWhite,
    cornerRadius: Dp = Constants.DEFAULT_CORNER_RADIUS,
    borderStroke: Dp? = null,
    borderColor: Color? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(cornerRadius))
            .border(
                width = borderStroke ?: 0.dp,
                shape = RoundedCornerShape(cornerRadius),
                color = borderColor ?: Color.Transparent
            ),
        content = content
    )
}