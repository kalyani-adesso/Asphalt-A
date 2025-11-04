package com.asphalt.commonui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.LightGreen
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import kotlinx.coroutines.delay


enum class BannerType {
    SUCCESS  //, INFO, ERROR
}
@Composable
fun StatusBanner(
    type: BannerType = BannerType.SUCCESS,
    message: String,
    showBanner : Boolean,
    autoDismissMillis : Long = 2500L,
) {
    var showSuccess by remember { mutableStateOf(showBanner) }

    LaunchedEffect(showBanner) {
        showSuccess = showBanner
        if (showBanner && autoDismissMillis > 0) {
            delay(autoDismissMillis)
            showSuccess = false
        }
    }
    val (bgColor,iconColor,icon) = when( type) {
        BannerType.SUCCESS -> Triple(Color(color = (0xFFBFFDD9)),
            Color(color = (0xFF016730)), Icons.Default.CheckBox)

//        BannerType.INFO -> Triple(Color(GreenLIGHT25),
//            Color(ShamrockGreen), Icons.Default.CheckBox)
//
//        BannerType.ERROR -> Triple(Color(GreenLIGHT25),
//            Color(ShamrockGreen), Icons.Default.CheckBox)
    }
    AnimatedVisibility(
        visible = showSuccess,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = Dimensions.padding, vertical = Dimensions.padding8)
                .clip(RoundedCornerShape(Dimensions.padding13))
                .background(bgColor),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_success),
                contentDescription = "Success",
                modifier = Modifier.padding(Dimensions.padding14),
                tint = Color.Unspecified // keep original color
            )
            Spacer(modifier = Modifier.height(Dimensions.padding13))

            Text(
                text = message,
                style = TypographyBold.bodyLarge,
                color = iconColor,
                modifier = Modifier
                    .padding(Dimensions.padding10)
            )
        }
    }
}