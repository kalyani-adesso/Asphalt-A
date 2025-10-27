package com.asphalt.dashboard.composables.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.FadingLine

@Composable
fun RideStatBox(iconRes: Int, data: String, description: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(Dimensions.padding13)
            .fillMaxHeight()
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.align(Alignment.TopStart)
                .padding(top = Dimensions.padding5)
        )

        FadingLine(Modifier.align(Alignment.Center))

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .wrapContentHeight()
        ) {
            Text(
                data,
                fontSize = Dimensions.textSize17,
                color = NeutralWhite,
                lineHeight = Dimensions.lineSpacing5,
                style = TypographyMedium.headlineLarge
            )
            Spacer(Modifier.height(Dimensions.spacing5))
            Text(
                description,
                fontSize = Dimensions.textSize12pt5,
                color = NeutralWhite,
                lineHeight = Dimensions.lineSpacing5,
                style = Typography.headlineLarge
            )
        }
    }
}
