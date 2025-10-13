package com.asphalt.dashboard.composables.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralGrey80
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.ui.barchart.CustomBarChart
import com.asphalt.commonui.utils.Utils
import com.asphalt.dashboard.viewmodels.PlacesVisitedGraphViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlacesVisitedGraph(placesVisitedGraphViewModel: PlacesVisitedGraphViewModel = koinViewModel()) {
    val startDate = placesVisitedGraphViewModel.startDate.collectAsStateWithLifecycle()
    val endDate = placesVisitedGraphViewModel.endDate.collectAsStateWithLifecycle()
    val xValues = placesVisitedGraphViewModel.xValuesList.collectAsStateWithLifecycle()
    val yValues = placesVisitedGraphViewModel.yValueList.collectAsStateWithLifecycle()
    val isArrowEnabled = placesVisitedGraphViewModel.isArrowEnabled.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimensions.size50)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    stringResource(R.string.places_visited_graph_title),
                    style = TypographyBold.headlineLarge,
                    fontSize = Dimensions.textSize16
                )

                Text(
                    "${
                        Utils.formatDateWithFormatter(
                            Constants.DATE_FORMAT_PLACES_VISITED,
                            startDate.value?.time
                        )?.uppercase()
                    } - ${
                        Utils.formatDateWithFormatter(
                            Constants.DATE_FORMAT_PLACES_VISITED,
                            endDate.value?.time
                        )?.uppercase()
                    }",
                    color = NeutralGrey80,
                    style = TypographyBold.headlineLarge,
                    fontSize = Dimensions.textSize12
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(Dimensions.size5)) {
                ArrowView {
                    Image(
                        painter = painterResource(R.drawable.ic_prev_enabled),
                        null,
                        modifier = Modifier.clickable {
                            placesVisitedGraphViewModel.fetchDataPreviousDateRange()
                        })
                }
                ArrowView {
                    val arrowIcon: Int =
                        if (isArrowEnabled.value)
                            R.drawable.ic_next_enabled
                        else
                            R.drawable.ic_next_disabled
                    Image(
                        painter = painterResource(arrowIcon),
                        null,
                        modifier = Modifier.clickable(enabled = isArrowEnabled.value) {
                            placesVisitedGraphViewModel.fetchDataNextDateRange()
                        })
                }
            }
        }
        CustomBarChart(xValues.value, yValues.value)
    }
}

@Composable
private fun ArrowView(
    boxContent: @Composable BoxScope.() -> Unit,
) {
    RoundedBox(
        backgroundColor = NeutralLightPaper, cornerRadius = Dimensions.size8
    ) {
        Box(
            modifier = Modifier.arrowPadding()
        ) {
            boxContent()
        }
    }
}

private fun Modifier.arrowPadding(): Modifier = this.then(
    Modifier.padding(
        start = Dimensions.padding13,
        end = Dimensions.padding9pt25,
        top = Dimensions.padding9pt25,
        bottom = Dimensions.padding9pt25
    )
)
