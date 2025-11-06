package com.asphalt.createride.ui.composables

import PlacesBottomSheet
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GreenLIGHT
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralBlackGrey
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.REDLIGHT
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.theme.VividRed
import com.asphalt.createride.viewmodel.CreateRideScreenViewModel

@Composable
fun RouteSection(viewModel: CreateRideScreenViewModel) {
    val startLocation = 1
    val endLocation = 2
    var bottomSheetType by remember { mutableStateOf(0) }

    if (bottomSheetType != 0) {
        PlacesBottomSheet(true, latLon = { lat, lon, locName ->
            if (bottomSheetType == startLocation) {
                viewModel.updateStartLocation(locName)
                viewModel.updateStartLocation(lat, lon)
                viewModel._showRideStartLocError.value = false
            } else if (bottomSheetType == endLocation) {
                viewModel.updateEnLocation(locName)
                viewModel.updateEndLocation(lat, lon)
                viewModel._showRideEndLocError.value = false
            }
            bottomSheetType = 0
        }, onDismiss = {
            bottomSheetType = 0
        })
    }
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
        Text(
            text = stringResource(R.string.starting_point),
            style = TypographyMedium.bodyMedium,
            color = NeutralBlack,
            modifier = Modifier.padding(start = Dimensions.padding16)
        )
        Spacer(modifier = Modifier.height(Dimensions.size8))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimensions.padding50)
                .padding(start = Dimensions.padding16, end = Dimensions.padding16)
                .background(
                    NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10)
                )
                .then(
                    if (viewModel._showRideStartLocError.value) {
                        Modifier.border(
                            width = Dimensions.padding1,
                            color = VividRed,
                            shape = RoundedCornerShape(Dimensions.padding10)
                        )
                    } else {
                        Modifier.border(
                            width = Dimensions.padding1,
                            color = NeutralWhite,
                            shape = RoundedCornerShape(Dimensions.padding10)
                        )
                    }
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    bottomSheetType = startLocation
                },
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Spacer(Modifier.width(Dimensions.padding16))
            Icon(
                painter = painterResource(R.drawable.ic_route),
                contentDescription = "",
                tint = GreenLIGHT,
                modifier = Modifier.padding(end = Dimensions.padding8)
            )

            Text(
                text = viewModel.rideDetailsState.value.startLocation
                    ?: stringResource(R.string.enter_start_loc),
                style = Typography.bodyMedium, maxLines = 1,
                color = if (viewModel.rideDetailsState.value.startLocation.isNullOrEmpty()) NeutralDarkGrey else NeutralBlackGrey
            )


        }
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Text(
            text = stringResource(R.string.destination),
            style = TypographyMedium.bodyMedium,
            color = NeutralBlack,
            modifier = Modifier.padding(start = Dimensions.padding16)
        )
        Spacer(modifier = Modifier.height(Dimensions.size8))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    bottomSheetType = endLocation
                }
                .height(Dimensions.padding50)
                .padding(start = Dimensions.padding16, end = Dimensions.padding16)
                .background(
                    NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10)
                )
                .then(
                    if (viewModel._showRideEndLocError.value) {
                        Modifier.border(
                            width = Dimensions.padding1,
                            color = VividRed,
                            shape = RoundedCornerShape(Dimensions.padding10)
                        )
                    } else {
                        Modifier.border(
                            width = Dimensions.padding1,
                            color = NeutralWhite,
                            shape = RoundedCornerShape(Dimensions.padding10)
                        )
                    }
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(Dimensions.padding16))
            Icon(
                painter = painterResource(R.drawable.ic_route),
                contentDescription = "",
                tint = REDLIGHT,
                modifier = Modifier.padding(end = Dimensions.padding8)
            )

            Text(
                text = viewModel.rideDetailsState.value.endLocation
                    ?: stringResource(R.string.enter_destination),
                style = Typography.bodyMedium, maxLines = 1,
                color = if (viewModel.rideDetailsState.value.endLocation.isNullOrEmpty()) NeutralDarkGrey else NeutralBlackGrey
            )
            /*TextField(
                value = if (!viewModel.rideDetailsState.value.endLocation.isNullOrEmpty()) {
                    viewModel.rideDetailsState.value.endLocation.toString()
                } else {
                    ""
                },
                onValueChange = {
                    *//* viewModel.updateEnLocation(it)*//*
                    viewModel._showRideEndLocError.value = false
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.enter_destination),
                        style = Typography.bodyMedium,
                        color = NeutralDarkGrey,

                        )
                },
                textStyle = Typography.bodyMedium,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), readOnly = true,

                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,

                    ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_route),
                        contentDescription = "",
                        tint = REDLIGHT

                    )
                }

            )*/

        }
//added padding
        Spacer(modifier = Modifier.height(Dimensions.padding16))
    }
}

@Preview
@Composable
fun RooutePreview() {
    var vimodel: CreateRideScreenViewModel = viewModel()
    RouteSection(vimodel)
}