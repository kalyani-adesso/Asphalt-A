package com.asphalt.marketplace.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.android.model.chat.getOtherUserId
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.BlueLite35
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GrayDark
import com.asphalt.commonui.theme.GrayLite34
import com.asphalt.commonui.theme.GreenLIGHT
import com.asphalt.commonui.theme.GreenLIGHT10
import com.asphalt.commonui.theme.GreenLIGHT25
import com.asphalt.commonui.theme.LightGray45
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryBrighterLightW75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.commonui.utils.Utils
import com.asphalt.marketplace.ui.composable.Buttons
import com.asphalt.marketplace.ui.composable.SpecificationRow

@Composable
fun ProductDetailsScreen(setTopAppBarState: (AppBarState) -> Unit) {
    setTopAppBarState(
        AppBarState(
            title = stringResource(R.string.product_details)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = NeutralWhite)
            .padding(start = Dimensions.padding16, end = Dimensions.padding16)
    ) {

        // Main content (above)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(NeutralLightPaper)
                .padding(vertical = Dimensions.padding16, horizontal = Dimensions.padding16)

        ) {
            Image(
                painter = painterResource(R.drawable.naked_bike),
                contentDescription = "",
                modifier = Modifier
                    .height(Dimensions.size200)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(NeutralWhite), contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(Dimensions.padding16))
            Text(text = "2020 Royal Enfield", style = TypographyBold.bodyMedium)
            Spacer(modifier = Modifier.height(Dimensions.padding10))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = GreenLIGHT10,
                            shape = RoundedCornerShape(Dimensions.size5)
                        )
                        .padding(
                            start = Dimensions.size5,
                            end = Dimensions.size5,
                        ), contentAlignment = Alignment.Center


                ) {
                    Text(
                        text = "Pre Owned Motorcycle",
                        style = Typography.bodySmall.copy(fontSize = Dimensions.textSize12),
                        color = GreenLIGHT25,
                        modifier = Modifier,
                    )
                }
                Column(modifier = Modifier, horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.price),
                        style = Typography.bodySmall.copy(fontSize = Dimensions.textSize12),
                        color = LightGray45,
                        modifier = Modifier,
                    )
                    Text(
                        text = "₹2,85,000",
                        style = TypographyBold.bodyLarge.copy(fontSize = Dimensions.textSize19),
                        color = BlueLite35,
                        modifier = Modifier,
                    )
                }
            }
            Spacer(modifier = Modifier.height(Dimensions.size16))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier
                            .height(Dimensions.padding20)
                            .width(Dimensions.padding20),
                        painter = painterResource(R.drawable.ic_location),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(PrimaryBrighterLightW75)
                    )
                    Spacer(modifier = Modifier.width(Dimensions.size10))
                    Text(
                        text = "Kakkand, Kochi",
                        style = Typography.bodyMedium,
                        color = GrayDark
                    )

                }

                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .height(Dimensions.padding16)
                            .width(Dimensions.padding16),
                        painter = painterResource(R.drawable.ic_clock_blue),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(Dimensions.size8))
                    Text(
                        text = "2 Days ago",
                        style = Typography.bodyMedium,
                        color = GrayLite34
                    )

                }
            }
            Spacer(modifier = Modifier.height(Dimensions.size16))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(NeutralWhite)
                    .padding(vertical = Dimensions.padding16, horizontal = Dimensions.padding16)

            ) {
                Spacer(modifier = Modifier.height(Dimensions.size25))
                Text(text = "Seller Information", style = TypographyBold.bodyMedium)
                Spacer(modifier = Modifier.height(Dimensions.size30))
                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier) {

                        CircularNetworkImage(
                            modifier = Modifier.border(
                                width = Dimensions.size2pt5,
                                color =
                                    GreenLIGHT,
                                shape = CircleShape
                            ),
                            size = Dimensions.padding40,
                            imageUrl = ""
                        )
                        Image(
                            painter = painterResource(R.drawable.ic_online_icon),
                            contentDescription = "Online Status",
                            modifier = Modifier
                                .size(Dimensions.size14)
                                .align(Alignment.BottomEnd)
                        )

                    }
                    Spacer(modifier = Modifier.width(Dimensions.size14))
                    Text(text = "Vyshnav", style = TypographyBold.bodyMedium)

                }
                Spacer(modifier = Modifier.height(Dimensions.size25))
                Text(text = "Description", style = TypographyBold.bodyMedium)
                Spacer(modifier = Modifier.height(Dimensions.size14))
                Text(text = "Excellent condition 2022 Royal Enfield Classic 350 with only 3,200 km. Always garaged and regularly maintained. Includes aftermarket exhaust, custom seat, and LED lighting. Clean RC in hand. First owner, all service records available.",
                    style = Typography.bodySmall)
                Spacer(modifier = Modifier.height(Dimensions.size14))
                Text(text = "Specifications", style = TypographyBold.bodyMedium)
                Spacer(modifier = Modifier.height(Dimensions.size8))
                SpecificationRow(heading1 = "Year", value1 = "2024", heading2 = "Kilometers", value2 = "2000 km")
                Spacer(modifier = Modifier.height(Dimensions.size8))
                SpecificationRow(heading1 = "Color", value1 = "Black", heading2 = "Owner", value2 = "First Owner")
                Spacer(modifier = Modifier.height(Dimensions.size8))
                SpecificationRow(heading1 = "Fuel Type", value1 = "Petrol", heading2 = "Insurance", value2 = "Valid till 2025")



            }

        }


        // Bottom button (fixed to screen bottom)
        Row(
            modifier = Modifier
                .padding(horizontal = Dimensions.padding16, vertical = Dimensions.padding16)
                .imePadding()
        ) {
            Buttons(
                stringResource(R.string.message),
                stringResource(R.string.make_offer)
            ) {
                //viewModel.validations()
            }
        }
    }
}


@Composable
@Preview
fun ProductDetailsPreview() {
    ProductDetailsScreen({})
}