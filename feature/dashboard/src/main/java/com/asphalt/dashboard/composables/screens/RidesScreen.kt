package com.asphalt.dashboard.composables.screens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asphalt.android.constants.APIConstants
import com.asphalt.android.datastore.DataStoreManager
import com.asphalt.android.network.KtorClient
import com.asphalt.android.network.user.UserAPIServiceImpl
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.repository.user.UserRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GrayDark
import com.asphalt.commonui.theme.GreenDark
import com.asphalt.commonui.theme.MagentaDeep
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.theme.VividOrangeLight
import com.asphalt.commonui.theme.VividRed
import com.asphalt.commonui.ui.BorderedButton
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.util.GetGradient
import com.asphalt.commonui.utils.ComposeUtils.ColorIconRounded
import com.asphalt.dashboard.constants.RideStatConstants
import com.asphalt.dashboard.constants.RideStatConstants.UPCOMING
import com.asphalt.dashboard.data.YourRideDataModel
import com.asphalt.dashboard.viewmodels.RidesScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RidesScreen(
    ridesScreenViewModel: RidesScreenViewModel = koinViewModel(),
    setTopAppBarState: (AppBarState) -> Unit
) {
    setTopAppBarState(AppBarState(title = stringResource(R.string.your_rides)))
    LaunchedEffect(Unit) {
        //ridesScreenViewModel.getRides()
    }
    ridesScreenViewModel.getRides()
    AsphaltTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = NeutralWhite)
        ) {
//            ActionBarWithBack(R.drawable.ic_arrow_back, stringResource(R.string.your_rides)) {
//                // Handle back press
//            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = Dimensions.padding16,
                        end = Dimensions.padding16
                    ), contentPadding = PaddingValues(bottom = Dimensions.size30)
            ) {
                item {
                    Spacer(Modifier.height(Dimensions.size30))
                    ButtonTabs(ridesScreenViewModel)
                    Spacer(Modifier.height(Dimensions.padding16))
                }
//            items(10) { index ->
//
//            }
                when (ridesScreenViewModel.tabSelectFlow.value) {
                    RideStatConstants.UPCOMING_RIDE -> {
                        items(ridesScreenViewModel.ridesListState.value.upcoming) { upconing ->
                            UpcomingRides(ridesScreenViewModel, upconing)
                            Spacer(Modifier.height(Dimensions.padding16))
                        }

                    }

                    RideStatConstants.HISTORY_RIDES -> {
                        items(ridesScreenViewModel.ridesListState.value.history) { history ->
                            HistoryRides(ridesScreenViewModel, history)
                            Spacer(Modifier.height(Dimensions.padding16))
                        }
                    }

                    RideStatConstants.INVITES_RIDES -> {
                        items(ridesScreenViewModel.ridesListState.value.invite) { invites ->
                            Invites(ridesScreenViewModel, invites)
                            Spacer(Modifier.height(Dimensions.padding16))
                        }
                    }

                }


            }
        }
    }
}

@Composable
fun UpcomingRides(ridesScreenViewModel: RidesScreenViewModel, upconing: YourRideDataModel) {
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
            Row(modifier = Modifier.weight(1f)) {
                if (upconing.rideStatus == UPCOMING) {
                    ColorIconRounded(backColor = MagentaDeep, resId = R.drawable.ic_location)
                } else {
                    ColorIconRounded(backColor = VividOrangeLight, resId = R.drawable.ic_location)
                }

                Spacer(modifier = Modifier.width(Dimensions.size5))
                Column {
                    Text(
                        text = upconing.title ?: "",
                        style = TypographyMedium.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(Dimensions.size3))
                    Text(
                        text = upconing.place ?: "",
                        style = Typography.bodySmall,
                        color = NeutralDarkGrey
                    )
                }

            }
            Box(
                modifier = Modifier
                    .height(Dimensions.size30)
                    .then(
                        if (upconing.rideStatus == UPCOMING) {
                            Modifier.background(
                                color = MagentaDeep,
                                shape = RoundedCornerShape(Dimensions.size10)
                            )
                        } else {
                            Modifier.background(
                                color = VividOrangeLight,
                                shape = RoundedCornerShape(Dimensions.size10)
                            )
                        }
                    )

                    .padding(
                        start = Dimensions.padding16,
                        end = Dimensions.padding16,
                        top = Dimensions.padding8,
                        bottom = Dimensions.padding8
                    ), contentAlignment = Alignment.Center


            ) {
                Text(
                    text = upconing.rideStatus ?: "",
                    style = TypographyMedium.bodySmall,
                    color = NeutralWhite,
                    modifier = Modifier.align(alignment = Alignment.Center)
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
                Text(text = upconing.date ?: "", style = Typography.bodyMedium, color = GrayDark)

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
                    text = "${upconing.riders}" + " " + stringResource(R.string.riders),
                    style = Typography.bodyMedium,
                    color = GrayDark
                )

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
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(Dimensions.size0),
                buttonRadius = Dimensions.size10,
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        stringResource(R.string.share).uppercase(),
                        style = TypographyMedium.bodySmall,
                        color = NeutralWhite,

                        )
                }

            }
//
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
                    text = if (upconing.rideStatus == UPCOMING) {
                        stringResource(R.string.view_details).uppercase()
                    } else {
                        stringResource(R.string.check_responses).uppercase()
                    },
                    style = TypographyMedium.bodySmall,
                    color = PrimaryDarkerLightB75
                )
            }

//
        }
    }
}

@Composable
fun HistoryRides(ridesScreenViewModel: RidesScreenViewModel, history: YourRideDataModel) {
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
                    Text(
                        text = history.title ?: "",
                        style = TypographyMedium.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(Dimensions.size3))
                    Text(
                        text = history.place ?: "",
                        style = Typography.bodySmall,
                        color = NeutralDarkGrey
                    )
                }

            }
            Box(
                modifier = Modifier
                    .height(Dimensions.size30)
                    .background(
                        color = GreenDark,
                        shape = RoundedCornerShape(Dimensions.size10)
                    )
                    .padding(
                        start = Dimensions.padding16,
                        end = Dimensions.padding16,
                        top = Dimensions.padding8,
                        bottom = Dimensions.padding8
                    ), contentAlignment = Alignment.Center


            ) {
                Text(
                    text = stringResource(R.string.completed).uppercase(),
                    style = TypographyMedium.bodySmall,
                    color = NeutralWhite,
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
                Text(text = history.date ?: "", style = Typography.bodyMedium, color = GrayDark)

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
                    text = "${history.riders}" + " " + stringResource(R.string.riders),
                    style = Typography.bodyMedium,
                    color = GrayDark
                )

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
                    text = stringResource(R.string.view_photos).uppercase(),
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
                    text = stringResource(R.string.share_exp).uppercase(),
                    style = TypographyMedium.bodySmall,
                    color = PrimaryDarkerLightB75
                )
            }
        }
    }
}

@Composable
fun Invites(ridesScreenViewModel: RidesScreenViewModel, invites: YourRideDataModel) {
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

                CircularNetworkImage(
                    modifier = Modifier.border(
                        width = Dimensions.size2pt5,
                        color = PrimaryDarkerLightB75,
                        shape = CircleShape
                    ), size = Dimensions.size32, imageUrl = invites.profileImageUrl?:""
                )
                Spacer(modifier = Modifier.width(Dimensions.size5))
                Column {
                    Text(
                        text = stringResource(R.string.invite_from, invites.createdUSerName ?: ""),
                        style = TypographyMedium.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(Dimensions.size3))
                    Text(
                        text = invites.place ?: "",
                        style = Typography.bodySmall,
                        color = NeutralDarkGrey
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
                Text(text = invites.date ?: "", style = Typography.bodyMedium, color = GrayDark)

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
                    text = "${invites.riders}" + " " + stringResource(R.string.riders),
                    style = Typography.bodyMedium,
                    color = GrayDark
                )

            }
        }
        Spacer(modifier = Modifier.height(Dimensions.size25))
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(
                Dimensions.padding16
            )
        ) {
            GradientButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    ridesScreenViewModel.acceptDeclined(invites.ridesId?:"",APIConstants.RIDE_ACCEPTED)

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
                    ridesScreenViewModel.acceptDeclined(invites.ridesId?:"",APIConstants.RIDE_DECLINED)
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

@Composable
fun ButtonTabs(ridesScreenViewModel: RidesScreenViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimensions.size92)
            .background(
                NeutralLightPaper,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(start = Dimensions.size10, end = Dimensions.size10),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimensions.size10),

        ) {
        Box(
            modifier = Modifier
                .height(Dimensions.size50)
                .width(100.dp)
                .weight(1f)
                .then(
                    if (ridesScreenViewModel.tabSelectFlow.value == RideStatConstants.UPCOMING_RIDE) {
                        Modifier.background(
                            brush = GetGradient(PrimaryDarkerLightB75, PrimaryDarkerLightB50),
                            shape = RoundedCornerShape(Dimensions.size10)
                        )
                    } else {
                        Modifier.background(
                            color = NeutralWhite,
                            shape = RoundedCornerShape(Dimensions.size10)
                        )
                    }
                )
                .clickable {
                    ridesScreenViewModel.updateTab(RideStatConstants.UPCOMING_RIDE)
                }, contentAlignment = Alignment.Center

            // Rounded corners here

        ) {
            Text(
                text = stringResource(R.string.upcoming),
                style = TypographyMedium.titleMedium,
                color = if (ridesScreenViewModel.tabSelectFlow.value == RideStatConstants.UPCOMING_RIDE) {
                    NeutralWhite
                } else {
                    NeutralBlack
                }
            )
        }
        Box(
            modifier = Modifier
                .height(Dimensions.size50)
                .width(100.dp)
                .weight(1f)
                .then(
                    if (ridesScreenViewModel.tabSelectFlow.value == RideStatConstants.HISTORY_RIDES) {
                        Modifier.background(
                            brush = GetGradient(PrimaryDarkerLightB75, PrimaryDarkerLightB50),
                            shape = RoundedCornerShape(Dimensions.size10)
                        )
                    } else {
                        Modifier.background(
                            color = NeutralWhite,
                            shape = RoundedCornerShape(Dimensions.size10)
                        )
                    }
                )
                .clickable {
                    ridesScreenViewModel.updateTab(RideStatConstants.HISTORY_RIDES)
                }, contentAlignment = Alignment.Center


        ) {
            Text(
                text = stringResource(R.string.history),
                style = TypographyMedium.titleMedium,
                color = if (ridesScreenViewModel.tabSelectFlow.value == RideStatConstants.HISTORY_RIDES) {
                    NeutralWhite
                } else {
                    NeutralBlack
                }
            )
        }
        Box(
            modifier = Modifier
                .height(Dimensions.size50)
                .width(100.dp)
                .weight(1f)
                .then(
                    if (ridesScreenViewModel.tabSelectFlow.value == RideStatConstants.INVITES_RIDES) {
                        Modifier.background(
                            brush = GetGradient(PrimaryDarkerLightB75, PrimaryDarkerLightB50),
                            shape = RoundedCornerShape(Dimensions.size10)
                        )
                    } else {
                        Modifier.background(
                            color = NeutralWhite,
                            shape = RoundedCornerShape(Dimensions.size10)
                        )
                    }
                )
                .clickable {
                    ridesScreenViewModel.updateTab(RideStatConstants.INVITES_RIDES)
                }, contentAlignment = Alignment.Center


        ) {
            Text(
                text = stringResource(R.string.invite),
                style = TypographyMedium.titleMedium,
                color = if (ridesScreenViewModel.tabSelectFlow.value == RideStatConstants.INVITES_RIDES) {
                    NeutralWhite
                } else {
                    NeutralBlack
                }
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun RidesPreview() {
    var dataStoreManager = DataStoreManager(LocalContext.current)
    var androidVM = AndroidUserVM(UserRepoImpl(),dataStoreManager,
        UserRepository(UserAPIServiceImpl(KtorClient())))
    var ridesScreenViewModel: RidesScreenViewModel = RidesScreenViewModel(androidVM)

    RidesScreen(ridesScreenViewModel,{})
}