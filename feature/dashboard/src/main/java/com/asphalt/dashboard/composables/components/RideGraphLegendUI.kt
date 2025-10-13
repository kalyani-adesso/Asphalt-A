package com.asphalt.dashboard.composables.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack10
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.dashboard.sealedclasses.RideGraphLegend

@Composable
fun RideGraphLegendUI(rideGraphLegend: RideGraphLegend) {
    RoundedBox(
        cornerRadius = Dimensions.radius2,
        borderColor = NeutralBlack10,
        borderStroke = Dimensions.padding1,
        modifier = Modifier
            .padding(Dimensions.padding2)
    ) {
        Row(
            modifier = Modifier.padding(Dimensions.padding6),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RoundedBox(
                modifier = Modifier
                    .height(Dimensions.size9)
                    .width(Dimensions.size9),
                backgroundColor = rideGraphLegend.color,
                cornerRadius = Dimensions.radius1
            ) { }
            Spacer(Modifier.width(Dimensions.size2pt5))
            Text(
                text = stringResource(rideGraphLegend.nameRes),
                style = Typography.bodySmall,
                fontSize = Dimensions.textSize12,
                color = NeutralDarkGrey
            )
        }
    }
}