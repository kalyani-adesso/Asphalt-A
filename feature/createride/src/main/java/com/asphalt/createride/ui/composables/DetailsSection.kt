package com.asphalt.createride.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralBlackGrey
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.theme.VividRed
import com.asphalt.commonui.util.CustomTimePickerDialog
import com.asphalt.commonui.util.DatePickerSample
import com.asphalt.commonui.utils.Utils
import com.asphalt.createride.viewmodel.CreateRideScreenViewModel

@Composable
fun DetailsSection(viewModel: CreateRideScreenViewModel) {
    var context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }
    var anchorWidth by remember { mutableStateOf(0) }
    val rideType = viewModel.getRideType(context)
    val am = stringResource(R.string.am)
    val pm = stringResource(R.string.pm)
    //Start Date
    if (viewModel.show_timePicker.value) {
        CustomTimePickerDialog(onDismiss = {
            viewModel.showTimePicker(false)
        }, onTimeSelected = { hr, min, isAm ->
            var time_text = "$hr:${String.format("%02d", min)} ${
                if (isAm) {
                    am
                } else {
                    pm
                }
            }"
            viewModel.updateTime(hr, min, isAm, time_text)
            viewModel.showTimePicker(false)
            viewModel._showRideTimeError.value = false
        })
    }

    if (viewModel.show_datePicker.value) {
        DatePickerSample(onCancel = {
            viewModel.showDatePicker(false)
        }, onOkClick = { timeMils ->
            viewModel.updateDate(timeMils, Utils.convertMillisToFormattedDate(timeMils))
            //datepicker = Utils.convertMillisToFormattedDate(timeMils) //timeMils?.toString() ?: ""
            viewModel.showDatePicker(false)
            viewModel._showRideDateError.value = false
        })
    }
//End Date
    if (viewModel.show_EndTimePicker.value) {
        CustomTimePickerDialog(onDismiss = {
            viewModel.showEndTimePicker(false)
        }, onTimeSelected = { hr, min, isAm ->
            var time_text = "$hr:${String.format("%02d", min)} ${
                if (isAm) {
                    am
                } else {
                    pm
                }
            }"
            viewModel.updateEndTime(hr, min, isAm, time_text)
            viewModel.showEndTimePicker(false)
            viewModel._showRideEndTimeError.value = false
        })
    }

    if (viewModel.show_EndDatePicker.value) {
        DatePickerSample(onCancel = {
            viewModel.showEndDatePicker(false)
        }, onOkClick = { timeMils ->
            viewModel.updateEndDate(timeMils, Utils.convertMillisToFormattedDate(timeMils))
            //datepicker = Utils.convertMillisToFormattedDate(timeMils) //timeMils?.toString() ?: ""
            viewModel.showEndDatePicker(false)
            viewModel._showRideEndDateError.value = false
        })
    }

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
            text = stringResource(R.string.ride_type),
            style = TypographyMedium.bodyMedium,
            color = NeutralBlack,
            modifier = Modifier.padding(start = Dimensions.padding16)
        )
        Spacer(modifier = Modifier.height(Dimensions.size8))
        Box(
            modifier = Modifier
                .padding(start = Dimensions.padding16, end = Dimensions.padding16)
                .onGloballyPositioned { coordinates ->
                    anchorWidth = coordinates.size.width
                }) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.padding50)
                    //.padding(start = Dimensions.padding16, end = Dimensions.padding16)
                    .background(
                        NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10)
                    )
                    .then(
                        if (viewModel._showRideTypeError.value) {
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
                    )
                    .clickable {
                        expanded = true
                    }
                    .onGloballyPositioned { coordinates ->
                        anchorWidth = coordinates.size.width // capture width in pixels
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (!viewModel.rideDetailsState.value.rideType.isNullOrEmpty()) {
                        viewModel.rideDetailsState.value.rideType.toString()
                    } else {
                        stringResource(R.string.select_ride_type)
                    },
                    style = Typography.bodyMedium,
                    color = if (!viewModel.rideDetailsState.value.rideType.isNullOrEmpty()) NeutralBlackGrey else NeutralDarkGrey,
                    modifier = Modifier.padding(start = Dimensions.padding16)
                )
                Image(
                    painter = painterResource(R.drawable.ic_dropdown_arrow),
                    contentDescription = "",
                    modifier = Modifier.padding(end = Dimensions.padding16)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { anchorWidth.toDp() })
                    .background(NeutralWhite)
            ) {
                rideType.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.rideType, style = Typography.bodySmall) },
                        onClick = {
                            viewModel.updateParticipantTab(option.id != Constants.SOLO_RIDE)
                            viewModel._showRideTypeError.value = false
                            viewModel.updateRiderType(option.rideType)
                            expanded = false
                        })
                }
            }
        }
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Text(
            text = stringResource(R.string.ride_title),
            style = TypographyMedium.bodyMedium,
            color = NeutralBlack,
            modifier = Modifier.padding(start = Dimensions.padding16)
        )
        Spacer(modifier = Modifier.height(Dimensions.size8))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimensions.padding50)
                .padding(start = Dimensions.padding16, end = Dimensions.padding16)
                .background(
                    NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10)
                )
                .then(
                    if (viewModel._showRideTitleError.value) {
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
                value = if (!viewModel.rideDetailsState.value.rideTitle.isNullOrEmpty()) {
                    viewModel.rideDetailsState.value.rideTitle.toString()
                } else {
                    ""
                },
                onValueChange = {
                    viewModel.updateRiderTitle(it)
                    viewModel._showRideTitleError.value = false
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.enter_ride_name),
                        style = Typography.bodyMedium,
                        color = NeutralDarkGrey,

                        )
                },
                textStyle = Typography.bodyMedium,
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

                )

        }
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Text(
            text = stringResource(R.string.description),
            style = TypographyMedium.bodyMedium,
            color = NeutralBlack,
            modifier = Modifier.padding(start = Dimensions.padding16)
        )
        Spacer(modifier = Modifier.height(Dimensions.size8))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimensions.padding16, end = Dimensions.padding16)
                .background(
                    NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = if (!viewModel.rideDetailsState.value.description.isNullOrEmpty()) {
                    viewModel.rideDetailsState.value.description.toString()
                } else {
                    ""
                },
                onValueChange = { viewModel.updateRiderDesc(it) },
                placeholder = {
                    Text(
                        text = stringResource(R.string.describe_vibe),
                        style = Typography.bodyMedium,
                        color = if (!viewModel.rideDetailsState.value.description.isNullOrEmpty()) NeutralBlackGrey else NeutralDarkGrey,

                        )
                },
                textStyle = Typography.bodyMedium,
                maxLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = Dimensions.padding80),

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
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimensions.padding16, end = Dimensions.padding16),
            horizontalArrangement = Arrangement.spacedBy(Dimensions.size10)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.start_date),
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
                            NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10),
                        )
                        .then(
                            if (viewModel._showRideDateError.value) {
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
                        )
                        .clickable { viewModel.showDatePicker(true) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(Dimensions.size10))
                    Image(
                        painter = painterResource(R.drawable.ic_calendar_blue),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(NeutralDarkGrey),
                        modifier = Modifier
                            .height(Dimensions.spacing20)
                            .width(Dimensions.spacing20)
                    )
                    Spacer(Modifier.width(Dimensions.size10))
                    Text(
                        text = if (viewModel.rideDetailsState.value.dateString.isNullOrEmpty()) {
                            stringResource(R.string.pick_date)
                        } else {
                            viewModel.rideDetailsState.value.dateString.toString()
                        },
                        style = Typography.bodyMedium,
                        color = if (viewModel.rideDetailsState.value.dateString.isNullOrEmpty()) NeutralDarkGrey else NeutralBlackGrey,
                        modifier = Modifier
                    )
                }

            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.time),
                    style = TypographyMedium.bodyMedium,
                    color = NeutralBlack,
                    // modifier = Modifier.padding(start = Dimensions.padding16)
                )
                Spacer(modifier = Modifier.height(Dimensions.size8))
                Row(
                    modifier = Modifier
                        .height(Dimensions.padding50)
                        .fillMaxWidth()
                        .background(
                            NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10),
                        )
                        .then(
                            if (viewModel._showRideTimeError.value) {
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
                        )
                        .clickable {
                            viewModel.showTimePicker(true)
                        }, verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(Dimensions.size10))
                    Image(
                        painter = painterResource(R.drawable.ic_clock),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(NeutralDarkGrey),
                        modifier = Modifier
                            .height(Dimensions.spacing20)
                            .width(Dimensions.spacing20)
                    )
                    Spacer(Modifier.width(Dimensions.size10))
                    Text(
                        text = if (viewModel.rideDetailsState.value.displayTime.isNullOrEmpty()) {
                            stringResource(R.string.pick_time)
                        } else {
                            viewModel.rideDetailsState.value.displayTime.toString()
                        },
                        style = Typography.bodyMedium,
                        color = if (viewModel.rideDetailsState.value.displayTime.isNullOrEmpty()) NeutralDarkGrey else NeutralBlackGrey,
                        modifier = Modifier
                    )
                }

            }

        }
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimensions.padding16, end = Dimensions.padding16),
            horizontalArrangement = Arrangement.spacedBy(Dimensions.size10)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text =stringResource(R.string.end_date),
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
                            NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10),
                        )
                        .then(
                            if (viewModel._showRideEndDateError.value) {
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
                        )
                        .clickable { viewModel.showEndDatePicker(true) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(Dimensions.size10))
                    Image(
                        painter = painterResource(R.drawable.ic_calendar_blue),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(NeutralDarkGrey),
                        modifier = Modifier
                            .height(Dimensions.spacing20)
                            .width(Dimensions.spacing20)
                    )
                    Spacer(Modifier.width(Dimensions.size10))
                    Text(
                        text = if (viewModel.rideDetailsState.value.endDateString.isNullOrEmpty()) {
                            stringResource(R.string.pick_date)
                        } else {
                            viewModel.rideDetailsState.value.endDateString.toString()
                        },
                        style = Typography.bodyMedium,
                        color = if (viewModel.rideDetailsState.value.endDateString.isNullOrEmpty()) NeutralDarkGrey else NeutralBlackGrey,
                        modifier = Modifier
                    )
                }

            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.time),
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
                            NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10),
                        )
                        .then(
                            if (viewModel._showRideEndTimeError.value) {
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
                        )
                        .clickable {
                            viewModel.showEndTimePicker(true)
                        }, verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(Dimensions.size10))
                    Image(
                        painter = painterResource(R.drawable.ic_clock),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(NeutralDarkGrey),
                        modifier = Modifier
                            .height(Dimensions.spacing20)
                            .width(Dimensions.spacing20)
                    )
                    Spacer(Modifier.width(Dimensions.size10))
                    Text(
                        text = if (viewModel.rideDetailsState.value.endDisplayTime.isNullOrEmpty()) {
                            stringResource(R.string.pick_time)
                        } else {
                            viewModel.rideDetailsState.value.endDisplayTime.toString()
                        },
                        style = Typography.bodyMedium,
                        color = if (viewModel.rideDetailsState.value.endDisplayTime.isNullOrEmpty()) NeutralDarkGrey else NeutralBlackGrey,
                        modifier = Modifier
                    )
                }

            }

        }
        //padding
        Spacer(modifier = Modifier.height(Dimensions.padding16))

    }
}


@Preview
@Composable
fun DetailsPreview() {
    var vimodel: CreateRideScreenViewModel = viewModel()
    DetailsSection(vimodel)
}