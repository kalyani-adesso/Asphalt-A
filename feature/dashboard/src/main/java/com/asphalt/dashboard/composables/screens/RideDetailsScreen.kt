package com.asphalt.dashboard.composables.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GrayDark
import com.asphalt.commonui.theme.LightGreen30
import com.asphalt.commonui.theme.LightOrange
import com.asphalt.commonui.theme.MagentaDeep
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.RedLight
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.utils.ComposeUtils.ColorIconRounded

@Composable
fun RidesDetailsScreen(setTopAppBarState: (AppBarState) -> Unit) {
    val scrollState = rememberScrollState()
    setTopAppBarState(
        AppBarState(
            title = stringResource(R.string.create_a_ride),
        )
    )
    AsphaltTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = NeutralWhite)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(start = Dimensions.padding16, end = Dimensions.padding16),
                //contentPadding = PaddingValues(bottom = Dimensions.spacing250)
            ) {
                Spacer(Modifier.height(Dimensions.size30))
                HeaderSection()
                Spacer(Modifier.height(Dimensions.size20))
                CountSection()

            }
            GradientButton(
                onClick = {},
                modifier = Modifier.align(Alignment.BottomCenter)
            ) { }
        }

    }
}

@Composable
fun CountSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.padding16)
    ) {
        for (i in 1..3)
            when (i) {
                1 -> CountRow(LightGreen30)
                2 -> CountRow(LightOrange)
                3 -> CountRow(RedLight)
            }

    }
}

@Composable
fun RowScope.CountRow(textColor: Color) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = NeutralLightGrey,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = NeutralWhite,
                shape = RoundedCornerShape(16.dp)
            )
            .weight(1f), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(
                vertical = Dimensions.padding16,
                horizontal = Dimensions.padding16,
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("2", style = TypographyBold.bodyMedium, color = textColor)
            Spacer(modifier = Modifier.height(Dimensions.size5))
            Text("Confirmed", style = Typography.bodySmall,color = textColor)
        }
    }
}

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                NeutralLightPaper,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(
                start = Dimensions.padding16,
                top = Dimensions.padding20,
                end = Dimensions.padding16,
                bottom = Dimensions.padding16

            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f)) {
                ColorIconRounded(
                    backColor = MagentaDeep,
                    resId = R.drawable.ic_location
                )

                Spacer(modifier = Modifier.width(Dimensions.size5))
                Column {
                    Text(
                        text = "TiTle" ?: "",
                        style = TypographyMedium.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(Dimensions.size3))
                    Text(
                        text = "Place" ?: "",
                        style = Typography.bodySmall,
                        color = NeutralDarkGrey,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }
        }
        Spacer(modifier = Modifier.height(Dimensions.size25))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .height(Dimensions.padding20)
                        .width(Dimensions.padding20),
                    painter = painterResource(R.drawable.ic_calendar_blue),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(Dimensions.size5))
                Text(
                    text = "Sun, Oct 21 - 09:00 AM" ?: "",
                    style = Typography.bodyMedium,
                    color = GrayDark
                )

            }
            Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .height(Dimensions.padding20)
                        .width(Dimensions.padding20),
                    painter = painterResource(R.drawable.ic_group_blue),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(Dimensions.size5))
                Text(
                    text = "${3}" + " " + stringResource(R.string.riders),
                    style = Typography.bodyMedium,
                    color = GrayDark
                )

            }
        }
    }
}

@Preview
@Composable
fun RideDetailsPreview() {
    RidesDetailsScreen(setTopAppBarState = {})
}