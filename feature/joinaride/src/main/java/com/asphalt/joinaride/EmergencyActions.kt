package com.asphalt.joinaride

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GreenDark
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.OrangeLight
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.commonui.utils.ComposeUtils.ColorIconRounded

@Composable
fun EmergecyActions(modifier: Modifier = Modifier) {

    ComposeUtils.CommonContentBox(
        isBordered = true,
        radius = Constants.DEFAULT_CORNER_RADIUS,
        modifier = Modifier
            .fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(
                    vertical = Dimensions.spacing19, horizontal = Dimensions.spacing16
                )
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ColorIconRounded(backColor = GreenDark, resId = R.drawable.local_police)

                Spacer(Modifier.width(Dimensions.size10))

                Text(
                    text = ("Emergency Actions"),
                    style = TypographyBold.titleMedium,
                    fontSize = Dimensions.textSize16,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = Dimensions.size10)
                )

            }
            Spacer(modifier = Modifier.height(Dimensions.size20))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                ElevatedButton(
                    onClick = {
                        // navigateToEndRide.invoke()

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeutralWhite),
                    shape = RoundedCornerShape(Dimensions.padding10),
                    modifier = modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = Dimensions.padding10, vertical = Dimensions.padding6)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.local_police),
                        contentDescription = "Call",
                        tint = GreenDark
                    )

                    Text(
                        stringResource(R.string.sos),
                        color = NeutralBlack,
                        style = TypographyBold.titleSmall,
                        modifier = Modifier.padding(start = Dimensions.padding2)

                    )
                }
                Spacer(modifier = Modifier.width(Dimensions.size20))
                ElevatedButton(
                    onClick = {
                        // navigateToEndRide.invoke()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeutralWhite),
                    shape = RoundedCornerShape(Dimensions.padding10),
                    modifier = modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = Dimensions.padding10, vertical = Dimensions.padding6)

                ) {

                    Icon(
                        painter = painterResource(R.drawable.moved_location),
                        contentDescription = "Call",
                        tint = OrangeLight
                    )

                    Text(
                        stringResource(R.string.share_location),
                        color = NeutralBlack,
                        style = TypographyBold.titleSmall,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmergecyActionsPreview(modifier: Modifier = Modifier) {

    EmergecyActions()
}
