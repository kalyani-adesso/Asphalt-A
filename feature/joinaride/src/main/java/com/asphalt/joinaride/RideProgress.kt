package com.asphalt.joinaride

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GreenLIGHT
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.VividRed
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.commonui.ui.RedButton
import com.asphalt.commonui.utils.ComposeUtils
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RideProgress(
    androidUserVM: AndroidUserVM = koinViewModel(),
    onClickEndRide :() -> Unit
) {

    val currentUser = androidUserVM.userState.collectAsState(null)


    ComposeUtils.CommonContentBox(
        isBordered = true,
        radius = Constants.DEFAULT_CORNER_RADIUS,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = Dimensions.spacing19, horizontal = Dimensions.spacing16)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    CircularNetworkImage(
                        modifier = Modifier.border(
                            width = Dimensions.size2pt5,
                            color = PrimaryDarkerLightB75,
                            shape = CircleShape
                        ),
                        size = Dimensions.size32,
                        imageUrl = "dashboardRideInvite.inviterProfilePicUrl",
                        placeholderPainter = painterResource(R.drawable.profile_placeholder)
                    )
                    Spacer(Modifier.width(Dimensions.size10))

                    Column {
                        Text(
                            text = "Ride in Progress",
                            style = TypographyBold.titleMedium,
                            fontSize = Dimensions.textSize16,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(end = Dimensions.size10)
                        )
                        Spacer(Modifier.height(Dimensions.size3))
                        Text(
                            text = "Group Navigation Active",
                            style = Typography.titleMedium,
                            color = NeutralDarkGrey,
                            fontSize = Dimensions.textSize12,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(end = Dimensions.size10)
                        )
                    }
                    Spacer(Modifier.height(Dimensions.size17))
                }
            }
            Spacer(modifier = Modifier.height(Dimensions.padding))

            Card(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = Dimensions.padding10, vertical = Dimensions.padding10),
                shape = RoundedCornerShape(Dimensions.size10),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp),
            ) {
                Row(modifier = Modifier.fillMaxWidth()
                    .padding(vertical = Dimensions.padding8,
                        horizontal = Dimensions.padding16),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(vertical = Dimensions.padding6)
                    ){
                        Box(contentAlignment = Alignment.BottomEnd) {

                            CircularNetworkImage(
                                modifier = Modifier.border(
                                    width = Dimensions.size2pt5,
                                    color = GreenLIGHT,
                                    shape = CircleShape
                                ),
                                size = Dimensions.padding40,
                                imageUrl = "ridersList.imgUrl" ?: ""
                            )
                            Image(
                                painter = painterResource(R.drawable.ic_online_icon),
                                contentDescription = "Online Status",
                                modifier = Modifier
                                    .size(Dimensions.size14)
                                    .align(Alignment.BottomEnd)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(
                        verticalArrangement = Arrangement.Center,

                    ) {
                        Text(
                            text = currentUser.value?.name.toString(),
                            style = TypographyBold.bodySmall,
                            color = NeutralBlack,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Spacer(modifier = Modifier.height(Dimensions.padding5))

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Image(
                                painter = painterResource(R.drawable.ic_electric_bolt),
                                contentDescription = "",
                                modifier = Modifier
                                    .width(Dimensions.size10)
                                    .height(Dimensions.size10),
                            )
                            Spacer(Modifier.width(Dimensions.size4))
                            Text(
                                text = "55 kph",
                                style = Typography.bodySmall.copy(fontSize = Dimensions.textSize12),
                                color = NeutralDarkGrey,
                                modifier = Modifier
                            )
                        }
                    }
                    Row(horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)) {
                        Button(
                            onClick = {
                                onClickEndRide.invoke()
                            },
                            modifier = Modifier.widthIn(min = 130.dp, max = 130.dp)
                                .height(28.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VividRed),
                            shape = RoundedCornerShape(30.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp)
                        ) {
                            Text(
                                text = "STOP TRACKING",
                                style = TypographyBold.bodySmall,
                                color = Color.White,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        }
                    }

                }
            }
            Spacer(modifier = Modifier.height(Dimensions.padding10))
            // end ride button
            RedButton(
                onClick = {
                    onClickEndRide.invoke()
                },
                modifier = Modifier
                    .height(Dimensions.size50)
                    .fillMaxWidth(),
                buttonRadius = Dimensions.size10, contentPaddingValues = PaddingValues(0.dp)
            ) {
                Text(
                    text = "END RIDE",
                    style = TypographyBold.bodySmall,
                    color = Color.White
                )
            }
        }
    }

}

@Preview
@Composable
fun RideProgressPreview() {

    RideProgress(onClickEndRide = {})

}