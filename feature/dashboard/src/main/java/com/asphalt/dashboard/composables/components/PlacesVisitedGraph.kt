package com.asphalt.dashboard.composables.components

import android.icu.util.Calendar
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralGrey80
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.BarPreview
import com.asphalt.commonui.ui.RoundedBox
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PlacesVisitedGraph() {
    val calendar = Calendar.getInstance()
    val formatter = SimpleDateFormat(Constants.DATE_FORMAT_PLACES_VISITED, Locale.getDefault())
    val currentDate = calendar.time
    calendar.add(Calendar.MONTH, -Constants.NO_OF_MONTHS_PLACE_VISITED)
    val sevenMonthsAgo = calendar.time
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
                    "${formatter.format(sevenMonthsAgo).uppercase()} - ${
                        formatter.format(
                            currentDate
                        ).uppercase()
                    }",
                    color = NeutralGrey80,
                    style = TypographyBold.headlineLarge,
                    fontSize = Dimensions.textSize12
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(Dimensions.size5)) {
                RoundedBox(
                    backgroundColor = NeutralLightPaper, cornerRadius = Dimensions.size8
                ) {
                    Box(
                        modifier = Modifier.arrowPadding(),
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_prev_disabled),
                            null,
                            modifier = Modifier.clickable {

                            })
                    }
                }
                RoundedBox(
                    backgroundColor = NeutralLightPaper, cornerRadius = Dimensions.size8
                ) {
                    Box(
                        modifier = Modifier.arrowPadding()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_next_enabled),
                            null,
                            modifier = Modifier.clickable {

                            })
                    }
                }
            }
        }
        Spacer(Modifier.height(Dimensions.size25))
        BarPreview()
    }
}

fun Modifier.arrowPadding(): Modifier = this.then(
    Modifier.padding(
        start = Dimensions.padding13,
        end = Dimensions.padding9pt25,
        top = Dimensions.padding9pt25,
        bottom = Dimensions.padding9pt25
    )
)
