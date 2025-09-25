package com.asphalt.android

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.asphalt.commonui.R.drawable
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.utils.ComposeUtils.calculateHeightDpForPercentage

@Composable
fun SplashScreen() {

    val logoPaddingVertical = calculateHeightDpForPercentage(0.2f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrimaryDarkerLightB75),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = Dimensions.padding87, vertical = logoPaddingVertical)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(drawable.ic_adesso_logo), "")
            Spacer(modifier = Modifier.height(Dimensions.size60))
            Image(painter = painterResource(drawable.ic_riders_club_logo), "")
            Spacer(modifier = Modifier.height(Dimensions.size20))
            Text(
                stringResource(R.string.adesso_riders_club),
                color = NeutralWhite,
                fontSize = Dimensions.textSize26,
                style = TypographyBold.headlineSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}