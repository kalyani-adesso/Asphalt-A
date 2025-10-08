package com.asphalt.dashboard.composables.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralGrey30
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralMidGrey
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBlack
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.VividRed
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.commonui.utils.Constants
import com.asphalt.commonui.utils.Utils
import com.asphalt.dashboard.constants.DashboardInvitesConstants
import com.asphalt.dashboard.data.RideInvite

@Composable
fun RideInviteList(rideInvites: List<RideInvite>) {
    LazyRow {
        items(rideInvites) {
            RideInviteUI(it)
        }

    }
}

@Composable
fun RideInviteUI(rideInvite: RideInvite) {
    RoundedBox(
        modifier = Modifier
            .width(
                ComposeUtils.run {
                    getDpForScreenRatio(
                        DashboardInvitesConstants.UPCOMING_INVITES_WIDTH_RATIO, getScreenWidth()
                    )
                })
            .wrapContentHeight()
            .padding(end = Dimensions.padding20),
        backgroundColor = NeutralLightPaper,
        borderColor = NeutralGrey30,
        borderStroke = Dimensions.padding1
    ) {
        Column(
            modifier = Modifier
                .padding(
                    vertical = Dimensions.spacing19, horizontal = Dimensions.spacing16
                )
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularNetworkImage(
                        modifier = Modifier.border(
                            width = Dimensions.size2pt5,
                            color = PrimaryDarkerLightB75,
                            shape = CircleShape
                        ), size = Dimensions.size32, imageUrl = rideInvite.inviterProfilePicUrl
                    )
                    Spacer(Modifier.width(Dimensions.size10))
                    Column {
                        Text(
                            stringResource(R.string.invite_from, rideInvite.inviterName),
                            style = TypographyBold.titleMedium,
                            fontSize = Dimensions.textSize16
                        )
                        Spacer(Modifier.height(Dimensions.size3))
                        Text(
                            stringResource(
                                R.string.start_to_destination,
                                rideInvite.startPoint,
                                rideInvite.destination
                            ),
                            style = Typography.titleMedium,
                            color = NeutralDarkGrey,
                            fontSize = Dimensions.textSize12
                        )
                    }
                }
                RoundedBox(
                    modifier = Modifier.size(Dimensions.size30),
                    cornerRadius = Dimensions.size10,
                    backgroundColor = PrimaryDarkerLightB75
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(R.drawable.ic_message),
                            null,
                            modifier = Modifier.clickable {
                                //TODO:Click action for message
                            })
                    }
                }

            }
            Spacer(Modifier.height(Dimensions.size17))
            Row {
                Image(painterResource(R.drawable.ic_calendar_blue), null)
                Spacer(Modifier.width(Dimensions.size5))
                Text(
                    text = Utils.formatDateTime(rideInvite.dateTime),
                    style = Typography.titleMedium,
                    fontSize = Dimensions.textSize12
                )

            }
            Spacer(Modifier.height(Dimensions.size17))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Image(
                        painterResource(R.drawable.ic_group_blue), null, modifier = Modifier.size(
                            width = Dimensions.size12pt83, height = Dimensions.size9pt33
                        )
                    )
                    Spacer(Modifier.width(Dimensions.size5))
                    Text(
                        text = stringResource(
                            R.string.people_joined, rideInvite.inviteesProfilePicUrls.size
                        ), style = Typography.titleMedium, fontSize = Dimensions.textSize12
                    )
                }
                AvatarRow(rideInvite.inviteesProfilePicUrls)


            }
            Spacer(Modifier.height(Dimensions.size20))

            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    Dimensions.size8, Alignment.CenterHorizontally
                ), modifier = Modifier.fillMaxWidth()
            ) {
                GradientButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        //TODO:Accept Invite action

                    },
                    buttonHeight = Dimensions.size50,
                    contentPadding = PaddingValues(Dimensions.size0)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            stringResource(R.string.accept).uppercase(),
                            color = NeutralWhite,
                            style = TypographyBold.titleMedium,
                            fontSize = Dimensions.textSize14
                        )
                    }
                }
                Button(
                    {
                        //TODO:Decline Invite action
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VividRed),
                    modifier = Modifier
                        .weight(1f)
                        .height(Dimensions.size50),
                    shape = RoundedCornerShape(Constants.DEFAULT_CORNER_RADIUS)
                ) {
                    Text(
                        stringResource(R.string.decline).uppercase(),
                        color = NeutralWhite,
                        style = TypographyBold.titleMedium,
                        fontSize = Dimensions.textSize14
                    )
                }
            }

        }
    }

}

@Composable
fun AvatarRow(imageUrls: List<String>) {
    val maxVisible = DashboardInvitesConstants.INVITE_AVATAR_MAX
    val visibleImages = imageUrls.take(maxVisible)
    val extraCount = imageUrls.size - maxVisible
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            Dimensions.size4, Alignment.CenterHorizontally
        )
    ) {
        visibleImages.forEach { imageUrl ->
            CircularNetworkImage(
                imageUrl = imageUrl, size = Dimensions.size20, modifier = Modifier.border(
                    width = Dimensions.size1pt5, shape = CircleShape, color = NeutralMidGrey
                )
            )
        }
        if (extraCount > 0) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(PrimaryDarkerLightB75)
                    .border(
                        width = Dimensions.size1pt5, shape = CircleShape, color = NeutralMidGrey
                    )
                    .defaultMinSize(Dimensions.size20, Dimensions.size20),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "+$extraCount",
                    color = NeutralWhite,
                    fontSize = Dimensions.textSize12,
                    style = TypographyBlack.bodySmall
                )
            }
        }
    }
}

@Preview
@Composable
fun RideInvitePreview() {
    RideInviteList(
        listOf(
            RideInvite(
                "Name",
                "https://picsum.photos/id/1/200/300",
                "Kanyakumari",
                "Goa",
                "26/10/2025 12:30",
                listOf(
                    "https://picsum.photos/id/1/200/300",
                    "https://picsum.photos/id/2/200/300",
                    "https://picsum.photos/id/3/200/300",
                    "https://picsum.photos/id/4/200/300",
                    "https://picsum.photos/id/5/200/300",

                    )
            ), RideInvite("", "", "", "", "", listOf())
        )
    )
}