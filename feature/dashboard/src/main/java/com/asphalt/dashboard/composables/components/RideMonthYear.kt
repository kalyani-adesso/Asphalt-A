package com.asphalt.dashboard.composables.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralGrey80
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.utils.Utils
import com.asphalt.commonui.utils.Utils.isBeforeMonthAndYear
import com.asphalt.commonui.utils.Utils.toFullMonthYear
import com.asphalt.dashboard.viewmodels.PerMonthRideStatsViewModel

@Composable
fun RideMonthYear(viewModel: PerMonthRideStatsViewModel) {
    val calendar = viewModel.calendar.collectAsStateWithLifecycle()
    val isNextArrowEnabled = Utils.isBeforeCurrentMonthAndYear(calendar.value)
    val isBackArrowEnabled = viewModel.creationCalendar.isBeforeMonthAndYear(calendar.value)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {

            Text(stringResource(R.string.your_ride_stats), style = TypographyBold.bodyLarge)
            Text(
                text = calendar.value.toFullMonthYear().uppercase(),
                style = TypographyBold.bodySmall,
                fontSize = Dimensions.textSize12,
                color = NeutralGrey80
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        val backArrowIcon: Int =
            if (isBackArrowEnabled)
                R.drawable.ic_prev_enabled
            else
                R.drawable.ic_prev_disabled
        Image(
            painter = painterResource(backArrowIcon),
            contentDescription = null,
            modifier = Modifier.clickable(enabled = isBackArrowEnabled) {
                viewModel.loadPreviousMonth()
            }
        )
        Spacer(modifier = Modifier.width(Dimensions.size30))


        val nextArrowIcon: Int =
            if (isNextArrowEnabled)
                R.drawable.ic_next_enabled
            else
                R.drawable.ic_next_disabled

        Image(
            painter = painterResource(nextArrowIcon), contentDescription = null,
            modifier = Modifier.clickable(enabled = isNextArrowEnabled) {
                viewModel.loadNextMonth()
            }

        )
    }
}