package com.asphalt.marketplace.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.marketplace.ui.composable.Buttons

@Composable
fun ProductDetailsScreen(setTopAppBarState: (AppBarState) -> Unit) {
    setTopAppBarState(
        AppBarState(
            title = stringResource(R.string.product_details)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = NeutralWhite)
            .padding(start = Dimensions.padding16, end = Dimensions.padding16)
    ) {

        // Main content (above)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(NeutralLightPaper)

        ) {
            Image(
                painter = painterResource(R.drawable.naked_bike),
                contentDescription = "",
                modifier = Modifier
                    .height(Dimensions.size200)
                    .fillMaxWidth()
                    .padding(vertical = Dimensions.padding16, horizontal = Dimensions.padding16)
                    .clip(RoundedCornerShape(16.dp))
                    .background(NeutralWhite), contentScale = ContentScale.Crop
            )
        }

        // Bottom button (fixed to screen bottom)
        Row(
            modifier = Modifier
                .padding(horizontal = Dimensions.padding16, vertical = Dimensions.padding16)
                .imePadding()
        ) {
            Buttons(
                stringResource(R.string.message),
                stringResource(R.string.make_offer)
            ) {
                //viewModel.validations()
            }
        }
    }
}

@Composable
@Preview
fun ProductDetailsPreview() {
    ProductDetailsScreen({})
}