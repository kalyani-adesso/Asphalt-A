package com.asphalt.dashboard.composables.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.TypographyBlack
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.utils.Utils
import java.text.DateFormatSymbols
import java.util.Calendar

@Composable
fun RideMonthYear(calendarState: MutableState<Calendar>) {
    val calendar = calendarState.value
    val isArrowEnabled = Utils.isBeforeCurrentMonthAndYear(calendar)

    Column(
        modifier = Modifier
            .height(115.dp)
            .width(Dimensions.spacing32),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_double_arrow_up_enabled),
            contentDescription = null,
            modifier = Modifier.clickable {
                calendarState.value = (calendar.clone() as Calendar).apply {
                    add(Calendar.MONTH, -1)
                }
            }
        )
        Spacer(modifier = Modifier.height(Dimensions.spacing15pt7))

        Text(
            text = DateFormatSymbols().shortMonths[calendar.get(Calendar.MONTH)],
            style = TypographyBlack.bodyLarge,
            fontWeight = FontWeight.Medium,
            fontSize = Dimensions.textSize18
        )
        Spacer(modifier = Modifier.height(Dimensions.spacing5))

        Text(
            text = (calendar.get(Calendar.YEAR) % 100).toString(),
            style = TypographyBold.headlineLarge,
            fontSize = Dimensions.textSize30,
        )

        val arrowIcon: Int =
            if (isArrowEnabled)
                R.drawable.ic_double_arrow_down_enabled
            else
                R.drawable.ic_double_arrow_down_disabled
        Spacer(modifier = Modifier.height(Dimensions.spacing15pt7))

        Image(
            painter = painterResource(arrowIcon), contentDescription = null,
            modifier = Modifier.clickable(enabled = isArrowEnabled) {
                calendarState.value = (calendar.clone() as Calendar).apply {
                    add(Calendar.MONTH, 1)
                }
            }

        )
    }
}