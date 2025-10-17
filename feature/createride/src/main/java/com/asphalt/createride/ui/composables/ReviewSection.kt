package com.asphalt.createride.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.BlueLight
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GreenLIGHT10
import com.asphalt.commonui.theme.GreenLIGHT25
import com.asphalt.commonui.theme.MagentaDeep
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.OrangeLight
import com.asphalt.commonui.theme.OrangeLight10
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.utils.ComposeUtils.ColorIconRounded
import com.asphalt.createride.viewmodel.CreateRideScreenViewModel

@Composable
fun ReviewSection(viewModel: CreateRideScreenViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = Dimensions.padding16,
                end = Dimensions.padding16,
            )
            .background(
                color = NeutralLightPaper, shape = RoundedCornerShape(Dimensions.size10)
            )
    ) {
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Text(
            text = "Review Your Ride",
            style = TypographyMedium.bodyMedium,
            color = NeutralBlack,
            modifier = Modifier.padding(start = Dimensions.padding16)
        )
        Spacer(modifier = Modifier.height(Dimensions.size8))
        Box(
            modifier = Modifier.padding(
                start = Dimensions.padding16,
                end = Dimensions.padding16
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.size73)
                    .background(
                        color = NeutralWhite,
                        shape = RoundedCornerShape(Dimensions.size10)
                    )
                    .padding(start = Dimensions.padding16, end = Dimensions.padding16),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    ColorIconRounded(backColor = BlueLight, resId = R.drawable.ic_navigate)
                    Spacer(modifier = Modifier.width(Dimensions.size8))
                    Column {
                        Text(
                            text = viewModel.rideDetailsState.value.rideTitle ?: "",
                            style = TypographyMedium.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${stringResource(R.string.refreshment_to)} ${viewModel.rideDetailsState.value.endLocation?:""}",
                            style = Typography.bodySmall,
                            color = NeutralDarkGrey
                        )
                    }

                }
                Box(
                    modifier = Modifier
                        .background(
                            color = GreenLIGHT10,
                            shape = RoundedCornerShape(Dimensions.size5)
                        )
                        .padding(
                            start = Dimensions.size5,
                            end = Dimensions.size5,
                            top = Dimensions.size8,
                            bottom = Dimensions.size8
                        ), contentAlignment = Alignment.Center


                ) {
                    Text(
                        text = viewModel.rideDetailsState.value.rideType?.uppercase() ?: "",
                        style = Typography.bodySmall.copy(fontSize = Dimensions.textSize12),
                        color = GreenLIGHT25,
                        modifier = Modifier
                    )
                }

            }
        }
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Box(
            modifier = Modifier.padding(
                start = Dimensions.padding16,
                end = Dimensions.padding16
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.size73)
                    .background(
                        color = NeutralWhite,
                        shape = RoundedCornerShape(Dimensions.size10)
                    )
                    .padding(start = Dimensions.padding16, end = Dimensions.padding16),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    ColorIconRounded(backColor = OrangeLight, resId = R.drawable.ic_calender_white)
                    Spacer(modifier = Modifier.width(Dimensions.size8))
                    Column {
                        Text(
                            text = stringResource(R.string.date_time),
                            style = TypographyMedium.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = viewModel.rideDetailsState.value.dateString ?: "",
                            style = Typography.bodySmall,
                            color = NeutralDarkGrey
                        )
                    }

                }


            }
        }
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Box(
            modifier = Modifier.padding(
                start = Dimensions.padding16,
                end = Dimensions.padding16
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.size73)
                    .background(
                        color = NeutralWhite,
                        shape = RoundedCornerShape(Dimensions.size10)
                    )
                    .padding(start = Dimensions.padding16, end = Dimensions.padding16),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    ColorIconRounded(backColor = MagentaDeep, resId = R.drawable.ic_route_white)
                    Spacer(modifier = Modifier.width(Dimensions.size8))
                    Column {
                        Text(
                            text = stringResource(R.string.route),
                            style = TypographyMedium.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${viewModel.rideDetailsState.value.startLocation ?: ""} - ${viewModel.rideDetailsState.value.endLocation ?: ""}",
                            style = Typography.bodySmall,
                            color = NeutralDarkGrey
                        )
                    }

                }

            }
        }
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Box(
            modifier = Modifier.padding(
                start = Dimensions.padding16,
                end = Dimensions.padding16
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.size73)
                    .background(
                        color = NeutralWhite,
                        shape = RoundedCornerShape(Dimensions.size10)
                    )
                    .padding(start = Dimensions.padding16, end = Dimensions.padding16),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    ColorIconRounded(backColor = OrangeLight10, resId = R.drawable.ic_group_white)
                    Spacer(modifier = Modifier.width(Dimensions.size8))
                    Column {
                        Text(
                            text = stringResource(R.string.participants),
                            style = TypographyMedium.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${viewModel.ridersList.value.count{it.isSelect}} ${stringResource(R.string.riders_selected)}",
                            style = Typography.bodySmall,
                            color = NeutralDarkGrey
                        )
                    }

                }

            }
        }
//added adding
        Spacer(modifier = Modifier.height(Dimensions.padding16))
    }
}

@Preview
@Composable
fun ReviewPreview() {
    var vimodel: CreateRideScreenViewModel = viewModel()
    ReviewSection(vimodel)
}