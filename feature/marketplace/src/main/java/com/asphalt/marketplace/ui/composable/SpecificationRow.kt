package com.asphalt.marketplace.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.asphalt.commonui.theme.BodyXSBlack
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.LightGray45
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium

@Composable
fun SpecificationRow(heading1: String, value1: String, heading2: String, value2: String) {
    Row(modifier = Modifier.fillMaxWidth()){
        Column(modifier = Modifier.weight(1f)){
            Text(text =heading1, style = BodyXSBlack, color = LightGray45)
            Spacer(modifier = Modifier.height(Dimensions.size5))
            Text(text =value1, style = TypographyMedium.bodyLarge)
        }
        Column(modifier = Modifier.weight(1f)){
            Text(text =heading2, style = BodyXSBlack, color = LightGray45)
            Spacer(modifier = Modifier.height(Dimensions.size5))
            Text(text =value2, style = TypographyMedium.bodyLarge)
        }
    }

}

@Preview
@Composable
fun SpecificationRowPreview() {
    SpecificationRow("Year", "2024", "Kilometers", "3200 Km")
}
