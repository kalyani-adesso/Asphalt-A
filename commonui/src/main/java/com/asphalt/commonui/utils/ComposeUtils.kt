package com.asphalt.commonui.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.TypographyBold

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
    fun getDpForScreenRatio(percentage: Float, screenSize:Int): Dp {

        val density = LocalResources.current.displayMetrics.density
        return ((screenSize * percentage) / density).dp
    }

    @Composable
    fun DefaultButtonContent(buttonText:String){
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

            Text(
                buttonText,
                color = NeutralWhite,
                fontSize = Dimensions.textSize18,
                style = TypographyBold.labelLarge,
            )
        }
    }

}