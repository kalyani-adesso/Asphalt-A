package com.asphalt.profile.screens.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralLightGrey
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryBrighterLightW50
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.VividOrange
import com.asphalt.commonui.theme.VividRed
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.profile.data.VehicleData
import com.asphalt.profile.screens.components.AddBikePopup
import com.asphalt.profile.sealedclasses.BikeType
import com.asphalt.profile.viewmodels.YourVehiclesVM
import org.koin.androidx.compose.koinViewModel

@Composable
fun YourVehiclesSection(yourVehiclesVM: YourVehiclesVM = koinViewModel()) {
    val yourVehiclesList = yourVehiclesVM.vehiclesList.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        yourVehiclesVM.getVehicles()
    }
    var showPopup by remember { mutableStateOf(false) }
    ComposeUtils.CommonContentBox {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.padding16, vertical = Dimensions.padding21)
        ) {
            if (showPopup)
                AddBikePopup({
                    showPopup = false
                }, onAddBike = { type, make, model ->
                    yourVehiclesVM.addBike(type, model, make)
                })
            ComposeUtils.RoundedIconWithHeaderComponent(
                PrimaryBrighterLightW50,
                R.drawable.ic_two_wheeler,
                stringResource(R.string.your_vehicles),
                stringResource(R.string.motorcycles_you_own_or_ride)
            )
            if (yourVehiclesList.value.isEmpty())
                NoVehiclesView {
                    showPopup = true
                }
            else {
                YourGarage(yourVehiclesList.value, {
                    yourVehiclesVM.deleteBike(it.id)
                }, {
                    showPopup = true

                })
            }
        }
    }


}

@Composable
fun GarageItem(vehicleData: VehicleData, onDeleteBike: () -> Unit) {
    RoundedBox(modifier = Modifier.fillMaxWidth(), cornerRadius = Dimensions.size10) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = Dimensions.padding15, vertical = Dimensions.size22)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            ComposeUtils.RoundedIconWithHeaderComponent(
                VividOrange,
                R.drawable.ic_two_wheeler,
                stringResource(BikeType.getBikeTypeById(vehicleData.type)!!),
                stringResource(
                    R.string.garage_vehicle,
                    vehicleData.make,
                    vehicleData.model
                )
            )

            Box(
                modifier = Modifier
                    .clickable {
                        onDeleteBike.invoke()

                    }
                    .fillMaxHeight()
            ) {
                ComposeUtils.ColorIconRounded(backColor = VividRed, resId = R.drawable.ic_delete)
            }

        }
    }

}

@Composable
fun YourGarage(
    yourVehiclesList: List<VehicleData>,
    onDeleteBike: (VehicleData) -> Unit,
    onAddBikeClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimensions.padding21),
        verticalArrangement = Arrangement.spacedBy(Dimensions.padding20)
    ) {
        ComposeUtils.SectionTitle(
            stringResource(
                R.string.your_garage,
                yourVehiclesList.size
            )
        )
        Column(modifier = Modifier.wrapContentHeight()) {
            yourVehiclesList.forEach {
                GarageItem(it, onDeleteBike = { onDeleteBike(it) })
                Spacer(Modifier.height(Dimensions.spacing15))
            }

        }
        AddBikeButton(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            onAddBikeClick.invoke()
        }


    }
}

@Composable
fun NoVehiclesView(onAddBikeClick: () -> Unit) {
    RoundedBox(
        cornerRadius = Dimensions.size10,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimensions.padding21),
        borderColor = NeutralLightGrey,
        borderStroke = Dimensions.spacing1
    ) {
        Column(
            modifier = Modifier
                .padding(top = Dimensions.size25, bottom = Dimensions.padding16)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(
                Dimensions.spacing15,
                Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(R.drawable.ic_two_wheeler_blue), null)
            AddBikeButton { onAddBikeClick() }
            ComposeUtils.SectionTitle(stringResource(R.string.no_vehicles_added_yet))
            ComposeUtils.SectionSubtitle(
                stringResource(R.string.add_your_motorcycle),
                modifier = Modifier.offset(y = Dimensions.spacingNeg8)
            )

        }

    }
}

@Composable
fun AddBikeButton(modifier: Modifier = Modifier, onAddBikeClick: () -> Unit) {
    GradientButton(modifier = modifier, buttonRadius = Dimensions.size10, onClick = {
        onAddBikeClick.invoke()
    }, buttonHeight = Dimensions.size50) {
        Row(

            horizontalArrangement = Arrangement.spacedBy(
                Dimensions.size3
            ),
        ) {
            Image(painter = painterResource(R.drawable.ic_add), null)
            Text(
                stringResource(R.string.add_bike).uppercase(),
                style = TypographyBold.bodyMedium,
                fontSize = Dimensions.textSize16,
                color = NeutralWhite
            )
        }
    }

}