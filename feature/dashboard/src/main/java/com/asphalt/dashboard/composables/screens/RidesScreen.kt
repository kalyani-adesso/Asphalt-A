package com.asphalt.dashboard.composables.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GrayDark
import com.asphalt.commonui.theme.GreenDark
import com.asphalt.commonui.theme.MagentaDeep
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.BorderedButton
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.util.GetGradient
import com.asphalt.commonui.utils.ComposeUtils.ColorIconRounded

@Composable
fun RidesScreen() {
    AsphaltTheme {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = NeutralWhite)
                .padding(start = Dimensions.padding16, end = Dimensions.padding16)
        ) {
            item {
                Spacer(Modifier.height(Dimensions.size30))
                ButtonTabs()
                Spacer(Modifier.height(Dimensions.padding16))
            }
//            items(10) { index ->
//
//            }
            item {
                UpcomingRides()
                Spacer(Modifier.height(Dimensions.padding16))
                HistoryRides()
            }


        }
    }
}

@Composable
fun UpcomingRides() {
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
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier) {
                ColorIconRounded(backColor = MagentaDeep, resId = R.drawable.ic_location)
                Spacer(modifier = Modifier.width(Dimensions.size5))
                Column {
                    Text("Weekend Coast Ride", style = TypographyMedium.titleMedium)
                    Text(
                        "Kochi - Kanyakumari",
                        style = Typography.bodySmall,
                        color = NeutralDarkGrey
                    )
                }

            }
            Box(
                modifier = Modifier
                    .background(
                        color = MagentaDeep,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(
                        start = Dimensions.padding16,
                        end = Dimensions.padding16,
                        top = Dimensions.padding8,
                        bottom = Dimensions.padding8
                    ), contentAlignment = Alignment.Center


            ) {
                Text(
                    "UPCOMING", style = TypographyMedium.bodySmall, color = NeutralWhite,
                    modifier = Modifier
                )
            }

        }
        Spacer(modifier = Modifier.height(Dimensions.size25))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .height(Dimensions.padding20)
                        .width(Dimensions.padding20),
                    painter = painterResource(R.drawable.ic_calendar_blue),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(Dimensions.size5))
                Text("Sun, Oct 21 - 09:00 AM", style = Typography.bodyMedium, color = GrayDark)

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
                Text("3 Riders", style = Typography.bodyMedium, color = GrayDark)

            }
        }
        Spacer(modifier = Modifier.height(Dimensions.size25))
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(
                Dimensions.padding16
            )
        ) {
            GradientButton(
                onClick = {

                },
                buttonHeight = Dimensions.size50,
                modifier = Modifier.weight(1f), contentPadding = PaddingValues(Dimensions.size0), buttonRadius = Dimensions.size10,
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        "SHARE",
                        style = TypographyMedium.bodySmall,
                        color = NeutralWhite,

                        )
                }

            }
//            Box(
//                modifier = Modifier
//                    .height(Dimensions.size50)
//                    .width(Dimensions.padding100)
//                    .background(
//                        brush = GetGradient(PrimaryDarkerLightB75, PrimaryDarkerLightB50),
//                        shape = RoundedCornerShape(Dimensions.size10)
//                    ), contentAlignment = Alignment.Center
//
//                // Rounded corners here
//
//            ) {
//                Text("SHARE", style = TypographyMedium.bodySmall, color = NeutralWhite)
//            }
            //Spacer(Modifier.width(Dimensions.padding15))
            BorderedButton(
                onClick = {

                },
                modifier = Modifier
                    .height(Dimensions.size50)
                    .background(NeutralWhite)
                    .weight(1.4f),
                buttonRadius = Dimensions.size10, contentPaddingValues = PaddingValues(0.dp)
            ) {
                Text(
                    "VIEW DETAILS",
                    style = TypographyMedium.bodySmall,
                    color = PrimaryDarkerLightB75
                )
            }

//            BorderedButton(
//                onClick = {
//
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(Dimensions.size50)
//                    .background(NeutralWhite),
//                buttonRadius = Dimensions.size10
//            ) {
//                Text(
//                    "VIEW DETAILS",
//                    style = TypographyMedium.bodySmall,
//                    color = PrimaryDarkerLightB75
//                )
//            }
        }
    }
}

@Composable
fun HistoryRides() {
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
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier) {

                ColorIconRounded(backColor = GreenDark, resId = R.drawable.ic_location)
                Spacer(modifier = Modifier.width(Dimensions.size5))
                Column {
                    Text("Weekend Coast Ride", style = TypographyMedium.titleMedium)
                    Text(
                        "Kochi - Kanyakumari",
                        style = Typography.bodySmall,
                        color = NeutralDarkGrey
                    )
                }

            }
            Box(
                modifier = Modifier
                    .background(
                        color = GreenDark,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(
                        start = Dimensions.padding16,
                        end = Dimensions.padding16,
                        top = Dimensions.padding8,
                        bottom = Dimensions.padding8
                    ), contentAlignment = Alignment.Center


            ) {
                Text(
                    "UPCOMING", style = TypographyMedium.bodySmall, color = NeutralWhite,
                    modifier = Modifier
                )
            }

        }
        Spacer(modifier = Modifier.height(Dimensions.size25))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .height(Dimensions.padding20)
                        .width(Dimensions.padding20),
                    painter = painterResource(R.drawable.ic_calendar_blue),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(Dimensions.size5))
                Text("Sun, Oct 21 - 09:00 AM", style = Typography.bodyMedium, color = GrayDark)

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
                Text("3 Riders", style = Typography.bodyMedium, color = GrayDark)

            }
        }
        Spacer(modifier = Modifier.height(Dimensions.size25))
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(
                Dimensions.padding16
            )
        ) {
            BorderedButton(
                onClick = {

                },
                modifier = Modifier
                    .height(Dimensions.size50)
                    .background(NeutralWhite)
                    .weight(1f),
                buttonRadius = Dimensions.size10, contentPaddingValues = PaddingValues(0.dp)
            ) {
                Text(
                    "VIEW PHOTOS",
                    style = TypographyMedium.bodySmall,
                    color = PrimaryDarkerLightB75
                )
            }
            //Spacer(Modifier.width(Dimensions.padding15))

            BorderedButton(
                onClick = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.size50)
                    .background(NeutralWhite)
                    .weight(1.4f),
                buttonRadius = Dimensions.size10, contentPaddingValues = PaddingValues(0.dp)
            ) {
                Text(
                    "SHARE EXPERIENCE",
                    style = TypographyMedium.bodySmall,
                    color = PrimaryDarkerLightB75
                )
            }
//            Box(
//                modifier = Modifier
//                    .height(Dimensions.size50)
//                    .background(
//                        brush = GetGradient(NeutralWhite, NeutralWhite),
//                        shape = RoundedCornerShape(Dimensions.size10)
//                    )
//                    .border(
//                        width = 1.dp,
//                        color = PrimaryDarkerLightB75,
//                        shape = RoundedCornerShape(Dimensions.size10)
//                    )
//                    .fillMaxWidth(), contentAlignment = Alignment.Center
//
//                // Rounded corners here
//
//            ) {
//                Text(
//                    "VIEW DETAILS",
//                    style = TypographyMedium.bodySmall,
//                    color = PrimaryDarkerLightB75
//                )
//            }
        }
    }
}

@Composable
fun ButtonTabs() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimensions.size92)
            .background(
                NeutralLightPaper,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(start = Dimensions.padding10, end = Dimensions.padding10),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Box(
            modifier = Modifier
                .height(Dimensions.size50)
                .width(100.dp)
                .background(
                    brush = GetGradient(PrimaryDarkerLightB75, PrimaryDarkerLightB50),
                    shape = RoundedCornerShape(16.dp)
                ), contentAlignment = Alignment.Center

            // Rounded corners here

        ) {
            Text("Up coming", style = TypographyMedium.titleMedium)
        }
        Box(
            modifier = Modifier
                .height(Dimensions.size50)
                .width(100.dp)
                .background(
                    color = NeutralWhite,
                    shape = RoundedCornerShape(16.dp)
                ), contentAlignment = Alignment.Center


        ) {
            Text("History", style = TypographyMedium.titleMedium)
        }
        Box(
            modifier = Modifier
                .height(Dimensions.size50)
                .width(100.dp)
                .background(
                    color = NeutralWhite,
                    shape = RoundedCornerShape(16.dp)
                ), contentAlignment = Alignment.Center


        ) {
            Text("Invite", style = TypographyMedium.titleMedium)
        }
    }
}

@Preview
@Composable
fun RidesPreview() {
    RidesScreen()
}