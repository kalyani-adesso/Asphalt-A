package com.asphalt.login

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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralPaperGrey
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.TypographyBlack
import com.asphalt.commonui.theme.TypographyBold

@Composable
fun LoginSuccessScreen() {
    AsphaltTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = NeutralPaperGrey),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Spacer(Modifier.height(Dimensions.padding100))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = Dimensions.padding30,
                        end = Dimensions.padding30
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(com.asphalt.commonui.R.drawable.ic_success),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,

                    )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Yey! Login Successfull", style = TypographyBold.headlineSmall)
                Spacer(modifier = Modifier.height(Dimensions.spacing16))
                Text(
                    text = "You will be moved to home screen right now.Enjoy the features!",
                    textAlign = TextAlign.Center,
                    style = TypographyBlack.bodyMedium,
                    color = NeutralDarkGrey,
                    modifier = Modifier.padding(
                        start = Dimensions.padding30,
                        end = Dimensions.padding30
                    )
                )
            }
            Box(
                modifier = Modifier
                    .padding(
                        start = Dimensions.padding30,
                        end = Dimensions.padding30
                    )
                    .weight(1f)
            ) {
                GradientButton(
                    PrimaryDarkerLightB75, PrimaryDarkerLightB50,
                    buttonText = "Lets Explore".uppercase(),

                    ) {}
            }

        }


    }
}

@Preview
@Composable
fun LoginSuccesPreview() {
    LoginSuccessScreen()
}