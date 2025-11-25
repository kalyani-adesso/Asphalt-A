package com.asphalt.joinaride

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.android.constants.APIConstants.RIDE_JOINED
import com.asphalt.android.model.rides.RidesData
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.SearchView
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.Typography
import com.asphalt.joinaride.viewmodel.JoinRideViewModel
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.GreenDark
import com.asphalt.commonui.theme.GreenLIGHT
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.SafetyOrange
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.commonui.utils.Utils
import org.koin.compose.viewmodel.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
@Composable
fun JoinRideMainListScreen(
    viewModel: JoinRideViewModel = koinViewModel(),
    setTopAppBarState: (AppBarState) -> Unit,
    navigateToConnectedRide:() -> Unit,
    navigateToEndRide : () -> Unit)
{
    var toolbarTitle by remember { mutableStateOf("") }
    toolbarTitle = stringResource(R.string.join_ride)

    setTopAppBarState(
        AppBarState(title = toolbarTitle))
        Column(modifier = Modifier
            .fillMaxSize()
            .background(
                color = NeutralWhite,
            )) {
            JoinRide(viewModel,
                navigateToConnectedRide = {navigateToConnectedRide.invoke()},
                navigateToEndRide = { navigateToEndRide.invoke()})
        }
}
@Composable
fun JoinRide(
    viewModel: JoinRideViewModel,
    navigateToConnectedRide: () -> Unit,
    navigateToEndRide: () -> Unit,
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val rides by viewModel.acceptedRides.collectAsState()

    val sortedList = rides.sortedByDescending{ it .createdDate}

    Column {
        SearchView(
            query = searchQuery,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = Dimensions.padding20,
                    start = Dimensions.padding16,
                    end = Dimensions.padding16
                )
                .background(
                    color = NeutralLightPaper,
                    shape = RoundedCornerShape(size = Dimensions.size10)
                ),
            onQueryChange = {
                viewModel.setSearchQuery(it) },
            onClearClick = {
                viewModel.setSearchQuery("") // Clear search filter
            },
            placeholder = "Search rides by location.."
        )
        Spacer(modifier = Modifier.height(Dimensions.padding20))

        if (rides.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No Riders Found", style = MaterialTheme.typography.bodyLarge)
            }
        }
        else {
            LazyColumn {
                items(items = sortedList) { rider ->
                    RiderCard( navigateToConnectedRide = {navigateToConnectedRide.invoke()},
                        navigateToEndRide = { navigateToEndRide.invoke() },
                        ridersList = rider, viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun RiderCard(
    navigateToConnectedRide: () -> Unit,
    navigateToEndRide: () -> Unit,
    ridersList : RidesData,
    viewModel: JoinRideViewModel
) {
    viewModel.setCreatedBy(ride = ridersList)
    val createdBy by viewModel.createdBy.collectAsState()

    ComposeUtils.CommonContentBox(
        isBordered = true,
        radius = Constants.DEFAULT_CORNER_RADIUS,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                start = Dimensions.padding16,
                end = Dimensions.padding16,
                bottom = Dimensions.padding16
            ),
        ) {
        Column(
            modifier = Modifier
                .padding(vertical = Dimensions.spacing19, horizontal = Dimensions.spacing16)
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(weight = 1f)
                ) {
                    CircularNetworkImage(
                        modifier = Modifier.border(
                            width = Dimensions.size2pt5,
                            color = PrimaryDarkerLightB75,
                            shape = CircleShape
                        ),
                        size = Dimensions.size32,
                        imageUrl = "dashboardRideInvite.inviterProfilePicUrl",
                        placeholderPainter = painterResource(id=R.drawable.profile_placeholder)
                    )
                    Spacer(Modifier.width(width = Dimensions.size10))
                    Column {
                        Text(
                            text = (ridersList.rideTitle ?: ""),
                            style = TypographyBold.titleMedium,
                            fontSize = Dimensions.textSize16,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(end = Dimensions.size10)
                        )
                        Spacer(Modifier.height(height = Dimensions.size3))
                        Text(
                            text = ("By" + " " + createdBy),
                            style = Typography.titleMedium,
                            color = NeutralDarkGrey,
                            fontSize = Dimensions.textSize12,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(end = Dimensions.size10)
                        )
                    }
                    Spacer(Modifier.height(height = Dimensions.size17))
                }
            }
            Spacer(Modifier.height(height = Dimensions.padding10))
            Column {
                Text(
                    text = ridersList.description ?: "",
                    style = Typography.titleSmall,
                    fontSize = Dimensions.textSize12,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = Dimensions.size10)
                )
                Spacer(Modifier.height(height = Dimensions.padding10))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                    , modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.ic_location_purple),
                            contentDescription = "location Icon",
                            tint = PrimaryDarkerLightB75)

                        Spacer(Modifier.width(width = Dimensions.size5))

                        val startLocation = ridersList.startLocation
                        val endLocation = ridersList.endLocation
                        Text(
                            text = startLocation + "-" + endLocation ?: "",
                            style = Typography.titleMedium,
                            fontSize = Dimensions.textSize12,
                            maxLines = 2
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_rides),
                            contentDescription = "Ride KM",
                            tint = SafetyOrange)
                        Spacer(Modifier.width(Dimensions.size5))
                        val distance = ridersList.rideDistance
                        val smallDistance = String.format("%.2f", distance)
                        Text(
                            text = smallDistance ?: "",
                            style = Typography.titleMedium,
                            fontSize = Dimensions.textSize12,
                            maxLines = 2
                        )
                    }
                }
                Spacer(Modifier.height(Dimensions.padding10))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.ic_calendar_blue
                            ),
                            contentDescription = "date Icon",
                            tint = GreenDark,
                        )
                        Spacer(Modifier.width(Dimensions.size5))

                        val timeStampString = ridersList.startDate
                        val date = Date(timeStampString ?: 0L)
                        val formatted = SimpleDateFormat("EEE, MMM dd - hh:mm a", Locale.getDefault()).format(date)

                        Text(
                            text = Utils.formatDateTime(
                                input =formatted,
                                inputFormat = "dd/MM/yyyy HH:mm",
                                outputFormat = "EEE, dd MMM yyyy - hh:mm a"
                            ),
                            style = Typography.titleMedium,
                            fontSize = Dimensions.textSize12
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.ic_group_white
                            ),
                            contentDescription = "Riders icon",
                            tint = GreenDark,
                        )
                        Spacer(Modifier.width(Dimensions.size5))
                        Text(
                            text =  "0/8 Riders",
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
                    // call rider button
                    ElevatedButton(
                        onClick = {
                            navigateToEndRide.invoke()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeutralWhite),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = Dimensions.padding10)
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
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    // join/ rejoin ride button
                    if (ridersList.rideStatus == RIDE_JOINED) {
                        ElevatedButton (
                            modifier = Modifier.weight(weight = 1f)
                                .height(height = Dimensions.size50),
                            shape = RoundedCornerShape(Constants.DEFAULT_CORNER_RADIUS),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenLIGHT),
                            onClick = {
//
//                                viewModel.updateRideStatus(userId = ridersList.createdBy ?: "", rideId = ridersList.ridesID ?: "",
//                                    status = RIDE_JOINED)
                                navigateToConnectedRide.invoke()
                            },
                            contentPadding = PaddingValues(all = Dimensions.size0)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = Dimensions.padding10),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = R.drawable.moved_location),
                                    contentDescription = "Riders icon",
                                    tint = NeutralWhite)
                                Text(
                                    stringResource(R.string.rejoinRide).uppercase(),
                                    color = NeutralWhite,
                                    style = TypographyBold.titleMedium,
                                    fontSize = Dimensions.textSize14,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    } else  {
                        // join ride button
                        GradientButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                ridersList.ridesID
                                // if user trying to join another ride previous will end then new ride join logic pending
                                viewModel.updateRideStatus(userId = ridersList.createdBy ?: "", rideId = ridersList.ridesID ?: "",
                                    status = RIDE_JOINED) // status 3
                                navigateToConnectedRide.invoke()
                            },
                            buttonHeight = Dimensions.size50,
                            contentPadding = PaddingValues(Dimensions.size0)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = Dimensions.padding10),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = R.drawable.moved_location),
                                    contentDescription = "Riders icon",
                                    tint = NeutralWhite)
                                Text(
                                    stringResource(R.string.join_ride).uppercase(),
                                    color = NeutralWhite,
                                    style = TypographyBold.titleMedium,
                                    fontSize = Dimensions.textSize14,
                                    modifier = Modifier.padding(start = 8.dp)
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
@Preview
fun JoinRideScrenPreview() {

}