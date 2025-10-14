package com.asphalt.commonui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralGrey30
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.Typography
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
    fun DefaultColumnRootWithScroll(
        topPadding: Dp,
        bottomPadding: Dp,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(
                    top = topPadding,
                    bottom = bottomPadding,
                    start = Constants.DEFAULT_SCREEN_HORIZONTAL_PADDING,
                    end = Constants.DEFAULT_SCREEN_HORIZONTAL_PADDING
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Dimensions.size20)
        ) {
            content()
        }
    }

    @Composable
    fun CommonContentBox(
        modifier: Modifier = Modifier,
        isBordered: Boolean = false,
        content: @Composable BoxScope.() -> Unit
    ) {
        RoundedBox(
            modifier = modifier,
            borderColor = if (isBordered) NeutralGrey30 else null,
            backgroundColor = NeutralLightPaper,
            borderStroke = if (isBordered) Constants.DEFAULT_BORDER_STROKE else null,
        ) {
            content()
        }
    }

    @Composable
    fun ColorIconRounded(
        backColor: Color,
        size: Dp = Dimensions.size30,
        radius: Dp = Dimensions.size5,
        resId: Int
    ) {
        RoundedBox(
            backgroundColor = backColor,
            modifier = Modifier
                .width(size)
                .height(size),
            cornerRadius = radius,
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

    @Composable
    fun SectionTitle(text: String, modifier: Modifier = Modifier) {
        Text(
            style = TypographyBold.bodyMedium,
            fontSize = Dimensions.textSize16, text = text, modifier = modifier
        )
    }

    @Composable
    fun SectionSubtitle(text: String, modifier: Modifier = Modifier) {
        Text(
            text = text, style = Typography.bodyMedium,
            fontSize = Dimensions.textSize12,
            color = NeutralDarkGrey, modifier = modifier
        )
    }
}