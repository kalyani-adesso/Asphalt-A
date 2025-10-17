package com.asphalt.commonui.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.LightGray28
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.TypographyMedium
import java.util.Calendar

@Composable
fun CustomTimePickerDialog(
    onDismiss: () -> Unit, onTimeSelected: (hour: Int, minute: Int, isAm: Boolean) -> Unit
) {
    val calendar = remember { Calendar.getInstance() }
    val initialHour = calendar.get(Calendar.HOUR).let { if (it == 0) 12 else it }
    val initialMinute = calendar.get(Calendar.MINUTE)
    val initialIsAm = calendar.get(Calendar.AM_PM) == Calendar.AM

    var hour by remember { mutableStateOf(initialHour) }
    var minute by remember { mutableStateOf(initialMinute) }
    var isAm by remember { mutableStateOf(initialIsAm) }
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(Dimensions.padding16), color = NeutralWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimensions.padding16),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(R.string.select_time), style = Typography.titleMedium)

                Spacer(modifier = Modifier.height(Dimensions.padding16))

                // Time Picker Content
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = Dimensions.padding24, end = Dimensions.padding24
                        ),
                    //horizontalArrangement = Arrangement.spacedBy(Dimensions.padding24),

                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    TimePickerColumn(
                        label = stringResource(R.string.hours),
                        value = hour,
                        onIncrement = { hour = if (hour < 12) hour + 1 else 1 },
                        onDecrement = { hour = if (hour > 1) hour - 1 else 12 })

                    Text(
                        ":",
                        modifier = Modifier.padding(bottom = Dimensions.padding24),
                        style = TypographyBold.headlineMedium
                    )

                    TimePickerColumn(
                        label = stringResource(R.string.min),
                        value = minute,
                        valueFormatter = { "%02d".format(it) },
                        onIncrement = { minute = if (minute < 59) minute + 1 else 0 },
                        onDecrement = { minute = if (minute > 0) minute - 1 else 59 })

                    Spacer(modifier = Modifier.width(Dimensions.padding16))


                    Column {
                        Text(
                            text = stringResource(R.string.am), modifier = Modifier
                                .background(
                                    color = if (isAm) {
                                        PrimaryDarkerLightB75
                                    } else {
                                        LightGray28
                                    },
                                    shape = RoundedCornerShape(Dimensions.size10) // Rounded corners
                                )
                                .padding(
                                    start = Dimensions.padding16,
                                    end = Dimensions.padding16,
                                    top = Dimensions.size5,
                                    bottom = Dimensions.size5
                                )
                                .clickable {
                                    isAm = true
                                }, style = TypographyBold.titleLarge, color = if (isAm) {
                                NeutralWhite
                            } else {
                                NeutralBlack
                            }

                        )
                        Spacer(modifier = Modifier.height(Dimensions.size10))
                        Text(
                            text = stringResource(R.string.pm), modifier = Modifier
                                .background(
                                    color = if (isAm) {
                                        LightGray28

                                    } else {
                                        PrimaryDarkerLightB75
                                    },
                                    shape = RoundedCornerShape(Dimensions.size10) // Rounded corners
                                )
                                .padding(
                                    start = Dimensions.padding16,
                                    end = Dimensions.padding16,
                                    top = Dimensions.size5,
                                    bottom = Dimensions.size5
                                )
                                .clickable {
                                    isAm = false
                                }, style = TypographyBold.titleLarge, color = if (isAm) {
                                NeutralBlack

                            } else {
                                NeutralWhite
                            }
                        )

                    }


                }

                Spacer(modifier = Modifier.height(Dimensions.padding16))

                // Action Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = Dimensions.padding20),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = TypographyMedium.bodyMedium,
                        color = NeutralDarkGrey,
                        modifier = Modifier.clickable {
                            onDismiss()
                        }
                    )
                    Spacer(modifier = Modifier.width(Dimensions.size20))
                    Text(
                        stringResource(R.string.ok),
                        style = TypographyMedium.bodyMedium,
                        color = PrimaryDarkerLightB75,
                        modifier = Modifier.clickable {
                            onTimeSelected(hour, minute, isAm)
                            //onDismiss()
                        })
                }
                Spacer(modifier = Modifier.height(Dimensions.padding16))
            }
        }
    }
}

@Composable
fun <T> TimePickerColumn(
    label: String,
    value: T,
    valueFormatter: (T) -> String = { it.toString() },
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = onIncrement) {
            Image(
                painter = painterResource(R.drawable.ic_uparrow_black),
                contentDescription = "Increase $label"
            )
        }
        Text(
            text = valueFormatter(value),
            style = TypographyBold.headlineMedium,
            modifier = Modifier.padding(vertical = Dimensions.size8)
        )
        IconButton(onClick = onDecrement) {
            Image(
                painter = painterResource(R.drawable.ic_down_arrow),
                contentDescription = "Increase $label"
            )

        }
        Spacer(modifier = Modifier.height(Dimensions.size10))

        Text(label, style = Typography.bodySmall)
    }
}
