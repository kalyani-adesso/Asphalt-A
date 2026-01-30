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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

@Composable
fun CreateAd(setTopAppBarState: (AppBarState) -> Unit) {
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
            Column(modifier = Modifier.fillMaxSize()) {
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
            Button(onClick = {}, modifier = Modifier.align(Alignment.BottomCenter)) { }

        }
    }


}

@Preview
@Composable
fun CreatAdPreview() {
    CreateAd({})
}