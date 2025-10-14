package com.asphalt.createride.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NaturalGreen
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.ActionBarWithBack

@Composable
fun CreateRideEntry() {

    AsphaltTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = NeutralWhite)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                ActionBarWithBack(R.drawable.ic_arrow_back, "Create a Ride") { }
                TabSelection()
            }

            // Fixed bottom button
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(color = NeutralWhite) // optional: to hide scrolling content under it
            ) {
                Button(onClick = {
                    // Button action here
                }) {
                    Text(text = "Click")
                }
            }
        }
    }
}

@Composable
fun TabSelection() {
    Spacer(Modifier.height(Dimensions.size30))
    Row(modifier = Modifier.fillMaxWidth().padding(start = Dimensions.padding16, end = Dimensions.padding16),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing10)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .height(Dimensions.padding50)
                    .width(Dimensions.padding50)
                    .background(
                        color = PrimaryDarkerLightB75,
                        shape = RoundedCornerShape(Dimensions.padding10)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_path),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(NaturalGreen)
                )

            }
            Spacer(modifier = Modifier.height(Dimensions.size10))
            Text(
                text = "Details",
                style = TypographyBold.bodySmall,
                color = NeutralBlack, fontSize = Dimensions.textSize12,
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .height(Dimensions.padding50)
                    .width(Dimensions.padding50)
                    .background(
                        color = PrimaryDarkerLightB75,
                        shape = RoundedCornerShape(Dimensions.padding10)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_route),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(NaturalGreen),
                )

            }
            Spacer(modifier = Modifier.height(Dimensions.size10))
            Text(
                text = "Route",
                style = TypographyBold.bodySmall,
                color = NeutralBlack,fontSize = Dimensions.textSize12
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .height(Dimensions.padding50)
                    .width(Dimensions.padding50)
                    .background(
                        color = PrimaryDarkerLightB75,
                        shape = RoundedCornerShape(Dimensions.padding10)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_participants),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(NaturalGreen)
                )

            }
            Spacer(modifier = Modifier.height(Dimensions.size10))
            Text(
                text = "Participants",
                style = TypographyBold.bodySmall,
                color = NeutralBlack, maxLines = 1, fontSize = Dimensions.textSize12
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .height(Dimensions.padding50)
                    .width(Dimensions.padding50)
                    .background(
                        color = PrimaryDarkerLightB75,
                        shape = RoundedCornerShape(Dimensions.padding10)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_review),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(NaturalGreen)
                )

            }
            Spacer(modifier = Modifier.height(Dimensions.size10))
            Text(
                text = "Review",
                style = TypographyBold.bodySmall,
                color = NeutralBlack,fontSize = Dimensions.textSize12
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .height(Dimensions.padding50)
                    .width(Dimensions.padding50)
                    .background(
                        color = PrimaryDarkerLightB75,
                        shape = RoundedCornerShape(Dimensions.padding10)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(NaturalGreen)
                )

            }
            Spacer(modifier = Modifier.height(Dimensions.size10))
            Text(
                text = "Share",
                style = TypographyBold.bodySmall,
                color = NeutralBlack,fontSize = Dimensions.textSize12
            )
        }
    }

}

@Composable
@Preview
fun CreateRidePreview() {
    CreateRideEntry()

}

