package com.asphalt.android.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asphalt.android.constants.BottomBarConstants.BOTTOM_BAR_ICON_ASPECT_RATIO
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.RoundedBox

@Composable
fun BottomBar(
    items: List<BottomNavItems>,
    selectedKey: AppNavKey,
    onItemSelected: (AppNavKey) -> Unit,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(Dimensions.spacing75)
                .padding(horizontal = Dimensions.size30, vertical = Dimensions.size12pt5),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val itemModifier = Modifier
                .aspectRatio(BOTTOM_BAR_ICON_ASPECT_RATIO)
                .fillMaxHeight()
            items.forEach { item ->
                val isSelected = item.key == selectedKey
                RoundedBox(
                    modifier = itemModifier.clickable {
                        onItemSelected(item.key)
                    }, contentAlignment = Alignment.Center,
                    backgroundColor = if (isSelected) PrimaryDarkerLightB75 else NeutralWhite
                ) {
                    Column(
                        modifier = Modifier.padding(
                            top = Dimensions.padding8,
                            bottom = Dimensions.padding2
                        ),
                        verticalArrangement = Arrangement.spacedBy(Dimensions.size8),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(item.iconRes), null,
                            tint = if (!isSelected) NeutralDarkGrey else NeutralWhite
                        )

                        Text(
                            text = item.title,
                            style = TypographyMedium.bodyMedium,
                            fontSize = Dimensions.textSize14,
                            color = if (!isSelected) NeutralDarkGrey else NeutralWhite

                        )
                    }
                }
            }
        }
    }
}



