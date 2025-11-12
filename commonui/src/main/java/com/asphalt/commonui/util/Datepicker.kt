package com.asphalt.commonui.util

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.BlueLight
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import java.time.LocalDate
import java.util.Calendar

@Composable
fun DatePickerSample(onCancel: () -> Unit, onOkClick: (timeMils: Long?) -> Unit) {
    //val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    var pickedDate by remember { mutableStateOf<LocalDate?>(null) }

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
        onDismissRequest = { onCancel.invoke()/*showDatePicker = false*/ },
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedDateMillis = datePickerState.selectedDateMillis
                    //val selectedDateSeconds = selectedDateMillis?.let { it / 1000L }

//                        selectedDateMillis?.let {
//                            pickedDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault())
//                                .toLocalDate()
//                        }
                    // showDatePicker = false
                    onOkClick.invoke(selectedDateMillis)
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
            TextButton(onClick = {
                //showDatePicker = false
                onCancel.invoke()
            }) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = Typography.bodyMedium,
                    color = BlueLight
                )
            }
        }
    , colors = DatePickerDefaults.colors(containerColor = NeutralWhite)) {
        DatePicker(
            state = datePickerState,
            showModeToggle = false,
            title = {
                Text(
                    text = stringResource(R.string.select_date), style = TypographyMedium.bodySmall,
                    modifier = Modifier.padding(
                        start = Dimensions.padding16,
                        top = Dimensions.padding16
                    )
                )
            },
            colors = DatePickerDefaults.colors(
                headlineContentColor = NeutralBlack,
                selectedDayContainerColor = PrimaryDarkerLightB75,
                containerColor = NeutralWhite
            )
        )
    }

}