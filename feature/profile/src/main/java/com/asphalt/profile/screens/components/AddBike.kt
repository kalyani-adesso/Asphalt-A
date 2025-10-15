package com.asphalt.profile.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralRed
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.SemiTransparentDarkGray
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.profile.data.VehicleData
import com.asphalt.profile.viewmodels.AddBikesVM
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddBikePopup(
    onDismiss: () -> Unit, addBikesVM: AddBikesVM = koinViewModel(),
    onAddBike: (VehicleData) -> Unit
) {
    addBikesVM.clearAll()
    val make = addBikesVM.make.collectAsStateWithLifecycle()
    val model = addBikesVM.model.collectAsStateWithLifecycle()
    val modelError = addBikesVM.modelError.collectAsStateWithLifecycle()
    val makeError = addBikesVM.makeError.collectAsStateWithLifecycle()

    Popup(properties = PopupProperties(focusable = true)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SemiTransparentDarkGray) // translucent overlay
                .clickable(onClick = { onDismiss() }), // click outside to dismiss
            contentAlignment = Alignment.TopStart
        ) {
            ComposeUtils.CommonContentBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = Dimensions.padding50,
                        start = Constants.DEFAULT_SCREEN_HORIZONTAL_PADDING,
                        end = Constants.DEFAULT_SCREEN_HORIZONTAL_PADDING
                    )
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = Constants.DEFAULT_SCREEN_HORIZONTAL_PADDING,
                        vertical = Dimensions.size25
                    )
                ) {
                    ComposeUtils.RoundedIconWithHeaderComponent(
                        PrimaryDarkerLightB75,
                        R.drawable.ic_bike,
                        stringResource(R.string.selecting_your_ride),
                        stringResource(R.string.choose_motorcycle_type)
                    )
                    Spacer(Modifier.height(Dimensions.size25))
                    Text(
                        stringResource(R.string.make),
                        style = TypographyMedium.bodyMedium,
                        fontSize = Dimensions.textSize16
                    )
                    Spacer(Modifier.height(Dimensions.padding10))

                    ComposeUtils.CustomTextField(make.value, {
                        addBikesVM.updateMake(it)
                    }, stringResource(R.string.type_make))
                    if (makeError.value)
                        Text(
                            text = stringResource(R.string.make_cannot_be_empty),
                            Modifier.padding(top = Dimensions.size4),
                            style = Typography.bodySmall,
                            color = NeutralRed
                        )

                    Spacer(Modifier.height(Dimensions.padding20))
                    Text(
                        stringResource(R.string.model),
                        style = TypographyMedium.bodyMedium,
                        fontSize = Dimensions.textSize16
                    )
                    Spacer(Modifier.height(Dimensions.padding10))

                    ComposeUtils.CustomTextField(
                        value = model.value,
                        onValueChanged = { addBikesVM.updateModel(it) },
                        placeHolderText = stringResource(R.string.type_model)
                    )
                    if (modelError.value)
                        Text(
                            text = stringResource(R.string.model_cannot_be_empty),
                            Modifier.padding(top = Dimensions.size4),
                            style = Typography.bodySmall,
                            color = NeutralRed
                        )
                    Spacer(Modifier.height(Dimensions.padding20))
                    GradientButton(onClick = {
                        addBikesVM.addBike()?.let {
                            onAddBike(it)
                            onDismiss.invoke()
                        }

                    }) {
                        ComposeUtils.DefaultButtonContent(stringResource(R.string.add_vehicle).uppercase())
                    }
                }
            }
        }
    }
}