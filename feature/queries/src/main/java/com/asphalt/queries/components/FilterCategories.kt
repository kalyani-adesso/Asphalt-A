package com.asphalt.queries.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.queries.QueryConstants.CATEGORY_ALL_ID
import com.asphalt.queries.sealedclasses.Categories


@Composable
fun FilterCategories(selectedCategory: MutableIntState) {

    ComposeUtils.CommonContentBox {
        Row(
            modifier = Modifier
                .wrapContentWidth()
        ) {
            LazyRow(
                modifier = Modifier.padding(vertical = 21.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item { Spacer(Modifier.width(10.dp)) }
                item {
                    CategorySelector(selectedCategory, "All", CATEGORY_ALL_ID)
                }
                items(Categories.getAllCategories()) {
                    CategorySelector(selectedCategory, it.name, it.id)
                }
                item { Spacer(Modifier.width(10.dp)) }

            }

        }

    }
}

@Composable
fun CategorySelector(
    selectedCategory: MutableIntState,
    categoryName: String,
    currentCategory: Int
) {
    val isSelected = selectedCategory.intValue == currentCategory
    GradientButton(
        onClick = {
            selectedCategory.intValue = currentCategory
        },
        buttonRadius = Dimensions.size10,
        startColor = if (isSelected) PrimaryDarkerLightB75 else NeutralWhite,
        endColor = if (isSelected) PrimaryDarkerLightB50 else NeutralWhite,
    ) {
        ComposeUtils.SectionTitle(
            categoryName,
            color = if (isSelected) NeutralWhite else NeutralBlack
        )
    }
}