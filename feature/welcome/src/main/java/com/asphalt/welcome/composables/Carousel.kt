package com.asphalt.welcome.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.NeutralWhite25
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.commonui.utils.ComposeUtils.getDpForScreenRatio
import com.asphalt.welcome.GetStartedConstants
import com.asphalt.welcome.data.CarouselItem

@Composable
fun Carousel(carouselItems: List<CarouselItem>, carouselTopPadding: Dp, pagerState: PagerState) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = carouselTopPadding, bottom = Dimensions.padding28)
                .align(Alignment.CenterHorizontally)
        ) { page ->
            val carouselItem = carouselItems[page]
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = carouselItem.imageRes), null,
                    modifier = Modifier
                        .height(
                            getDpForScreenRatio(
                                GetStartedConstants.CAROUSEL_IMAGE_HEIGHT_RATIO,
                                ComposeUtils.getScreenHeight()
                            )
                        )
                        .aspectRatio(GetStartedConstants.CAROUSEL_IMAGE_ASPECT_RATIO)
                        .border(
                            width = Dimensions.size2,
                            shape = RoundedCornerShape(Dimensions.radius40),
                            color = Color.White
                        )
                        .clip(RoundedCornerShape(Dimensions.radius40)),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = Dimensions.padding28),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration)
                        NeutralWhite
                    else
                        NeutralWhite25
                Box(
                    modifier = Modifier
                        .padding(Dimensions.padding4)
                        .clip(CircleShape)
                        .background(color)
                        .size(Dimensions.size8)
                )
            }
        }

        Text(
            stringResource(carouselItems[pagerState.currentPage].titleRes),
            color = NeutralWhite,
            style = TypographyBold.headlineSmall,
            lineHeight = Dimensions.lineSpacing30,
            fontSize = Dimensions.textSize26,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(Dimensions.size20))
        Text(
            stringResource(carouselItems[pagerState.currentPage].descriptionRes),
            color = NeutralWhite,
            style = Typography.titleMedium,
            lineHeight = Dimensions.lineSpacing20,
            fontSize = Dimensions.textSize17,
            textAlign = TextAlign.Center
        )
    }

}