package com.asphalt.welcome.composables

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.PrimaryBrighterLightW75
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.utils.ComposeUtils.calculateHeightDpForPercentage
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.welcome.GetStartedConstants
import com.asphalt.welcome.sealedclasses.Carousels

@Composable
fun GetStartedScreen(
    onNavigateToRegister: () -> Unit = {}
) {
    //val context = LocalContext.current

    val carouselItems = listOf(
        Carousels.JoyRideCarousel.carouselItem, Carousels.CommunityFeatureCarousel.carouselItem,
        Carousels.RideTogetherCarousel.carouselItem
    )
    val carouselTopPadding =
        calculateHeightDpForPercentage(GetStartedConstants.CAROUSEL_HEIGHT_RATIO)
    val cardHeight = calculateHeightDpForPercentage(GetStartedConstants.CARD_HEIGHT_RATIO)
    val pagerState = rememberPagerState(pageCount = {
        carouselItems.size
    })
    Scaffold { innerPadding ->
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Image(
                painter = painterResource(id = carouselItems[pagerState.currentPage].imageRes),
                null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Card(
                modifier = Modifier
                    .height(cardHeight)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(
                    topStart = Dimensions.radius40,
                    topEnd = Dimensions.radius40
                ),

                colors = CardDefaults.cardColors(containerColor = PrimaryDarkerLightB75)

            ) {}
            Column(modifier = Modifier.fillMaxSize()) {

                Carousel(carouselItems, carouselTopPadding, pagerState)
                Spacer(Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(
                            start = Dimensions.padding40,
                            end = Dimensions.padding40,
                            bottom = innerPadding.calculateBottomPadding()
                        ),
                ) {
                    Card(
                        modifier = Modifier.wrapContentSize(),
                        elevation = CardDefaults.cardElevation(Dimensions.padding1),
                        shape = RoundedCornerShape(Dimensions.radius15)
                    ) {
                        GradientButton(
                            startColor = PrimaryBrighterLightW75,
                            endColor = PrimaryDarkerLightB50,
                            onClick = onNavigateToRegister,
                               // Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
                            buttonText = stringResource(R.string.get_started).uppercase(),
                            showArrow = true
                        )
                    }
                }
                Spacer(Modifier.weight(1f))

            }

        }
    }
}
