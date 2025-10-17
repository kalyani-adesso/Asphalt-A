package com.asphalt.createride.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.createride.ui.CreateRideEntry

@Composable
fun ShareSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = Dimensions.padding16,
                end = Dimensions.padding16,
            )
            .background(
                color = NeutralLightPaper, shape = RoundedCornerShape(Dimensions.size10)
            )
    ) {
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimensions.size200)
                .padding(start = Dimensions.padding16, end = Dimensions.padding16),
            colors = CardDefaults.cardColors(
                containerColor = Color.White // or use NeutralWhite
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_tick_green_10),
                    contentDescription = "",
                    modifier = Modifier.size(Dimensions.padding100),

                    )
                Spacer(Modifier.height(Dimensions.padding16))
                Text(text = "Ride Created!", style = TypographyBold.bodyMedium)
                Spacer(Modifier.height(Dimensions.padding16))
                Text(
                    text = "Share your ride with friends",
                    style = Typography.bodySmall,
                    color = NeutralDarkGrey
                )
            }

        }
        Spacer(Modifier.height(Dimensions.padding16))
        Column(
            modifier = Modifier.padding(
                start = Dimensions.padding16,
                end = Dimensions.padding16
            )
        ) {
            Text(text = "Share Link", style = TypographyBold.bodyMedium)
            Spacer(Modifier.height(Dimensions.padding16))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.padding50)
                    .background(
                        NeutralWhite, shape = RoundedCornerShape(Dimensions.size5)
                    )
                    .padding(start = Dimensions.size10, end = Dimensions.size10),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "https://adessoriderclub.app/12121312", style = Typography.bodySmall,
                    color = NeutralDarkGrey, modifier = Modifier.weight(1f),
                    maxLines = 1, overflow = TextOverflow.Ellipsis
                )
                Image(painter = painterResource(R.drawable.ic_copy_icon), contentDescription = "")
            }
            Spacer(Modifier.height(Dimensions.padding16))
            Text(text = "Share Via", style = TypographyBold.bodyMedium)
            Spacer(Modifier.height(Dimensions.padding16))
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(
                    Dimensions.size10
                )
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_whats_app_gradient),
                    contentDescription = "", modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(R.drawable.ic_face_book_gradient),
                    contentDescription = "",
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(R.drawable.ic_twit_gradient),
                    contentDescription = "",
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(R.drawable.ic_mail_gradient),
                    contentDescription = "",
                    modifier = Modifier.weight(1f)
                )
            }
        }

//Adding space
        Spacer(modifier = Modifier.height(Dimensions.padding16))
    }
}

@Composable
@Preview
fun SharePreview() {
    ShareSection()

}