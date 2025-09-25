package com.asphalt.getstarted.composables

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.TypographyBold

@Composable
fun GradientButton(
    startColor: Color,
    endColor: Color,
    onClick: () -> Unit,
    buttonPaddingValues: PaddingValues = PaddingValues(0.dp),
    buttonHeight: Dp = Dimensions.size60,
    buttonRadius: Dp = Dimensions.radius15,
    buttonText: String,
    buttonAlignment: Alignment = Alignment.BottomCenter,
    showArrow: Boolean = false
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(buttonPaddingValues)
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
                .background(gradient, RoundedCornerShape(buttonRadius))
                .align(buttonAlignment)

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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Dimensions.padding)
                ) {
                    Text(
                        buttonText,
                        color = NeutralWhite,
                        fontSize = Dimensions.textSize18,
                        style = TypographyBold.labelLarge,
                    )
                    if (showArrow)
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow),
                            contentDescription = null,
                        )
                }
            }

        }
    }
}