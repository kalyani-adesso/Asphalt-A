package com.asphalt.createride.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
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
import com.asphalt.commonui.theme.BlueLight
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.createride.viewmodel.CreateRideScreenViewModel
import java.time.LocalDate
import java.util.Calendar

@Composable
fun DetailsSection(viewModel: CreateRideScreenViewModel) {
    var context = LocalContext.current
    var text by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var anchorWidth by remember { mutableStateOf(0) }
    val rideType = viewModel.getRideType(context)
    DatePickerSample()

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
            text = "Ride Type",
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
                    "Select ride type",
                    style = Typography.bodyMedium,
                    color = NeutralDarkGrey,
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
                            text = option.rideType
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Text(
            text = "Ride Title",
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
                        text = "Enter ride name...",
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

                )

        }
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Text(
            text = "Description",
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
                value = if (!text2.isEmpty()) {
                    text2
                } else {
                    ""
                },
                onValueChange = { text2 = it },
                placeholder = {
                    Text(
                        text = "Describe the vibe...",
                        style = Typography.bodyMedium,
                        color = NeutralDarkGrey,

                        )
                },
                textStyle = Typography.bodyMedium.copy(NeutralDarkGrey),
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
                    text = "Date",
                    style = TypographyMedium.bodyMedium,
                    color = NeutralBlack,
                    modifier = Modifier.padding(start = Dimensions.padding16)
                )
                Spacer(modifier = Modifier.height(Dimensions.size8))
                Row(
                    modifier = Modifier
                        .height(Dimensions.padding50)
                        .fillMaxWidth()
                        .background(
                            NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10),
                        ), verticalAlignment = Alignment.CenterVertically
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
                        "Pick Date",
                        style = Typography.bodyMedium,
                        color = NeutralDarkGrey,
                        modifier = Modifier
                    )
                }

            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Time",
                    style = TypographyMedium.bodyMedium,
                    color = NeutralBlack,
                    modifier = Modifier.padding(start = Dimensions.padding16)
                )
                Spacer(modifier = Modifier.height(Dimensions.size8))
                Row(
                    modifier = Modifier
                        .height(Dimensions.padding50)
                        .fillMaxWidth()
                        .background(
                            NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10),
                        ), verticalAlignment = Alignment.CenterVertically
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
                        "Pick Time",
                        style = Typography.bodyMedium,
                        color = NeutralDarkGrey,
                        modifier = Modifier
                    )
                }

            }

        }

        //padding
        Spacer(modifier = Modifier.height(Dimensions.padding16))
    }
}


@Composable
fun DatePickerSample() {
    //val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    var pickedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(true) }
    if (showDatePicker) {
        val calendar = remember {
            Calendar.getInstance().apply {
                // Set to start of day (optional)
                //set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }
        val currentDateMillis = calendar.timeInMillis
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = currentDateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateMillis = datePickerState.selectedDateMillis
                        val selectedDateSeconds = selectedDateMillis?.let { it / 1000L }
//                        selectedDateMillis?.let {
//                            pickedDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault())
//                                .toLocalDate()
//                        }
                        showDatePicker = false
                    }
                ) {
                    Text(
                        text = stringResource(R.string.ok),
                        style = Typography.bodyMedium,
                        color = BlueLight
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = Typography.bodyMedium,
                        color = BlueLight
                    )
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false,
                title = {
                    Text(
                        text = stringResource(R.string.select_date), style = Typography.bodySmall,
                        modifier = Modifier.padding(
                            start = Dimensions.padding16,
                            top = Dimensions.padding16
                        )
                    )
                },
                colors = DatePickerDefaults.colors(
                    headlineContentColor = NeutralBlack,
                    selectedDayContainerColor = PrimaryDarkerLightB75
                )
            )
        }
    }
}


@Preview
@Composable
fun DetailsPreview() {
    var vimodel: CreateRideScreenViewModel = viewModel()
    DetailsSection(vimodel)
}