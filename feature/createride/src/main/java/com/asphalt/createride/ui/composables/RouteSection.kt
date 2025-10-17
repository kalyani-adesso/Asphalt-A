package com.asphalt.createride.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GreenLIGHT
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.REDLIGHT
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.createride.viewmodel.CreateRideScreenViewModel

@Composable
fun RouteSection(viewModel: CreateRideScreenViewModel) {
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
            text = "Starting Point",
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
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = if (!viewModel.rideDetailsState.value.startLocation.isNullOrEmpty()) {
                    viewModel.rideDetailsState.value.startLocation.toString()
                } else {
                    ""
                },
                onValueChange = { viewModel.updateStartLocation(it) },
                placeholder = {
                    Text(
                        text = "Enter starting location",
                        style = Typography.bodyMedium,
                        color = NeutralDarkGrey,

                        )
                },
                textStyle = Typography.bodyMedium.copy(NeutralDarkGrey),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),

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
                        tint = GreenLIGHT

                    )
                }

            )

        }
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Text(
            text = "Destination",
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
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = if (!viewModel.rideDetailsState.value.endLocation.isNullOrEmpty()) {
                    viewModel.rideDetailsState.value.endLocation.toString()
                } else {
                    ""
                },
                onValueChange = { viewModel.updateEnLocation(it) },
                placeholder = {
                    Text(
                        text = "Enter destination",
                        style = Typography.bodyMedium,
                        color = NeutralDarkGrey,

                        )
                },
                textStyle = Typography.bodyMedium.copy(NeutralDarkGrey),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),

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

            )

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