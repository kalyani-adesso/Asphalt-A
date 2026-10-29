package com.asphalt.marketplace

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import com.asphalt.marketplace.ui.ProductListScreen
import com.asphalt.marketplace.viewmodel.ProductListViewModel
import org.junit.Rule
import org.junit.Test

class ProductListScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun productListScreen_displaysUIElements() {

        composeTestRule.setContent {
            ProductListScreen(
                setTopAppBarState = {},
                productListViewmodel = ProductListViewModel(),
                postAddClick = {},
                productClick = {}
            )
        }

       /* // Check title/button text
        composeTestRule
            .onNodeWithText("POST AD")
            .assertIsDisplayed()
*/
        // Check search placeholder
        composeTestRule
            .onNodeWithText("Search items")
            .assertIsDisplayed()

        // Check product item
        composeTestRule
            .onAllNodesWithText("Yahama R15 2020")[0]
            .assertIsDisplayed()

        // Owner
        composeTestRule
            .onAllNodesWithText("Hari")[0]
            .assertIsDisplayed()

        // Price
        composeTestRule
            .onAllNodesWithText("₹ 50000")[0]
            .assertIsDisplayed()
    }

}