package com.asphalt.getstarted.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.PrimaryBrighterLightW75
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.getstarted.CarouselItem
import com.asphalt.getstarted.Constants
import com.asphalt.getstarted.R

@Composable
fun GetStartedScreen() {
    val windowInfo = LocalWindowInfo.current
    val density = LocalResources.current.displayMetrics.density
    val screenHeightPx = windowInfo.containerSize.height
    val carouselItems = listOf(
        CarouselItem(
            R.drawable.joy_riding,
            stringResource(R.string.joy_riding),
            stringResource(R.string.joy_riding_desc)

        ),
        CarouselItem(
            R.drawable.community_features,
            stringResource(R.string.community_features),
            stringResource(R.string.community_features_desc)
        ),
        CarouselItem(
            R.drawable.ride_together_safely,
            stringResource(R.string.ride_together_safely),
            stringResource(R.string.ride_together_safely_desc)
        )
    )
    val carouselTopPadding = ((screenHeightPx * Constants.CAROUSEL_HEIGHT_RATIO) / density).dp
    val cardHeight = ((screenHeightPx * Constants.CARD_HEIGHT_RATIO) / density).dp
    val pagerState = rememberPagerState(pageCount = {
        carouselItems.size
    })


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Image(
            painter = painterResource(id = carouselItems[pagerState.currentPage].imageRes), "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Card(
            modifier = Modifier.Companion
                .height(cardHeight)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(
                topStart = Dimensions.radius40,
                topEnd = Dimensions.radius40
            ),

            colors = CardDefaults.cardColors(containerColor = PrimaryDarkerLightB75)

        ) {
            GradientButton(
                startColor = PrimaryBrighterLightW75,
                endColor = PrimaryDarkerLightB50,
                {
                    //TODO: Handle button click for get started
                },
                PaddingValues(
                    start = Dimensions.padding40,
                    bottom = Dimensions.padding69,
                    end = Dimensions.padding40
                ),
                buttonText = stringResource(R.string.get_started).uppercase(),
                showArrow = true
            )
        }
        Carousel(carouselItems, carouselTopPadding, pagerState)
    }
}
