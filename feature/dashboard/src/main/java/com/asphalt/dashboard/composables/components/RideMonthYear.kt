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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.utils.Utils
import com.asphalt.dashboard.viewmodels.PerMonthRideStatsViewModel
import java.text.DateFormatSymbols
import java.util.Calendar

@Composable
fun RideMonthYear(viewModel: PerMonthRideStatsViewModel) {
    val calendar = viewModel.calendar.collectAsStateWithLifecycle()
    val isArrowEnabled = Utils.isBeforeCurrentMonthAndYear(calendar.value)

    Column(
        modifier = Modifier
            .height(Dimensions.size115)
            .width(Dimensions.padding33),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_double_arrow_up_enabled),
            contentDescription = null,
            modifier = Modifier.clickable {
                viewModel.loadPreviousMonth()
            }
        )
        Spacer(modifier = Modifier.height(Dimensions.spacing15pt7))

        Text(
            text = Utils.getMonthAbbr(calendar.value),
            style = TypographyMedium.bodyLarge,
            fontWeight = FontWeight.Medium,
            fontSize = Dimensions.textSize17
        )
        Spacer(modifier = Modifier.height(Dimensions.spacing5))

        Text(
            text = (calendar.value.get(Calendar.YEAR) % 100).toString(),
            style = TypographyBold.headlineLarge,
            fontSize = Dimensions.textSize28,
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
              viewModel.loadNextMonth()
            }

        )
    }
}