package com.asphalt.login


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.PrimaryLight
import com.asphalt.commonui.theme.TypographyBold


@Composable
fun GradientButton(
    startColor: Color,
    endColor: Color,
    buttonHeight: Dp = Dimensions.size60,
    buttonRadius: Dp = Dimensions.radius15,
    buttonText: String,
    showArrow: Boolean = false,
    isEnable :Boolean =true,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
    ) {
        val gradient = Brush.horizontalGradient(
            listOf(
                startColor,
                endColor
            )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(buttonHeight)
                .then(
                    if(isEnable){
                        Modifier.background(gradient, RoundedCornerShape(buttonRadius))
                    }else{
                        Modifier.background(gradient, RoundedCornerShape(buttonRadius))
                    }

                )

        ) {
            Button(
                onClick = { onClick },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxSize(),
                shape = RoundedCornerShape(buttonRadius)

            ) {
                Row(
                    horizontalArrangement =
                        if (showArrow)
                            Arrangement.SpaceBetween
                        else
                            Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(buttonText, color = NeutralWhite, style = TypographyBold.bodyLarge)
                }
            }

        }
    }
}

@Preview
@Composable
fun ButtonPreview() {
    //LoginScreen()
    GradientButton(
        PrimaryDarkerLightB75, PrimaryDarkerLightB50,
        buttonText = "Signin"
    ) {

    }
}