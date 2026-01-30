package com.asphalt.marketplace.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.BorderedButton
import com.asphalt.commonui.ui.GradientButton
import kotlinx.coroutines.launch

@Composable
fun Buttons(label1: String, label2: String) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(
            Dimensions.spacing20
        )
    ) {
        BorderedButton(
            modifier = Modifier.weight(1f), onClick = {
            }, contentPaddingValues = PaddingValues(
                Dimensions.size0
            ), buttonHeight = Dimensions.size50
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    label1,//stringResource(R.string.previous).uppercase(),
                    color = PrimaryDarkerLightB75,
                    style = TypographyBold.bodyMedium
                )
            }
        }

        GradientButton(
            modifier = Modifier.weight(1f), endColor = PrimaryDarkerLightB75,
            onClick = {


            }, contentPadding = PaddingValues(
                Dimensions.size0
            ), buttonHeight = Dimensions.size50
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    label2,
                    color = NeutralWhite,
                    style = TypographyBold.bodyMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewButtons(){
    Buttons("CANCEL","PUBLISH")
}