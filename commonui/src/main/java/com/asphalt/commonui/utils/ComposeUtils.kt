package com.asphalt.commonui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.RoundedBox

object ComposeUtils {
    @Composable
    fun getScreenHeight(): Int {
        val windowInfo = LocalWindowInfo.current
        val screenHeight = windowInfo.containerSize.height
        return screenHeight
    }

    @Composable
    fun getScreenWidth(): Int {
        val windowInfo = LocalWindowInfo.current
        val screenWidth = windowInfo.containerSize.width
        return screenWidth
    }

    @Composable
    fun getDpForScreenRatio(percentage: Float, screenSize: Int): Dp {

        val density = LocalResources.current.displayMetrics.density
        return ((screenSize * percentage) / density).dp
    }

    @Composable
    fun DefaultButtonContent(buttonText: String) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

            Text(
                buttonText,
                color = NeutralWhite,
                fontSize = Dimensions.textSize18,
                style = TypographyBold.labelLarge,
            )
        }
    }

    @Composable
    fun ColorIconRounded(
        backColor: Color,
        size: Dp = Dimensions.size30,
        radious: Dp = Dimensions.size5,
        resId: Int
    ) {
        RoundedBox(
            backgroundColor = backColor,
            modifier = Modifier
                .width(size)
                .height(size),
            cornerRadius = radious,
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Image(
                    modifier = Modifier,
                    painter = painterResource(resId),
                    contentDescription = ""
                )
            }

        }
    }
}