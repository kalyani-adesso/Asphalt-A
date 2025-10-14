package com.asphalt.profile.screens.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.android.model.CurrentUser
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralLightGrey
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryBrighterLightW50
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.profile.data.VehicleData
import com.asphalt.profile.viewmodels.YourVehiclesVM
import org.koin.androidx.compose.koinViewModel

@Composable
fun YourVehiclesSection(user: CurrentUser?, yourVehiclesVM: YourVehiclesVM = koinViewModel()) {
    val yourVehiclesList = yourVehiclesVM.vehiclesList.collectAsStateWithLifecycle()

    ComposeUtils.CommonContentBox {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.padding16, vertical = Dimensions.padding21)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    Dimensions.spacing10
                )
            ) {

                ComposeUtils.ColorIconRounded(
                    PrimaryBrighterLightW50,
                    resId = R.drawable.ic_two_wheeler
                )
                Column {
                    ComposeUtils.SectionTitle(
                        "Your Vehicles"
                    )
                    ComposeUtils.SectionSubtitle(
                        "Motorcycles you own or ride",
                    )
                }
            }
            if (yourVehiclesList.value.isEmpty())
                NoVehiclesView {
                    yourVehiclesVM.addVehicle(VehicleData("New", "Bike"))
                }
            else {
                ComposeUtils.SectionTitle("Your Garage" + "(" + yourVehiclesList.value.size + ")")
            }


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
            ComposeUtils.SectionTitle("No vehicles added yet")
            ComposeUtils.SectionSubtitle(
                "Add your motorcycle to get started!",
                modifier = Modifier.offset(y = Dimensions.spacingNeg8)
            )

        }

    }
}

@Composable
fun AddBikeButton(onAddBikeClick: () -> Unit) {
    GradientButton(buttonRadius = Dimensions.size10, onClick = {
        onAddBikeClick.invoke()
    }, buttonHeight = Dimensions.size50) {
        Row(

            horizontalArrangement = Arrangement.spacedBy(
                Dimensions.size3
            ),
        ) {
            Image(painter = painterResource(R.drawable.ic_add), null)
            Text(
                "Add Bike".uppercase(),
                style = TypographyBold.bodyMedium,
                fontSize = Dimensions.textSize16,
                color = NeutralWhite
            )
        }
    }

}