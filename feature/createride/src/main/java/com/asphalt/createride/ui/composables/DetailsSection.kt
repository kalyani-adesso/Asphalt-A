package com.asphalt.createride.ui.composables

import android.icu.util.Calendar
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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.testTag
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
import com.asphalt.commonui.util.ShowDefaultTimePicker
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

    val showDatePicker by viewModel.showDatePicker.collectAsState()
    val showEndDatePicker by viewModel.showEndDatePicker.collectAsState()
    val showTimePicker by viewModel.showTimePicker.collectAsState()
    val showEndTimePicker by viewModel.showEndTimePicker.collectAsState()
    val rideDetails by viewModel.rideDetailsState.collectAsState()
    val showRideTypeError by viewModel.showRideTypeError.collectAsState()
    val showRideTitleError by viewModel.showRideTitleError.collectAsState()
    val showRideDateError by viewModel.showRideDateError.collectAsState()
    val showRideEndDateError by viewModel.showRideEndDateError.collectAsState()
    val showRideTimeError by viewModel.showRideTimeError.collectAsState()
    val showRideEndTimeError by viewModel.showRideEndTimeError.collectAsState()

    val tagRideType = if (showRideTypeError) "Ride_Type_Error" else "Ride_Type"
    //Start Date
    if (showTimePicker) {//CustomTimePickerDialog

        ShowDefaultTimePicker(onDismiss = {
            viewModel.setShowTimePicker(false)
        }, onTimeSelected = { hr, min, isAm ->
            var time_text = "$hr:${String.format("%02d", min)} ${
                if (isAm) {
                    am
                } else {
                    pm
                }
            }"
            viewModel.updateTime(hr, min, isAm, time_text)
            viewModel.setShowTimePicker(false)
            viewModel.setShowRideTimeError(false)
        },rideDetails.hour,
            rideDetails.mins,
            rideDetails.isAm)
    }

    if (showDatePicker) {
        DatePickerSample(onCancel = {
            viewModel.setShowDatePicker(false)
        },rideDetails.dateMils,onOkClick = { timeMils ->
            viewModel.updateDate(timeMils, Utils.convertMillisToFormattedDate(timeMils))
            viewModel.setShowDatePicker(false)
            viewModel.setShowRideDateError(false)
        })
    }
//End Date
    if (showEndTimePicker) {
        ShowDefaultTimePicker(
            onDismiss = {
                viewModel.setShowEndTimePicker(false)
            },
            onTimeSelected = { hr, min, isAm ->
                var time_text = "$hr:${String.format("%02d", min)} ${
                    if (isAm) {
                        am
                    } else {
                        pm
                    }
                }"
                viewModel.updateEndTime(hr, min, isAm, time_text)
                viewModel.setShowEndTimePicker(false)
                viewModel.setShowRideEndTimeError(false)
            },
            hour = rideDetails.endHour ,
            minute = rideDetails.endMins ,
            isAm = rideDetails.isEndAm
        )
    }

    if (showEndDatePicker) {
        DatePickerSample(onCancel = {
            viewModel.setShowEndDatePicker(false)
        },rideDetails.endDateMils,onOkClick = { timeMils ->
            viewModel.updateEndDate(timeMils, Utils.convertMillisToFormattedDate(timeMils))
            viewModel.setShowEndDatePicker(false)
            viewModel.setShowRideEndDateError(false)
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
                    .height(Dimensions.padding50).testTag(tagRideType)
                    //.padding(start = Dimensions.padding16, end = Dimensions.padding16)
                    .background(
                        NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10)
                    )
                    .then(
                        if (showRideTypeError) {
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
                    text = if (!rideDetails.rideType.isNullOrEmpty()) {
                        rideDetails.rideType.toString()
                    } else {
                        stringResource(R.string.select_ride_type)
                    },
                    style = Typography.bodyMedium,
                    color = if (!rideDetails.rideType.isNullOrEmpty()) NeutralBlackGrey else NeutralDarkGrey,
                    modifier = Modifier.padding(start = Dimensions.padding16).testTag("Ride_Type_Text")
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
                            viewModel.setShowRideTypeError(false)
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
                    if (showRideTitleError) {
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
                value = if (!rideDetails.rideTitle.isNullOrEmpty()) {
                    rideDetails.rideTitle.toString()
                } else {
                    ""
                },
                onValueChange = {
                    viewModel.updateRiderTitle(it)
                    viewModel.setShowRideTitleError(false)
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
                    .fillMaxHeight().testTag("rideTitleInput"),

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
                value = if (!rideDetails.description.isNullOrEmpty()) {
                    rideDetails.description.toString()
                } else {
                    ""
                },
                onValueChange = { viewModel.updateRiderDesc(it) },
                placeholder = {
                    Text(
                        text = stringResource(R.string.describe_vibe),
                        style = Typography.bodyMedium,
                        color = if (!rideDetails.description.isNullOrEmpty()) NeutralBlackGrey else NeutralDarkGrey,

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
                            if (showRideDateError) {
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
                        .clickable { viewModel.setShowDatePicker(true) },
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
                        text = if (rideDetails.dateString.isNullOrEmpty()) {
                            stringResource(R.string.pick_date)
                        } else {
                            rideDetails.dateString.toString()
                        },
                        style = Typography.bodyMedium,
                        color = if (rideDetails.dateString.isNullOrEmpty()) NeutralDarkGrey else NeutralBlackGrey,
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
                            if (showRideTimeError) {
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
                            viewModel.setShowTimePicker(true)
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
                        text = if (rideDetails.displayTime.isNullOrEmpty()) {
                            stringResource(R.string.pick_time)
                        } else {
                            rideDetails.displayTime.toString()
                        },
                        style = Typography.bodyMedium,
                        color = if (rideDetails.displayTime.isNullOrEmpty()) NeutralDarkGrey else NeutralBlackGrey,
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
                            if (showRideEndDateError) {
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
                        .clickable { viewModel.setShowEndDatePicker(true) },
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
                        text = if (rideDetails.endDateString.isNullOrEmpty()) {
                            stringResource(R.string.pick_date)
                        } else {
                            rideDetails.endDateString.toString()
                        },
                        style = Typography.bodyMedium,
                        color = if (rideDetails.endDateString.isNullOrEmpty()) NeutralDarkGrey else NeutralBlackGrey,
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
                            if (showRideEndTimeError) {
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
                        text = if (rideDetails.endDisplayTime.isNullOrEmpty()) {
                            stringResource(R.string.pick_time)
                        } else {
                            rideDetails.endDisplayTime.toString()
                        },
                        style = Typography.bodyMedium,
                        color = if (rideDetails.endDisplayTime.isNullOrEmpty()) NeutralDarkGrey else NeutralBlackGrey,
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