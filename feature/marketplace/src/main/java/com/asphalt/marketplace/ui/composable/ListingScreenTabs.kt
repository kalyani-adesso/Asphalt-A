package com.asphalt.marketplace.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.util.GetGradient
import com.asphalt.marketplace.constant.MarketPlaceConstants
import com.asphalt.marketplace.viewmodel.ProductListViewModel

@Composable
fun MainTabs(productListViewmodel: ProductListViewModel) {//PrimaryDarkerLightB75,NeutralBlack
    Row(
        modifier = Modifier
            .fillMaxWidth()
            /* .background(
                 NeutralLightPaper, shape = RoundedCornerShape(16.dp)
             )*/
            .padding(start = Dimensions.size10, end = Dimensions.size10),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.size10),

        ) {
        Box(
            modifier = Modifier
                .height(Dimensions.size50)
                .width(100.dp)
                .weight(1f)
                .clickable {
                    productListViewmodel.setMainTabController(MarketPlaceConstants.BROWSE)
                }
                .then(
                    if (productListViewmodel.mainTabController.value == MarketPlaceConstants.BROWSE) {
                        Modifier.background(
                            color = PrimaryDarkerLightB75,
                            shape = RoundedCornerShape(Dimensions.size10)
                        )
                    } else {
                        Modifier.background(
                            color = NeutralWhite, shape = RoundedCornerShape(Dimensions.size10)
                        )
                    }
                ), contentAlignment = Alignment.Center
        )

        // Rounded corners here

        {
            Text(
                text = stringResource(R.string.browse),
                style = TypographyMedium.titleMedium,
                color = if (productListViewmodel.mainTabController.value == MarketPlaceConstants.BROWSE)
                    NeutralWhite
                else
                    NeutralBlack
            )
        }

        Box(
            modifier = Modifier
                .height(Dimensions.size50)
                .width(100.dp)
                .weight(1f)
                .clickable {
                    productListViewmodel.setMainTabController(MarketPlaceConstants.FAVORITE)
                }
                .then(
                    if (productListViewmodel.mainTabController.value == MarketPlaceConstants.FAVORITE) {
                        Modifier.background(
                            color = PrimaryDarkerLightB75,
                            shape = RoundedCornerShape(Dimensions.size10)
                        )
                    } else {
                        Modifier.background(
                            color = NeutralWhite, shape = RoundedCornerShape(Dimensions.size10)
                        )
                    }
                ), contentAlignment = Alignment.Center
        )

        // Rounded corners here

        {
            Text(
                text = stringResource(R.string.favorites),
                style = TypographyMedium.titleMedium,
                color = if (productListViewmodel.mainTabController.value == MarketPlaceConstants.FAVORITE)
                    NeutralWhite
                else
                    NeutralBlack
            )
        }

        Box(
            modifier = Modifier
                .height(Dimensions.size50)
                .width(100.dp)
                .weight(1f)
                .clickable {
                    productListViewmodel.setMainTabController(MarketPlaceConstants.MYADDS)
                }
                .then(
                    if (productListViewmodel.mainTabController.value == MarketPlaceConstants.MYADDS) {
                        Modifier.background(
                            color = PrimaryDarkerLightB75,
                            shape = RoundedCornerShape(Dimensions.size10)
                        )
                    } else {
                        Modifier.background(
                            color = NeutralWhite, shape = RoundedCornerShape(Dimensions.size10)
                        )
                    }
                ), contentAlignment = Alignment.Center
        )

        // Rounded corners here

        {
            Text(
                text = stringResource(R.string.my_ads),
                style = TypographyMedium.titleMedium,
                color = if (productListViewmodel.mainTabController.value == MarketPlaceConstants.MYADDS)
                    NeutralWhite
                else
                    NeutralBlack
            )
        }

    }
}

@Composable
fun SubTabs(productListViewmodel: ProductListViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimensions.size92)
            .padding(start = Dimensions.padding16)
            .background(
                NeutralLightPaper, shape = RoundedCornerShape(Dimensions.padding10)
            )

    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            contentPadding = PaddingValues(start = Dimensions.padding5)
        ) {
            items(productListViewmodel.subTabController.value) { item ->
                Box(
                    modifier = Modifier
                        .then(
                            if (item.isSelected) {
                                Modifier.background(
                                    brush = GetGradient(
                                        PrimaryDarkerLightB75,
                                        PrimaryDarkerLightB75
                                    ),
                                    shape = RoundedCornerShape(Dimensions.size10)
                                )
                            } else {
                                Modifier.background(
                                    color = NeutralWhite,
                                    shape = RoundedCornerShape(Dimensions.size10)
                                )
                            }
                        )
                        .clickable {
                            productListViewmodel.updateSubMenu(item.id)
                        }
                        .padding(all = Dimensions.padding15),
                    contentAlignment = Alignment.Center

                    // Rounded corners here

                ) {
                    Text(
                        text = stringResource(item.title),//stringResource(R.string.upcoming),
                        style = TypographyMedium.titleMedium,
                        color = if (item.isSelected) {//viewModel.tabSelection.value == item.id
                            NeutralWhite
                        } else {
                            NeutralBlack
                        }
                    )
                }
                Spacer(modifier = Modifier.width(Dimensions.padding10))
            }
        }
    }
}