package com.asphalt.joinaride

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asphalt.android.model.joinride.JoinRideModel
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.SearchView
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.Typography
import com.asphalt.android.viewmodel.joinridevm.JoinRideViewModel
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.GreenDark
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralWhite25
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.SafetyOrange
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.VividRed
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.commonui.utils.ComposeUtils.getDpForScreenRatio
import com.asphalt.commonui.utils.ComposeUtils.getScreenWidth
import com.asphalt.commonui.utils.Utils
import com.asphalt.dashboard.constants.DashboardInvitesConstants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun JoinRideScreen(
    viewModel: JoinRideViewModel = koinViewModel(),
    setTopAppBarState: (AppBarState) -> Unit,
    navigateToConnectedRide:() -> Unit
)
{
    var isLoading by remember { mutableStateOf(false) }
    var toolbarTitle by remember { mutableStateOf("") }

    toolbarTitle = stringResource(R.string.join_ride)

    setTopAppBarState(
        AppBarState(title = toolbarTitle)
    )
        Column(modifier = Modifier
            .fillMaxSize()
            .background(
                color = NeutralLightPaper,
                shape = RoundedCornerShape(Dimensions.size10)
            )) {
            JoinRide(viewModel, navigateToConnectedRide = {navigateToConnectedRide.invoke()})

        }
}

@Composable
fun JoinRide(viewModel: JoinRideViewModel,
             navigateToConnectedRide:() -> Unit) {

    val query by viewModel.searchQuery.collectAsState()
    val riders by viewModel.filteredList.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()) {


        SearchView(
            query = query,
            onQueryChange = { viewModel.onSearchQueryChanged(it)},
            onClearClick = { viewModel.onSearchQueryChanged("")},
            placeholder = "Search rides by location.."
        )
        Spacer(modifier = Modifier.height(Dimensions.padding20))

        if (riders.isEmpty()) {
            Text("No Riders Found", style = MaterialTheme.typography.bodyLarge)
        }
        else {
            LazyColumn {
                items(riders) { rider ->
                    RiderCard(rider, navigateToConnectedRide = {navigateToConnectedRide.invoke()})

                }
            }
        }
    }
}

@Composable
fun RiderCard(rider: JoinRideModel,
              navigateToConnectedRide:() -> Unit) {

    ComposeUtils.CommonContentBox(
        isBordered = true,
        radius = Constants.DEFAULT_CORNER_RADIUS,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = Dimensions.padding16, end = Dimensions.padding16, bottom = Dimensions.padding16),

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
                            text = (rider.rideType),
                            style = TypographyBold.titleMedium,
                            fontSize = Dimensions.textSize16,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(end = Dimensions.size10)
                        )
                        Spacer(Modifier.height(Dimensions.size3))
                        Text(
                            text = (rider.byWhom),
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
            Spacer(Modifier.height(Dimensions.padding10))
            Column {
                Text(
                    text = ("Join us for a beautiful sunrise ride along with coastal highway"),
                    style = Typography.titleSmall,
                    fontSize = Dimensions.textSize12,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = Dimensions.size10)
                )
                Spacer(Modifier.height(Dimensions.padding10))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                    , modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.ic_location_purple
                            ),
                            contentDescription = "Email Icon",
                            tint = PrimaryDarkerLightB75,
                        )
                        Spacer(Modifier.width(Dimensions.size5))

                        Text(
                            text =(rider.destination),
                            style = Typography.titleMedium,
                            fontSize = Dimensions.textSize12
                        )

                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.ic_rides
                            ),
                            contentDescription = "Ride KM",
                            tint = SafetyOrange,
                        )
                        Spacer(Modifier.width(Dimensions.size5))

                        Text(
                            text =(rider.distance),
                            style = Typography.titleMedium,
                            fontSize = Dimensions.textSize12
                        )

                    }
                }

                Spacer(Modifier.height(Dimensions.padding10))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                    , modifier = Modifier.fillMaxWidth()
                ){
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.ic_calendar_blue
                            ),
                            contentDescription = "Email Icon",
                            tint = GreenDark,
                        )
                        Spacer(Modifier.width(Dimensions.size5))
                        Text(
                            text = Utils.formatDateTime(
                                rider.dateTime,
                                "dd/MM/yyyy HH:mm",
                                "EEE, dd MMM yyyy - hh:mm a"
                            ),
                            style = Typography.titleMedium,
                            fontSize = Dimensions.textSize12
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.ic_person
                            ),
                            contentDescription = "Riders icon",
                            tint = GreenDark,
                        )
                        Spacer(Modifier.width(Dimensions.size5))
                        Text(
                            text = Utils.formatDateTime(
                                rider.riders,
                                "dd/MM/yyyy HH:mm",
                                "EEE, dd MMM yyyy - hh:mm a"
                            ),
                            style = Typography.titleMedium,
                            fontSize = Dimensions.textSize12
                        )
                    }
                }

                Spacer(Modifier.height(height = Dimensions.size20))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        Dimensions.size8, Alignment.CenterHorizontally
                    ), modifier = Modifier.fillMaxWidth()
                ) {

                    Button(
                        onClick = {
                            //TODO:call ride

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeutralDarkGrey),
                        modifier = Modifier
                            .weight(1f)
                            .height(Dimensions.size50),
                        shape = RoundedCornerShape(Constants.DEFAULT_CORNER_RADIUS)
                    ) {

                        Icon(imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            tint = PrimaryDarkerLightB75)

                        Text(
                            stringResource(R.string.call_ride).uppercase(),
                            color = NeutralBlack,
                            style = TypographyBold.titleMedium,
                            fontSize = Dimensions.textSize14,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }

                    GradientButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            navigateToConnectedRide.invoke()

                        },
                        buttonHeight = Dimensions.size50,
                        contentPadding = PaddingValues(Dimensions.size0)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.moved_location,
                                ),
                                contentDescription = "Riders icon",
                                tint = NeutralWhite,
                            )
                            Text(
                                stringResource(R.string.join_ride).uppercase(),
                                color = NeutralWhite,
                                style = TypographyBold.titleMedium,
                                fontSize = Dimensions.textSize14,
                                modifier = Modifier.padding(start = 8.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun JoinRideScrenPreview() {

    JoinRideScreen(setTopAppBarState = {},
        viewModel = viewModel(),
        navigateToConnectedRide = {})
}
