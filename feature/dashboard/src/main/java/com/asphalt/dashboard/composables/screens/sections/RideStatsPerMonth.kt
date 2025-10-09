package com.asphalt.dashboard.composables.screens.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralGrey30
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.Constants
import com.asphalt.commonui.utils.Utils
import com.asphalt.dashboard.composables.components.RideMonthYear
import com.asphalt.dashboard.composables.components.RideStatBox
import com.asphalt.dashboard.constants.RideStatConstants
import com.asphalt.dashboard.data.PerMonthRideStatistics
import com.asphalt.dashboard.data.RideStat
import com.asphalt.dashboard.sealedclasses.RideStatType
import java.text.DateFormatSymbols
import java.util.Calendar

@Composable
fun RideStatsPerMonth(
    calendarState: MutableState<Calendar>, perMonthStatistics: PerMonthRideStatistics,
) {
    val rideStatsList = listOf(
        RideStat(RideStatType.TotalRides, perMonthStatistics.totalRides),
        RideStat(RideStatType.Locations, perMonthStatistics.locations),
        RideStat(RideStatType.TotalKms, perMonthStatistics.totalKms),
    )
    RoundedBox(
        borderStroke = Constants.DEFAULT_BORDER_STROKE,
        borderColor = NeutralGrey30,
        backgroundColor = NeutralLightPaper
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = Dimensions.padding14pt5, vertical = Dimensions.padding18),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RideMonthYear(calendarState)
            for (rideStat in rideStatsList) {
                Spacer(Modifier.weight(RideStatConstants.RIDE_STAT_SPACER_WEIGHT))
                RoundedBox(
                    modifier = Modifier
                        .weight(RideStatConstants.RIDE_STAT_WEIGHT)
                        .aspectRatio(RideStatConstants.RIDE_STAT_ASPECT_RATIO),
                    backgroundColor = rideStat.type.bgColor
                ) {
                    RideStatBox(
                        rideStat.type.iconRes,
                        rideStat.value.toString(),
                        stringResource(rideStat.type.descriptionRes)
                    )
                }
            }
        }
    }
}



@Preview
@Composable
fun RideStatsPreview() {
    val calendarState = remember { mutableStateOf(Calendar.getInstance()) }

    RideStatsPerMonth(calendarState, PerMonthRideStatistics(15, 25, 3000))


}
