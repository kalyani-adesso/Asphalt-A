package com.asphalt.dashboard.composables.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.android.constants.APIConstants
import com.asphalt.android.model.RidersList
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GrayDark
import com.asphalt.commonui.theme.GreenLIGHT
import com.asphalt.commonui.theme.LightBlue
import com.asphalt.commonui.theme.LightGreen30
import com.asphalt.commonui.theme.LightGreen40
import com.asphalt.commonui.theme.LightOrange
import com.asphalt.commonui.theme.MagentaDeep
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.RedLight
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.utils.ComposeUtils.ColorIconRounded
import com.asphalt.commonui.utils.Utils
import com.asphalt.dashboard.viewmodels.RidesDetailsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RidesDetailsScreen(
    rideId: String?, setTopAppBarState: (AppBarState) -> Unit,
    viewModel: RidesDetailsViewModel = koinViewModel()
) {
    //val ridesData by viewModel.ridesData
    //viewModel.getUserList()
    LaunchedEffect(Unit) {
        if (rideId != null)
            viewModel.getSingleRide(rideId)
    }
    val scrollState = rememberScrollState()
    setTopAppBarState(
        AppBarState(
            title = viewModel.ridesData.value?.rideTitle ?: ""
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
                HeaderSection(viewModel)
                Spacer(Modifier.height(Dimensions.size20))
                CountSection(viewModel)
                Spacer(Modifier.height(Dimensions.size20))
                UsersList(viewModel)
                Spacer(Modifier.height(Dimensions.size50))
            }

            GradientButton(
                onClick = {},
                startColor = LightGreen40,
                endColor = LightGreen40,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = Dimensions.padding16, vertical = Dimensions.padding16)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_navigate),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(Dimensions.size10))
                    Text(
                        "Start Ride".uppercase(),
                        color = NeutralWhite,
                        fontSize = Dimensions.textSize18,
                        style = TypographyBold.labelLarge,
                    )
                }
            }
        }
    }


}

@Composable
fun UsersList(viewModel: RidesDetailsViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = NeutralLightPaper, shape = RoundedCornerShape(Dimensions.size10)
            )
    ) {
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        val userList = viewModel.ridersList.value
        userList.forEach { user ->
            UserRow(user)
            Spacer(modifier = Modifier.height(Dimensions.padding16))
        }
    }
}

@Composable
fun UserRow(user: RidersList) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimensions.size73)
            .padding(start = Dimensions.padding16, end = Dimensions.padding16),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // or use NeutralWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Dimensions.padding16, end = Dimensions.padding16),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(Dimensions.padding40)) {

                        CircularNetworkImage(
                            modifier = Modifier.border(
                                width = Dimensions.size2pt5,
                                color = GreenLIGHT,
                                shape = CircleShape
                            ),
                            size = Dimensions.padding40,
                            imageUrl = user.profilePic ?: ""
                        )
                        Image(
                            painter = painterResource(R.drawable.ic_online_icon),
                            contentDescription = "Online Status",
                            modifier = Modifier
                                .size(Dimensions.size14)
                                .align(Alignment.BottomEnd)
                        )
                    }
                    Spacer(Modifier.width(Dimensions.size5))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = user.name,
                                style = TypographyBold.bodySmall,
                                color = NeutralBlack,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Spacer(Modifier.height(Dimensions.size10))

                            Text(
                                text = stringResource(user.displayStatusString),
                                style = Typography.bodySmall,
                                color = NeutralDarkGrey,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis, fontSize = Dimensions.textSize12
                            )
                        }
                        Box(
                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (user.isOrganizer) {
                                Row(
                                    modifier = Modifier
                                        .background(
                                            color = LightBlue,
                                            shape = RoundedCornerShape(Dimensions.size5)
                                        )
                                        .padding(
                                            vertical = Dimensions.size5,
                                            horizontal = Dimensions.size10,
//                                            top = Dimensions.size5,
//                                             bottom =  Dimensions.size2pt5
                                        ),
                                    verticalAlignment = Alignment.CenterVertically,


                                    ) {

                                    Text(
                                        text = stringResource(R.string.organizer),
                                        style = Typography.bodySmall.copy(fontSize = Dimensions.textSize12),
                                        color = PrimaryDarkerLightB75,
                                        modifier = Modifier
                                    )
                                }
                            } else {
                                Image(
                                    painter = if (user.inviteStatus == APIConstants.RIDE_ACCEPTED)
                                        painterResource(R.drawable.ic_tick_accept)
                                    else if (user.inviteStatus == APIConstants.RIDE_INVITED)
                                        painterResource(R.drawable.ic_pending)
                                    else
                                        painterResource(R.drawable.ic_declined),
                                    contentDescription = "Online Status",
                                    modifier = Modifier
                                        .size(Dimensions.size25)

                                )
                            }
                        }

                    }
                }
            }
        }
    }
}


@Composable
fun CountSection(viewModel: RidesDetailsViewModel) {
    val participants = viewModel.ridesData.value?.participants ?: emptyList()

    val acceptedCount = participants.count { it.inviteStatus == APIConstants.RIDE_ACCEPTED }
    val pendingCount = participants.count { it.inviteStatus == APIConstants.RIDE_INVITED }
    val declinedCount = participants.count { it.inviteStatus == APIConstants.RIDE_DECLINED }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.padding16)
    ) {

        CountRow(
            LightGreen30,
            acceptedCount,
            stringResource(R.string.confirmed)
        )

        CountRow(
            LightOrange,
            pendingCount,
            stringResource(R.string.pending)
        )

        CountRow(
            RedLight,
            declinedCount,
            stringResource(R.string.decline)
        )
    }

}


@Composable
fun RowScope.CountRow(textColor: Color, count: Int, label: String) {
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
            Text(text = count.toString(), style = TypographyBold.bodyMedium, color = textColor)
            Spacer(modifier = Modifier.height(Dimensions.size5))
            Text(label, style = Typography.bodySmall, color = textColor)
        }
    }
}

@Composable
fun HeaderSection(viewModel: RidesDetailsViewModel) {
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
                        text = viewModel.ridesData.value?.rideTitle ?: "",
                        style = TypographyMedium.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(Dimensions.size3))
                    Text(
                        text = ("${viewModel.ridesData.value?.startLocation ?: ""} - " +
                                "${viewModel.ridesData.value?.endLocation ?: ""}"),
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
                    text = viewModel.ridesData.value?.startDate?.let {
                        Utils.getDateWithTime(
                            viewModel.ridesData.value?.startDate
                        )
                    } ?: "",
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
                    text = "${viewModel.ridesData.value?.participants?.size ?: ""}" + " " + stringResource(
                        R.string.riders
                    ),
                    style = Typography.bodyMedium,
                    color = GrayDark
                )

            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun RideDetailsPreview() {
    val viewModel = RidesDetailsViewModel()
    RidesDetailsScreen(null, setTopAppBarState = {}, viewModel)
}