package com.asphalt.dashboard.composables.screens.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.android.model.dashboard.DashboardDomain
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.LightPlatinum
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.commonui.utils.Utils.toCompressedString
import com.asphalt.dashboard.composables.components.RideMonthYear
import com.asphalt.dashboard.composables.components.RideStatBox
import com.asphalt.dashboard.constants.RideStatConstants
import com.asphalt.dashboard.viewmodels.PerMonthRideStatsViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun RideStatsPerMonth(
    dashboardSummary: List<DashboardDomain>,
    perMonthRideStatsViewModel: PerMonthRideStatsViewModel = koinViewModel()
) {
    LaunchedEffect(dashboardSummary) {
        perMonthRideStatsViewModel.setSummaryData(dashboardSummary)
        perMonthRideStatsViewModel.getRideStatsByDate()
    }
    val perMonthStats = perMonthRideStatsViewModel.perMonthStats.collectAsStateWithLifecycle()


    ComposeUtils.CommonContentBox(isBordered = true, radius = Constants.DEFAULT_CORNER_RADIUS) {
        Column(
            modifier = Modifier.padding(
                horizontal = Dimensions.padding20,
                vertical = Dimensions.padding18
            ),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing15)
        ) {
            RideMonthYear(perMonthRideStatsViewModel)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),

                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (rideStat in perMonthStats.value) {

                    RoundedBox(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(RideStatConstants.RIDE_STAT_ASPECT_RATIO),
                        backgroundColor = rideStat.type.bgColor,
                        borderColor = LightPlatinum,
                        borderStroke = Dimensions.spacing1,
                        cornerRadius = Dimensions.size8
                    ) {
                        RideStatBox(
                            rideStat.type.iconRes,
                            rideStat.value.toCompressedString(),
                            stringResource(rideStat.type.descriptionRes)
                        )


                    }
                    if (rideStat != perMonthStats.value.last())
                        Spacer(Modifier.width(Dimensions.size16))
                }
            }
        }
    }
}
