package com.asphalt.commonui.util

import android.app.TimePickerDialog
import android.icu.util.Calendar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.PrimaryDarkerLightB75

@Composable
fun ShowDefaultTimePicker(
    onDismiss: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int, isAm: Boolean) -> Unit,
    hour: Int?,
    minute: Int?,
    isAm: Boolean
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance().apply {
        val hour24 = when {
            hour == null -> get(Calendar.HOUR_OF_DAY)
            hour == 12 && isAm -> 0
            hour == 12 && !isAm -> 12
            isAm -> hour
            else -> hour + 12
        }
        set(Calendar.HOUR_OF_DAY, hour24)
        set(Calendar.MINUTE, minute ?: get(Calendar.MINUTE))
    }

    val timePickerDialog = remember {
        TimePickerDialog(
            context,R.style.MyTimePickerTheme,
            { _, selectedHour24, selectedMinute ->

                val isAmSelected = selectedHour24 < 12

                val hour12 = when {
                    selectedHour24 == 0 -> 12
                    selectedHour24 > 12 -> selectedHour24 - 12
                    else -> selectedHour24
                }

                onTimeSelected(hour12, selectedMinute, isAmSelected)
                onDismiss()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        ).apply {
            setOnCancelListener {
                onDismiss()
            }
        }
    }


    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(showDialog) {
        if (showDialog) {
            timePickerDialog.show()
            try {
                timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE)
                    ?.setTextColor(PrimaryDarkerLightB75.toArgb())

                timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE)
                    ?.setTextColor(NeutralDarkGrey.toArgb())
            } catch (e: Exception) {
                // ignore styling errors
            }
        }
    }

    LaunchedEffect(hour, minute, isAm) {
        showDialog = true
    }
}