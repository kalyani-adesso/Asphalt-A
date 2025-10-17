package com.asphalt.createride.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GrayLight10
import com.asphalt.commonui.theme.GreenLIGHT
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.createride.viewmodel.CreateRideScreenViewModel

@Composable
fun ParticipantSection(mod: Modifier, viewmodel: CreateRideScreenViewModel) {
    var text by remember { mutableStateOf("") }

    LazyColumn(
        modifier = mod
            .fillMaxWidth()
            .padding(
                start = Dimensions.padding16,
                end = Dimensions.padding16,
            )
            .background(
                color = NeutralLightPaper, shape = RoundedCornerShape(Dimensions.size10)
            ), contentPadding = PaddingValues(bottom = 200.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Spacer(Modifier.height(Dimensions.padding16))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = Dimensions.padding16,
                            end = Dimensions.padding16
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,

                    ) {
                    Text("Invite Contacts", style = TypographyMedium.bodyMedium)
                    Text("0 selected", style = TypographyMedium.bodySmall, color = NeutralDarkGrey)
                }
                Spacer(Modifier.height(Dimensions.padding8))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimensions.padding50)

                        .padding(start = Dimensions.padding16, end = Dimensions.padding16)
                        .background(
                            NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextField(
                        value = if (!text.isEmpty()) {
                            text
                        } else {
                            ""
                        },
                        onValueChange = { text = it },
                        placeholder = {
                            Text(
                                text = "Search by name ,number or bike type...",
                                style = Typography.bodyMedium,
                                color = NeutralDarkGrey,


                                )
                        },
                        textStyle = Typography.bodyMedium.copy(NeutralDarkGrey),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),

                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,

                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,

                            ),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_search_blue),
                                contentDescription = "Email icon",
                                tint = Color.Unspecified

                            )
                        }

                    )

                }
            }
        }
        items(5) { it ->
            Spacer(Modifier.height(Dimensions.padding10))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.size73)
                    .padding(start = Dimensions.padding16, end = Dimensions.padding16),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White // or use NeutralWhite
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = Dimensions.padding16, end = Dimensions.padding16),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(Dimensions.padding40)) {

                                CircularNetworkImage(
                                    modifier = Modifier.border(
                                        width = Dimensions.size2pt5,
                                        color = GreenLIGHT,
                                        shape = CircleShape
                                    ),
                                    size = Dimensions.padding40,
                                    imageUrl = "https://picsum.photos/id/1/200/300"
                                )
                                Image(
                                    painter = painterResource(R.drawable.ic_online_icon),
                                    contentDescription = "Online Status",
                                    modifier = Modifier
                                        .size(Dimensions.size14)
                                        .align(Alignment.BottomEnd)
                                )
                            }
                            Spacer(Modifier.width(Dimensions.size5))
                            Column(modifier = Modifier) {
                                Row() {
                                    val originalText = "Sooraj Rajaneeeeeeeeee"
                                    val maxLength = 20

                                    Text(
                                        text = originalText.take(maxLength),
                                        style = TypographyBold.bodySmall,
                                        color = NeutralBlack,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Spacer(Modifier.width(Dimensions.size5))
                                    Row(
                                        modifier = Modifier
                                            .background(
                                                color = GrayLight10,
                                                shape = RoundedCornerShape(Dimensions.size5)
                                            )
                                            .height(Dimensions.padding16)
                                            .padding(
                                                start = Dimensions.size5,
                                                end = Dimensions.size5,
//                                            top = Dimensions.size5,
//                                             bottom =  Dimensions.size2pt5
                                            ),
                                        verticalAlignment = Alignment.CenterVertically,


                                        ) {
                                        Image(
                                            painter = painterResource(R.drawable.ic_spanner),
                                            contentDescription = "",
                                            colorFilter = ColorFilter.tint(NeutralBlack),
                                        )
                                        Spacer(Modifier.width(Dimensions.size4))
                                        Text(
                                            text = "Mechanic",
                                            style = Typography.bodySmall.copy(fontSize = Dimensions.textSize12),
                                            color = NeutralBlack,
                                            modifier = Modifier,

                                            )
                                    }
                                }
                                Row() {

                                    Image(
                                        painter = painterResource(R.drawable.ic_bike),
                                        contentDescription = "",
                                        colorFilter = ColorFilter.tint(NeutralDarkGrey),
                                        modifier = Modifier
                                            .width(Dimensions.size10)
                                            .height(Dimensions.size10),
                                    )
                                    Spacer(Modifier.width(Dimensions.size4))
                                    Text(
                                        text = "Harley Davidson 750",
                                        style = Typography.bodySmall.copy(fontSize = Dimensions.textSize12),
                                        color = NeutralDarkGrey,
                                        modifier = Modifier,

                                        )
                                }
                            }
                        }
                        Image(
                            painter = painterResource(R.drawable.ic_radio_btn_back_gray),
                            contentDescription = ""
                        )
                    }
                }
            }
            Spacer(Modifier.height(Dimensions.padding2))
        }
    }

}

@Preview
@Composable
fun Participantreview() {
    var vimodel: CreateRideScreenViewModel = viewModel()
    ParticipantSection(mod = Modifier,vimodel)
}