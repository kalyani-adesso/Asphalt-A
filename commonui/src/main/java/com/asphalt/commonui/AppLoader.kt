package com.asphalt.commonui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GrayDark
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.PrimaryBrighterLightW75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold

@Composable
fun AppLoader(
    logoRes: Int = R.drawable.ic_app_icon,
    title: String = "Loading",
    description: String = "please wait while we process your request",
    showProgress: Boolean = true
)
{

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            Icon(painter = painterResource(id = logoRes),
                contentDescription = "App Logo",
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.height(Dimensions.size17))

            // title
            Text(text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = NeutralBlack,
                style = TypographyBold.bodyLarge,
            )
            Spacer(modifier = Modifier.height(Dimensions.size10))
            //description
            Text(text = description,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = GrayDark,
                style = Typography.titleMedium
            )
            Spacer(modifier = Modifier.height(Dimensions.size20))

            //Circular progress indicator
            if (showProgress) {
                CircularProgressIndicator()
            }
        }
    }
}