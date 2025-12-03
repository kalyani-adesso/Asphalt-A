package com.asphalt.profile.screens.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralLightGrey
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.profile.sealedclasses.StatType
import com.asphalt.profile.viewmodels.TotalStatsVM
import org.koin.androidx.compose.koinViewModel

@Composable
fun TotalStatisticsSection(statsVM: TotalStatsVM= koinViewModel()) {

    val stats = statsVM.stats.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        statsVM.getTotalStats()
    }
    ComposeUtils.CommonContentBox(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = Dimensions.padding16,
                vertical = Dimensions.padding20
            ), verticalArrangement = Arrangement.spacedBy(Dimensions.padding16)
        ) {
            ComposeUtils.SectionTitle(stringResource(R.string.total_statistics))
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimensions.size17),
                modifier = Modifier.fillMaxWidth()
            ) {
                StatView(StatType.PlacesStatType, stats.value.cityCount)
                StatView(StatType.RidesStatType, stats.value.rideCount)
            }
        }
    }
}

@Composable
fun RowScope.StatView(statType: StatType, count: Int) {
    RoundedBox(
        modifier = Modifier.weight(1f),
         borderColor = NeutralLightGrey,
        borderStroke = Dimensions.spacing1,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = Dimensions.padding15)
                .wrapContentWidth(),
            verticalArrangement = Arrangement.spacedBy(
                Dimensions.spacing10, Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(statType.iconRes), null)
            ComposeUtils.SectionTitle("$count ${stringResource(statType.statTypeResId)}")
            ComposeUtils.SectionSubtitle(
                stringResource(statType.statDescriptionResId),
                color = NeutralBlack, modifier = Modifier.offset(y = Dimensions.spacingNeg4)
            )

        }
    }

}