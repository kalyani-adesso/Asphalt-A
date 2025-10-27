package com.asphalt.joinaride

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightGrey
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.ComposeUtils

@Composable
fun ConnectedRideEnd(
    modifier: Modifier = Modifier,
    setTopAppBarState: (AppBarState) -> Unit,
    ) {

    setTopAppBarState(AppBarState(title = stringResource(R.string.connected_ride)))

    Column(modifier = modifier.fillMaxSize()) {

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
                    painter = painterResource(com.asphalt.commonui.R.drawable.ic_tick_green_10),
                    contentDescription = "",
                    modifier = Modifier.size(Dimensions.padding100),

                    )
                Spacer(Modifier.height(Dimensions.padding16))
                Text(text = stringResource(id = R.string.ride_created), style = TypographyBold.bodyMedium)
                Spacer(Modifier.height(Dimensions.padding16))
                Text(
                    text = stringResource(R.string.share_with_friends),
                    style = Typography.bodySmall,
                    color = NeutralDarkGrey
                )
            }
        }

        ComposeUtils.CommonContentBox(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = Dimensions.padding16,
                    vertical = Dimensions.padding20
                ), verticalArrangement = Arrangement.spacedBy(Dimensions.padding16)
            ) {
                ComposeUtils.SectionTitle(stringResource(R.string.ride_difficulty))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.size17),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatView(1)
                    StatView(1)
                    StatView(1)
                }
            }
        }
    }


}

@Composable
fun RowScope.StatView(count: Int) {
    RoundedBox(
        modifier = Modifier.weight(1f),
        borderColor = NeutralLightGrey,
        borderStroke = Dimensions.spacing1,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimensions.padding15)
                .wrapContentWidth(),
            verticalArrangement = Arrangement.spacedBy(
                Dimensions.spacing10, Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(R.drawable.ic_location_purple), null)
            ComposeUtils.SectionTitle("$count ${stringResource(R.string.duration)}")
            ComposeUtils.SectionSubtitle(
                stringResource(R.string.duration),
                color = NeutralBlack, modifier = Modifier.offset(y = Dimensions.spacingNeg4)
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConnectedRideEndPreview(modifier: Modifier = Modifier) {

    MaterialTheme {
        ConnectedRideEnd(modifier = modifier.fillMaxSize()) {

        }
    }
}