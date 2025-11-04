package com.asphalt.commonui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GreenLIGHT25
import com.asphalt.commonui.theme.LightGreen
import com.asphalt.commonui.theme.MutedRose
import com.asphalt.commonui.theme.OrangeLight10
import com.asphalt.commonui.theme.PaleMint
import com.asphalt.commonui.theme.PalePink
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.VividRed
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.ComposeUtils
import kotlinx.coroutines.delay


enum class BannerType {
    SUCCESS, INFO, ERROR
}

@Composable
fun StatusBanner(
    type: BannerType = BannerType.SUCCESS,
    message: String,
    showBanner: Boolean,
    autoDismissMillis: Long = 2500L,
    onDismiss:()->Unit
) {


    LaunchedEffect(showBanner) {
        if (showBanner && autoDismissMillis > 0) {
            delay(autoDismissMillis)
            onDismiss()
        }
    }

    val bannerUIElements: BannerUIElements = when (type) {
        BannerType.SUCCESS -> BannerUIElements.SuccessBannerElement
        BannerType.INFO -> BannerUIElements.SuccessBannerElement
        BannerType.ERROR -> BannerUIElements.ErrorBannerElement
    }

    AnimatedVisibility(
        visible = showBanner,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        RoundedBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimensions.size60)
                .padding(horizontal = Dimensions.padding15),
            borderColor = bannerUIElements.borderColor,
            backgroundColor = bannerUIElements.bgColor,
            borderStroke = Constants.DEFAULT_BORDER_STROKE,
            cornerRadius = Dimensions.spacing12
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimensions.padding20),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    Dimensions.spacing10
                )
            ) {
                ComposeUtils.ColorIconRounded(
                    backColor = bannerUIElements.bodyColor,
                    size = Dimensions.padding28,
                    radius = Dimensions.radius6,
                    resId = bannerUIElements.iconRes
                )
                Text(message, style = TypographyBold.bodySmall, color = bannerUIElements.bodyColor, overflow = TextOverflow.Ellipsis, maxLines = 1)

            }
        }
    }
}

sealed class BannerUIElements(
    val bgColor: Color,
    val borderColor: Color?,
    val bodyColor: Color,
    val iconRes: Int
) {
    object SuccessBannerElement :
        BannerUIElements(PaleMint, LightGreen, GreenLIGHT25, R.drawable.ic_tick)

    object ErrorBannerElement :
        BannerUIElements(PalePink, MutedRose, VividRed, R.drawable.ic_error)
}