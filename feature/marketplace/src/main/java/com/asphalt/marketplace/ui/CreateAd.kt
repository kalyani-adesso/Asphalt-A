package com.asphalt.marketplace.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightGrey
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.marketplace.ui.composable.Buttons
import com.asphalt.marketplace.ui.composable.InputAndDropDown
import com.asphalt.marketplace.ui.composable.TextFieldWithTitle
import com.asphalt.marketplace.ui.composable.TwoDropDownWithTitle
import com.asphalt.marketplace.viewmodel.CreateAdViewModel

@Composable
fun CreateAd(setTopAppBarState: (AppBarState) -> Unit) {
    val scrollState = rememberScrollState()
    val viewModel: CreateAdViewModel = viewModel()
    setTopAppBarState(
        AppBarState(
            title = "Add Post"
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
                    .padding(bottom = Dimensions.padding100)
                    .imePadding()
            ) {
                CreateAdForm(viewModel)
                Box(
                    modifier = Modifier
                        .height(Dimensions.size175)
                        .fillMaxWidth()
                        .padding(
                            start = Dimensions.padding16,
                            end = Dimensions.padding16
                        )
                        .border(
                            width = Dimensions.spacing1,
                            color = NeutralLightGrey,
                            shape = RoundedCornerShape(Dimensions.size10)
                        ), contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        GradientButton(
                            modifier = Modifier,
                            buttonRadius = Dimensions.size10,
                            onClick = {

                            },
                            buttonHeight = Dimensions.size50
                        ) {
                            Row(

                                horizontalArrangement = Arrangement.spacedBy(
                                    Dimensions.size3
                                ),
                            ) {
                                Image(painter = painterResource(R.drawable.ic_add), null)
                                Text(
                                    stringResource(R.string.add_photos).uppercase(),
                                    style = TypographyBold.bodyMedium,
                                    fontSize = Dimensions.textSize16,
                                    color = NeutralWhite
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(Dimensions.spacing15))
                        Text(
                            text = "No items added yet", style = TypographyBold.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(Dimensions.spacing15))

                        Text(
                            text = "Tap to upload product images", style = Typography.bodySmall,
                            textAlign = TextAlign.Center, color = NeutralDarkGrey
                        )
                    }


                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(color = NeutralWhite)
                    .padding(horizontal = Dimensions.padding16, vertical = Dimensions.padding16)
                    .imePadding()
            ) {
                Buttons("Cancel", "Publish") {
                    viewModel.validations()
                }
            }

            //Button(onClick = {}, modifier = Modifier.align(Alignment.BottomCenter)) { }

        }
    }


}

@Composable
fun CreateAdForm(viewModel: CreateAdViewModel) {
    val adModel by viewModel.createAd_model.collectAsState()
    var title: String = ""
    Spacer(modifier = Modifier.height(Dimensions.size16))
    TextFieldWithTitle(
        "Title*", 1, showError = adModel.isShowTitleError,
        value = adModel.tile
    ) { tit ->
        viewModel.setTitle(tit)
    }
    Spacer(modifier = Modifier.height(Dimensions.size16))
    TwoDropDownWithTitle("Category*", "Condition*")
    Spacer(modifier = Modifier.height(Dimensions.size16))
    TwoDropDownWithTitle("Year*", "Kilometer*")
    Spacer(modifier = Modifier.height(Dimensions.size16))
    TwoDropDownWithTitle("Engine*", "Type*")
    Spacer(modifier = Modifier.height(Dimensions.size16))
    InputAndDropDown(
        "Color*", "Owner*", adModel.isShowColorError,
        input1 = { input ->
            viewModel.setColor(input)
        }, input2 = {

        }, inputString1 = adModel.color
    )
    Spacer(modifier = Modifier.height(Dimensions.size16))
    InputAndDropDown(
        "Fuel Type*",
        "Insurance*",
        adModel.isShowFuelError,
        input1 = { input ->
            viewModel.setFuelType(input)
        },
        input2 = { input ->
        }, inputString1 = adModel.fuel
    )
    Spacer(modifier = Modifier.height(Dimensions.size16))
    TextFieldWithTitle(
        title = "Price*",
        maxLines = 1,
        adModel.isShowPriceError,
        if (adModel.price == null) "" else adModel.price.toString(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    ) { input ->
        viewModel.setPrice(input)
    }
    Spacer(modifier = Modifier.height(Dimensions.size16))
    TextFieldWithTitle(
        "Location", maxLines = 1, adModel.isLocationError,
        adModel.location
    ) {

    }
    Spacer(modifier = Modifier.height(Dimensions.size16))
    TextFieldWithTitle(
        "Description", maxLines = 3, adModel.isShowDescError,
        adModel.desc
    ) {

    }
    Spacer(modifier = Modifier.height(Dimensions.size16))
}


@Preview
@Composable
fun CreatAdPreview() {
    CreateAd({})
}