package com.asphalt.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.R.string

@Composable
fun LoginSuccessScreen() {
    AsphaltTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = NeutralWhite),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Spacer(Modifier.height(Dimensions.spacing144))
            Column(
                modifier = Modifier
                    .fillMaxWidth().weight(1.4f)
                    .padding(
                        start = Dimensions.padding30,
                        end = Dimensions.padding30
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_success),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,

                    )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                Text(text = stringResource(string.login_success_ful), style = TypographyBold.headlineSmall)
                Spacer(modifier = Modifier.height(Dimensions.spacing16))
                Text(
                    text = stringResource(string.login_success_msg),
                    textAlign = TextAlign.Center,
                    style = TypographyMedium.bodyMedium,
                    color = NeutralDarkGrey,
                    modifier = Modifier.padding(
                        start = Dimensions.padding30,
                        end = Dimensions.padding30
                    )
                )
                Spacer(modifier = Modifier.height(Dimensions.padding80))
                Box(
                    modifier = Modifier
                        .padding(
                            start = Dimensions.padding30,
                            end = Dimensions.padding30
                        )

                ) {
                    GradientButton(
                        PrimaryDarkerLightB75, PrimaryDarkerLightB50,
                        buttonText = stringResource(string.lets_explore).uppercase(),

                        ) {}
                }
            }


        }


    }
}

@Preview
@Composable
fun LoginSuccesPreview() {
    LoginSuccessScreen()
}