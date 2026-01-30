package com.asphalt.marketplace.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium

@Composable
fun InputAndDropDown(title1:String,title2:String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = Dimensions.padding16, end = Dimensions.padding16),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.size10)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title1,//stringResource(R.string.start_date),
                style = TypographyMedium.bodyMedium,
                color = NeutralBlack,
                //modifier = Modifier.padding(start = Dimensions.padding16)
            )
            Spacer(modifier = Modifier.height(Dimensions.size8))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.padding50)
                    .background(
                        NeutralLightPaper, shape = RoundedCornerShape(Dimensions.padding10)
                    )
                    .border(
                        width = Dimensions.padding1,
                        color = NeutralWhite,
                        shape = RoundedCornerShape(Dimensions.padding10)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = "",
                    onValueChange = {

                    },
                    placeholder = {
                        Text(
                            text = "eg: Yamaha - R15 2020 ",//stringResource(R.string.enter_ride_name),
                            style = Typography.bodyMedium,
                            color = NeutralDarkGrey,

                            )
                    },
                    textStyle = Typography.bodyMedium,
                    singleLine = true,
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

                    )

            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title2,//stringResource(R.string.start_date),
                style = TypographyMedium.bodyMedium,
                color = NeutralBlack,
                //modifier = Modifier.padding(start = Dimensions.padding16)
            )
            Spacer(modifier = Modifier.height(Dimensions.size8))
            Row(
                modifier = Modifier
                    .height(Dimensions.padding50)
                    .fillMaxWidth()
                    .background(
                        NeutralLightPaper, shape = RoundedCornerShape(Dimensions.padding10),
                    )
                    .border(
                        width = Dimensions.padding1,
                        color = NeutralWhite,
                        shape = RoundedCornerShape(Dimensions.padding10)
                    )
                    .clickable { },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Used",
                    style = Typography.bodyMedium,
                    color = NeutralDarkGrey,
                    modifier = Modifier
                        .padding(start = Dimensions.padding16)
                )
                Image(
                    painter = painterResource(R.drawable.ic_dropdown_arrow),
                    contentDescription = "",
                    modifier = Modifier.padding(end = Dimensions.padding16)
                )
            }
        }

    }
}

@Preview
@Composable
fun InputAndDropDownPreview(){
    InputAndDropDown("title1","title2")
}