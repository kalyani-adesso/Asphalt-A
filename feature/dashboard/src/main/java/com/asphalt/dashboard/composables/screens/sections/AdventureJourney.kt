package com.asphalt.dashboard.composables.screens.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralGrey30
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBlack
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.DonutPreview
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.dashboard.composables.components.RideGraphLegendUI
import com.asphalt.dashboard.sealedclasses.RideGraphLegend

@Composable
fun AdventureJourney(options: List<String>) {
    val selectedItem = remember { mutableStateOf(options[0]) }
    val totalRidesInfo by remember { mutableIntStateOf(0) }
    RoundedBox(
        modifier = Modifier.fillMaxWidth(),
        borderColor = NeutralGrey30,
        backgroundColor = NeutralLightPaper
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
                SelectTimeFrame(selectedItem, options)


            }
            Text(
                stringResource(R.string.journey), style = TypographyBold.headlineLarge,
                fontSize = Dimensions.textSize18,
                modifier = Modifier.absoluteOffset(Dimensions.spacing0, Dimensions.spacingNeg4)
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Box {
                    DonutPreview()
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(painter = painterResource(R.drawable.ic_directions_bike), null)
                        Spacer(Modifier.height(Dimensions.spacing5))
                        Text(
                            stringResource(R.string.total_rides_info_chart, totalRidesInfo),
                            fontSize = Dimensions.textSize12,
                            style = TypographyBlack.bodySmall,
                            color = NeutralDarkGrey
                        )
                    }
                }
            }
            Spacer(Modifier.height(Dimensions.spacing15))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                RideGraphLegendUI(RideGraphLegend.TotalRides)
                Spacer(Modifier.width(Dimensions.size8))

                RideGraphLegendUI(RideGraphLegend.DistanceCovered)
                Spacer(Modifier.width(Dimensions.size8))


                RideGraphLegendUI(RideGraphLegend.PlacesExplored)
            }
            Spacer(Modifier.width(Dimensions.size8))


            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                RideGraphLegendUI(RideGraphLegend.RideGroups)
                Spacer(Modifier.width(Dimensions.size8))

                RideGraphLegendUI(RideGraphLegend.RideInvites)
            }
        }
    }

}

@Composable
fun SelectTimeFrame(selectedItem: MutableState<String>, items: List<String>) {
    var isExpanded by remember { mutableStateOf(false) }

    RoundedBox(
        borderColor = NeutralGrey30,
        modifier = Modifier
            .clickable {
                isExpanded = true
            },
        cornerRadius = Dimensions.radius5
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                horizontal = Dimensions.padding10,
                vertical = Dimensions.padding8pt5
            )
        ) {
            Text(
                text = selectedItem.value,
                fontSize = Dimensions.textSize12,
                style = Typography.bodySmall,
                color = NeutralDarkGrey
            )
            Image(
                painter = painterResource(R.drawable.ic_dropdown_arrow),
                null,
                modifier = Modifier.padding(Dimensions.padding5)
            )
        }
        DropdownMenu(expanded = isExpanded, { isExpanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(text = {
                    Text(
                        item,
                        fontSize = Dimensions.textSize12,
                        style = Typography.bodySmall
                    )
                }, onClick = {
                    selectedItem.value = item
                    isExpanded = false
                })
            }
        }
    }
}

@Preview
@Composable
fun AdventureJourneyPreview() {
    AdventureJourney(listOf("Last month", "Last 6 months", "Last 1 year"))
}