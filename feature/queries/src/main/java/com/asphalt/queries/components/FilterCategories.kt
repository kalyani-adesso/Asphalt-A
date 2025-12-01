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
import androidx.compose.ui.res.stringResource
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.queries.constants.QueryConstants.CATEGORY_ALL_ID
import com.asphalt.queries.sealedclasses.QueryCategories


@Composable
fun FilterCategories(selectedCategory: MutableIntState) {

    ComposeUtils.CommonContentBox {
        Row(
            modifier = Modifier
                .wrapContentWidth()
        ) {
            LazyRow(
                modifier = Modifier.padding(vertical = Dimensions.padding21),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.size10)
            ) {
                item { Spacer(Modifier.width(Dimensions.size10)) }
                item {
                    CategorySelector(
                        selectedCategory,
                        stringResource(R.string.all),
                        CATEGORY_ALL_ID
                    )
                }
                items(QueryCategories.getAllCategories()) {
                    CategorySelector(selectedCategory, stringResource(it.nameRes), it.id)
                }
                item { Spacer(Modifier.width(Dimensions.size10)) }

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
        endColor = if (isSelected) PrimaryDarkerLightB75 else NeutralWhite,
    ) {
        ComposeUtils.SectionTitle(
            categoryName,
            color = if (isSelected) NeutralWhite else NeutralBlack
        )
    }
}