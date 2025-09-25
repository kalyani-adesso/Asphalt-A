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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asphalt.commonui.Theme.Dimensions

@Composable
fun SplashScreen() {
    val windowInfo = LocalWindowInfo.current
    val density = LocalResources.current.displayMetrics.density
    val screenHeightPx = windowInfo.containerSize.height
    val logoPaddingVertical = ((screenHeightPx * 0.25f) / density).dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0XFF006EC7)),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = Dimensions.padding87, vertical = logoPaddingVertical)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(R.drawable.adesso_logo), "")
            Spacer(modifier = Modifier.height(Dimensions.size60))
            Image(painter = painterResource(R.drawable.riders_club_logo), "")
            Spacer(modifier = Modifier.height(Dimensions.size20))
            Text(
                "adesso Rider's\nClub",
                color = Color.White,
                fontSize = 26.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}