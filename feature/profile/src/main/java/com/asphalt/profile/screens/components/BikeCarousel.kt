package com.asphalt.profile.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.LightGrayishBlue
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.profile.sealedclasses.BikeType
import com.asphalt.profile.viewmodels.AddBikesVM
import kotlinx.coroutines.launch

@Composable
fun BikeCarousel(addBikesVM: AddBikesVM) {
    val bikeCarousels = BikeType.getAllTypes()
    val pagerState = rememberPagerState(pageCount = {
        bikeCarousels.size
    })
    val coroutineScope = rememberCoroutineScope()
    val leftEnabled by remember {
        derivedStateOf { pagerState.currentPage > 0 }
    }
    val rightEnabled by remember {
        derivedStateOf { pagerState.currentPage < pagerState.pageCount - 1 }
    }
    val selectedBikeType by remember {   derivedStateOf { bikeCarousels[pagerState.currentPage].id }}
    LaunchedEffect(selectedBikeType) {
        addBikesVM.updateBikeType(selectedBikeType)
    }
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            ComposeUtils.ArrowView(
                backColor = NeutralWhite,
                modifier = Modifier.clickable(enabled = leftEnabled) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }
            ) {
                Image(
                    painter = painterResource(if (leftEnabled) R.drawable.ic_prev_enabled else R.drawable.ic_prev_disabled),
                    null,
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ComposeUtils.SectionSubtitle(
                    stringResource(
                        R.string.of,
                        pagerState.currentPage + 1,
                        pagerState.pageCount
                    ),
                    color = NeutralBlack
                )
                Row {
                    repeat(pagerState.pageCount) { iteration ->
                        val color =
                            if (pagerState.currentPage == iteration)
                                PrimaryDarkerLightB75
                            else
                                LightGrayishBlue
                        Box(
                            modifier = Modifier
                                .padding(Dimensions.padding4)
                                .clip(CircleShape)
                                .background(color)
                                .size(Dimensions.size9)
                        )
                    }
                }
            }
            ComposeUtils.ArrowView(
                backColor = NeutralWhite,
                modifier = Modifier.clickable(enabled = rightEnabled) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            ) {
                Image(
                    painter = painterResource(if (rightEnabled) R.drawable.ic_next_enabled else R.drawable.ic_next_disabled),
                    null,
                )
            }
        }
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) { page ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Dimensions.spacing15),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painterResource(bikeCarousels[page].imageRes),
                    null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
                ComposeUtils.SectionTitle(stringResource(bikeCarousels[page].typeRes))

            }
        }
    }
}