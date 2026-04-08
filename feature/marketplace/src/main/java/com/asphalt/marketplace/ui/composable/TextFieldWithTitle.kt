package com.asphalt.marketplace.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.theme.VividRed

@Composable
fun TextFieldWithTitle(
    title: String,
    maxLines: Int = 1,
    showError: Boolean = false,
    value: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit,

    ) {
    Column() {
        Text(
            text = title, //stringResource(R.string.ride_title),
            style = TypographyMedium.bodyMedium,
            color = NeutralBlack,
            modifier = Modifier.padding(start = Dimensions.padding16)
        )
        Spacer(modifier = Modifier.height(Dimensions.size8))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (maxLines > 1) {
                        Modifier.heightIn(min = Dimensions.padding80)
                    } else {
                        Modifier.height(Dimensions.padding50)
                    }
                )
                .padding(start = Dimensions.padding16, end = Dimensions.padding16)
                .background(
                    NeutralLightPaper, shape = RoundedCornerShape(Dimensions.padding10)
                )
                .then(
                    if (showError) {
                        Modifier.border(
                            width = Dimensions.padding1,
                            color = VividRed,
                            shape = RoundedCornerShape(Dimensions.padding10)
                        )
                    } else {
                        Modifier.border(
                            width = Dimensions.padding1,
                            color = NeutralWhite,
                            shape = RoundedCornerShape(Dimensions.padding10)
                        )
                    }
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = value,
                onValueChange = {
                    onValueChange.invoke(it)
                },
                placeholder = {
                    Text(
                        text = "eg: Yamaha - R15 2020 ",//stringResource(R.string.enter_ride_name),
                        style = Typography.bodyMedium,
                        color = NeutralDarkGrey,

                        )
                },
                textStyle = Typography.bodyMedium,
                maxLines = maxLines,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .testTag("rideTitleInput"),

                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,

                    ),
                keyboardOptions = keyboardOptions
            )


        }
    }
}

@Preview
@Composable
fun TextFieldWithTitlePreview() {

    TextFieldWithTitle("title", 1, false, value = "", onValueChange = {})
}