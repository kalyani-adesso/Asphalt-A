package com.asphalt.dashboard.composables.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralGrey80
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.utils.Utils
import com.asphalt.commonui.utils.Utils.toFullMonthYear
import com.asphalt.dashboard.viewmodels.PerMonthRideStatsViewModel
import java.text.DateFormatSymbols
import java.util.Calendar

@Composable
fun RideMonthYear(viewModel: PerMonthRideStatsViewModel) {
    val calendar = viewModel.calendar.collectAsStateWithLifecycle()
    val isArrowEnabled = Utils.isBeforeCurrentMonthAndYear(calendar.value)

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

        Image(
            painter = painterResource(R.drawable.ic_prev_enabled),
            contentDescription = null,
            modifier = Modifier.clickable {
                viewModel.loadPreviousMonth()
            }
        )
        Spacer(modifier = Modifier.width(Dimensions.size30))


        val arrowIcon: Int =
            if (isArrowEnabled)
                R.drawable.ic_next_enabled
            else
                R.drawable.ic_next_disabled

        Image(
            painter = painterResource(arrowIcon), contentDescription = null,
            modifier = Modifier.clickable(enabled = isArrowEnabled) {
              viewModel.loadNextMonth()
            }

        )
    }
}