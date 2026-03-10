package com.asphalt.joinaride

import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.asphalt.android.model.connectedride.ConnectedRideDTO
import com.asphalt.android.model.message.MessageRoot
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.DarkBrown
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GreenLIGHT
import com.asphalt.commonui.theme.GreenLIGHT25
import com.asphalt.commonui.theme.LightGreen
import com.asphalt.commonui.theme.LightPink
import com.asphalt.commonui.theme.LightYellow
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralBrown
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralPink
import com.asphalt.commonui.theme.PrimaryBrighterLightW75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.commonui.util.PhoneCallUtils
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.commonui.utils.ComposeUtils.ColorIconRounded
import com.asphalt.joinaride.message.MessageScreenUI
import com.asphalt.joinaride.viewmodel.JoinRideViewModel
import com.asphalt.joinaride.viewmodel.MessageViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RidersGroupStatus(
    ridesData: RidesData,
    viewModel: JoinRideViewModel = koinViewModel(),
    androidUserVM: AndroidUserVM = koinViewModel(),

    ) {
    // Remember scroll state for the vertical scroll
    val scrollState = rememberScrollState()


//    val rideUsers by viewModel.rideUsers.collectAsState()

//    val riders by viewModel.joinedUsers.collectAsState()

//    val currentUser = androidUserVM.userState.collectAsState(null)

    val joinedRiders by viewModel.joinedUsers.collectAsState()

//    LaunchedEffect(currentUser) {
//        val userData = currentUser.value?.uid?.let { androidUserVM.getUser(it) }
//        viewModel.observeRideLocations(ridesData.ridesID.toString())
//        Log.d("TAG", "RidersGroupStatus userData: $userData")
//    }

    ComposeUtils.CommonContentBox(
        isBordered = true,
        radius = Constants.DEFAULT_CORNER_RADIUS,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .padding(
                    vertical = Dimensions.spacing19,
                    horizontal = Dimensions.spacing16
                )
                .wrapContentHeight(unbounded = false)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ColorIconRounded(backColor = NeutralPink, resId = R.drawable.ic_bike)

                Spacer(Modifier.width(Dimensions.size10))
                Text(
                    text = ("Group Status   (${if (joinedRiders.size > 0) joinedRiders.size - 1 else 0} Riders)"),
                    style = TypographyBold.titleMedium,
                    fontSize = Dimensions.textSize16,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = Dimensions.size20)
                )
            }
            Spacer(modifier = Modifier.height(Dimensions.size10))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
                    .background(
                        color = NeutralLightPaper,
                        shape = RoundedCornerShape(Dimensions.size10)
                    ), contentPadding = PaddingValues(bottom = Dimensions.padding10),
                verticalArrangement = Arrangement.spacedBy(Dimensions.padding6)
            ) {
                items(items = joinedRiders.filter { rideDTO -> rideDTO.userID != androidUserVM.getCurrentUserUID() }) { rider ->
//                    val userData= rideUsers.firstOrNull { it.ridesID == riders.userID }

                    Spacer(Modifier.height(Dimensions.padding10))
                    GroupRidersCard(rider, viewModel, androidUserVM)
                }
            }
        }
    }
}

@Composable
fun GroupRidersCard(
//    ridersList: List<ConnectedRideDTO>,
    riderData: ConnectedRideDTO,
    viewModel: JoinRideViewModel = koinViewModel(),
    androidUserVM: AndroidUserVM = koinViewModel(),
) {

    val currentUser = androidUserVM.userState.collectAsState(null)
    val userData = currentUser.value?.uid?.let { androidUserVM.getUser(it) }

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        MessageScreenUI(
            onSend = {},
            ridesData = riderData,
            onCancel = { showDialog = false }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimensions.size73)
            .padding(start = Dimensions.padding16, end = Dimensions.padding16),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // or use NeutralWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
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
                            imageUrl = "ridersList.imgUrl" ?: ""
                        )
                    }
                    Spacer(Modifier.width(Dimensions.size5))
                    Column(modifier = Modifier) {
                        Row() {
                            val name = androidUserVM.getUser(riderData?.userID ?: "")?.name
                            Log.d("TAG", "GroupRidersCard name: $name")
                            Text(
                                text = name ?: "",
                                style = TypographyBold.bodySmall,
                                color = NeutralBlack,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Spacer(Modifier.width(Dimensions.size5))
                            when (riderData?.status?.lowercase()) {
                                "connected" -> {
                                    Row(
                                        modifier = Modifier
                                            .background(
                                                color = LightGreen,
                                                shape = RoundedCornerShape(Dimensions.size5)
                                            )
                                            .height(Dimensions.padding16)
                                            .padding(
                                                start = Dimensions.size5,
                                                end = Dimensions.size5,
                                                //                      top = Dimensions.size5,
                                                //                      bottom =  Dimensions.size2pt5
                                            ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Spacer(Modifier.width(Dimensions.size4))
                                        Text(
                                            text = riderData.status,
                                            style = Typography.bodySmall.copy(fontSize = Dimensions.textSize12),
                                            color = GreenLIGHT25,
                                            modifier = Modifier
                                        )
                                    }
                                }

                                "delayed" -> {
                                    Row(
                                        modifier = Modifier
                                            .background(
                                                color = LightYellow,
                                                shape = RoundedCornerShape(Dimensions.size5)
                                            )
                                            .height(Dimensions.padding16)
                                            .padding(
                                                start = Dimensions.size5,
                                                end = Dimensions.size5,
                                                //                                            top = Dimensions.size5,
                                                //                                             bottom =  Dimensions.size2pt5
                                            ),
                                        verticalAlignment = Alignment.CenterVertically,


                                        ) {
                                        Spacer(Modifier.width(Dimensions.size4))
                                        Text(
                                            text = riderData.status,
                                            style = Typography.bodySmall.copy(fontSize = Dimensions.textSize12),
                                            color = NeutralBrown,
                                            modifier = Modifier
                                        )
                                    }
                                }

                                "stopped" -> {
                                    Row(
                                        modifier = Modifier
                                            .background(
                                                color = LightPink,
                                                shape = RoundedCornerShape(Dimensions.size5)
                                            )
                                            .height(Dimensions.padding16)
                                            .padding(
                                                start = Dimensions.size5,
                                                end = Dimensions.size5,
                                                //                                            top = Dimensions.size5,
                                                //                                             bottom =  Dimensions.size2pt5
                                            ),
                                        verticalAlignment = Alignment.CenterVertically,


                                        ) {
                                        Spacer(Modifier.width(Dimensions.size4))
                                        Text(
                                            text = riderData.status,
                                            style = Typography.bodySmall.copy(fontSize = Dimensions.textSize12),
                                            color = DarkBrown,
                                            modifier = Modifier
                                        )
                                    }
                                }
                            }
                        }
                        Row(modifier = Modifier.padding(top = Dimensions.padding5)) {

                            Image(
                                painter = painterResource(R.drawable.ic_electric_bolt),
                                contentDescription = "",
                                modifier = Modifier
                                    .width(Dimensions.size14)
                                    .height(Dimensions.size14),
                            )
                            Spacer(Modifier.width(Dimensions.size4))
                            Text(
                                text = riderData?.speedInKph?.toString() ?: "",
                                style = Typography.bodySmall.copy(fontSize = Dimensions.textSize12),
                                color = NeutralDarkGrey,
                                modifier = Modifier
                            )
                            Spacer(modifier = Modifier.width(Dimensions.padding5))
                            Image(
                                painter = painterResource(R.drawable.ic_circle),
                                contentDescription = "",
                                modifier = Modifier
                                    .width(Dimensions.size5)
                                    .height(Dimensions.size5)
                            )
                            Spacer(modifier = Modifier.width(Dimensions.padding5))
                            Text(
                                text = "Just Now",
                                style = Typography.bodySmall.copy(fontSize = Dimensions.textSize12),
                                color = NeutralDarkGrey,
                                modifier = Modifier
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(Dimensions.padding))
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        val context = LocalContext.current
                        Box(
                            modifier = Modifier
                                .clickable {
                                    riderData?.userID?.let {
                                        androidUserVM.getUser(it)?.contactNumber?.let { phoneNumber ->
                                            PhoneCallUtils.dialPhoneNumber(
                                                context,
                                                phoneNumber
                                            )
                                        }
                                    }
                                }
                        ) {
                            ColorIconRounded(
                                backColor = PrimaryBrighterLightW75,
                                resId = R.drawable.ic_call_white
                            )
                        }
                        Spacer(modifier = Modifier.width(Dimensions.padding10))
                        Box(
                            modifier = Modifier
                                .clickable {
                                    // onDeleteBike.invoke()
                                    Log.d("TAG", "GroupRidersCard: button clicked")
                                    showDialog = true

                                }

                        ) {
                            ColorIconRounded(
                                backColor = PrimaryBrighterLightW75,
                                resId = R.drawable.ic_message
                            )
                        }
                    }

                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RidersGroupStatusPreview(modifier: Modifier = Modifier) {

    //RidersGroupStatus()
}