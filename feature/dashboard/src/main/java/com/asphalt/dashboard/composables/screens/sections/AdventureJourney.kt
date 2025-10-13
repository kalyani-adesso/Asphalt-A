package com.asphalt.dashboard.composables.screens.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralGrey30
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.DonutChart
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.dashboard.composables.components.RideGraphLegendUI
import com.asphalt.dashboard.composables.components.SelectJourneyTimeFrame
import com.asphalt.dashboard.sealedclasses.AdventureJourneyTimeFrameChoices
import com.asphalt.dashboard.sealedclasses.RideGraphLegend
import com.asphalt.dashboard.viewmodels.AdventureJourneyViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdventureJourney(
    adventureJourneyViewModel: AdventureJourneyViewModel = koinViewModel()
) {
    val options = listOf(
        AdventureJourneyTimeFrameChoices.LastFourMonths,
        AdventureJourneyTimeFrameChoices.LastWeek, AdventureJourneyTimeFrameChoices.LastYear
    )
    val selectedItem = remember { mutableStateOf(options[0]) }
    LaunchedEffect(selectedItem.value) {
        adventureJourneyViewModel.fetchAdventureData(selectedItem.value.choiceId)
    }
    val totalRides = adventureJourneyViewModel.totalRides.collectAsStateWithLifecycle()
    val colorList = adventureJourneyViewModel.colorList.collectAsStateWithLifecycle()
    val valueList = adventureJourneyViewModel.valueList.collectAsStateWithLifecycle()

    RoundedBox(
        modifier = Modifier.fillMaxWidth(),
        borderColor = NeutralGrey30,
        backgroundColor = NeutralLightPaper,
        borderStroke = Dimensions.padding1
    ) {
        Column(
            modifier = Modifier.padding(
                top = Dimensions.padding16,
                start = Dimensions.padding20,
                end = Dimensions.padding20,
                bottom = Dimensions.padding24
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        stringResource(R.string.adventure),
                        style = TypographyBold.headlineLarge,
                        fontSize = Dimensions.textSize18
                    )
                }
                SelectJourneyTimeFrame(selectedItem, options)
            }
            Text(
                stringResource(R.string.journey), style = TypographyBold.headlineLarge,
                fontSize = Dimensions.textSize18,
                modifier = Modifier.absoluteOffset(Dimensions.spacing0, Dimensions.spacingNeg4)
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Box {
                    DonutChart(valueList.value, colorList.value)
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(painter = painterResource(R.drawable.ic_directions_bike), null)
                        Spacer(Modifier.height(Dimensions.spacing5))
                        Text(
                            stringResource(
                                R.string.total_rides_info_chart,
                                totalRides.value
                            ),
                            fontSize = Dimensions.textSize12,
                            style = TypographyMedium.bodySmall,
                            color = NeutralDarkGrey
                        )
                    }
                }
            }
            Spacer(Modifier.height(Dimensions.spacing15))
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    Dimensions.size8,
                    alignment = Alignment.CenterHorizontally
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                RideGraphLegendUI(RideGraphLegend.TotalRides)
                RideGraphLegendUI(RideGraphLegend.PlacesExplored)
                RideGraphLegendUI(RideGraphLegend.RideGroups)

            }
            Spacer(Modifier.height(Dimensions.size8))
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    Dimensions.size8,
                    alignment = Alignment.CenterHorizontally
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                RideGraphLegendUI(RideGraphLegend.RideInvites)
            }
        }
    }

}


