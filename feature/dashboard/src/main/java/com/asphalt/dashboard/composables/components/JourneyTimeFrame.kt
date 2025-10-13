package com.asphalt.dashboard.composables.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralGrey30
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.dashboard.sealedclasses.AdventureJourneyTimeFrameChoices

@Composable
fun SelectJourneyTimeFrame(
    selectedItem: MutableState<AdventureJourneyTimeFrameChoices>,
    items: List<AdventureJourneyTimeFrameChoices>
) {
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
                text = stringResource(selectedItem.value.choiceRes),
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
                        stringResource(item.choiceRes),
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