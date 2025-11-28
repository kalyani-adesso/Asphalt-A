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
import com.asphalt.android.model.dashboard.DashboardDomain
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.DonutChart
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.dashboard.composables.components.RideGraphLegendUI
import com.asphalt.dashboard.composables.components.SelectJourneyTimeFrame
import com.asphalt.dashboard.sealedclasses.AdventureJourneyTimeFrameChoices
import com.asphalt.dashboard.sealedclasses.RideGraphLegend
import com.asphalt.dashboard.viewmodels.AdventureJourneyViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AdventureJourney(
    dashboardData: List<DashboardDomain>,
    adventureJourneyViewModel: AdventureJourneyViewModel = koinViewModel()
) {

    val options = AdventureJourneyTimeFrameChoices.getAllChoices()
    val selectedItem = remember { mutableStateOf(options[0]) }
    LaunchedEffect(dashboardData) {
        adventureJourneyViewModel.setDashboardData(dashboardData)
        adventureJourneyViewModel.fetchAdventureData(selectedItem.value.choiceId)

    }
    LaunchedEffect(selectedItem.value) {
        adventureJourneyViewModel.fetchAdventureData(selectedItem.value.choiceId)
    }
    val totalRides = adventureJourneyViewModel.totalRides.collectAsStateWithLifecycle()
    val colorList = adventureJourneyViewModel.colorList.collectAsStateWithLifecycle()
    val valueList = adventureJourneyViewModel.valueList.collectAsStateWithLifecycle()

    ComposeUtils.CommonContentBox(
        modifier = Modifier.fillMaxWidth(),
        isBordered = true, radius = Constants.DEFAULT_CORNER_RADIUS
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
                    Spacer(Modifier.height(Dimensions.size5))
                    Text(
                        stringResource(R.string.adventure_journey),
                        style = TypographyBold.headlineLarge,
                        fontSize = Dimensions.textSize18
                    )
                    Spacer(Modifier.height(Dimensions.size20))
                    Box {
                        DonutChart(
                            values = remember(valueList.value) { valueList.value },
                            colors = remember(colorList.value) { colorList.value })
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(painter = painterResource(R.drawable.ic_total_rides), null)
                            Spacer(Modifier.height(Dimensions.spacing10))
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
                Column(verticalArrangement = Arrangement.SpaceBetween) {
                    SelectJourneyTimeFrame(selectedItem, options)
                    Spacer(Modifier.height(Dimensions.size25))
                    RideGraphLegendUI(RideGraphLegend.TotalRides)
                    RideGraphLegendUI(RideGraphLegend.PlacesExplored)
                    RideGraphLegendUI(RideGraphLegend.RideGroups)
                    RideGraphLegendUI(RideGraphLegend.RideInvites)


                }
            }

        }
    }

}


