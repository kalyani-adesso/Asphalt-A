package com.asphalt.commonui.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.utils.ComposeUtils.SectionSubtitle

@Composable
fun CustomLabel(
    modifier: Modifier = Modifier,
    textColor: Color,
    backColor: Color,
    text: String,
    iconRes: Int? = null
) {
    RoundedBox(
        modifier = modifier,
        backgroundColor = backColor, cornerRadius = Dimensions.radius5
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = Dimensions.padding6,
                vertical = Dimensions.size4
            ),
        ) {
            iconRes?.let {
                Icon(
                    painter = painterResource(it),
                    null,
                    modifier = Modifier.padding(end = Dimensions.padding5)
                )
            }
            SectionSubtitle(
                text = text, color = textColor
            )
        }
    }

}