package com.asphalt.marketplace

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import com.asphalt.commonui.AppBarState
import com.asphalt.marketplace.ui.ProductDetailsScreen
import org.junit.Rule
import org.junit.Test

class ProductDetailsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun productDetailsScreen_displaysBasicContent() {

        composeTestRule.setContent {
            ProductDetailsScreen(
                setTopAppBarState = {}
            )
        }

        composeTestRule.onNodeWithText("2020 Royal Enfield")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("₹2,85,000")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Pre Owned Motorcycle")
            .assertIsDisplayed()
    }
    @Test
    fun productDetailsScreen_displaysLocationAndTime() {

        composeTestRule.setContent {
            ProductDetailsScreen(
                setTopAppBarState = {}
            )
        }

        composeTestRule.onNodeWithText("Kakkand, Kochi")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("2 Days ago")
            .assertIsDisplayed()
    }

    @Test
    fun productDetailsScreen_displaysSellerInformation() {

        composeTestRule.setContent {
            ProductDetailsScreen(
                setTopAppBarState = {}
            )
        }

        composeTestRule.onNodeWithText("Seller Information")
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Vyshnav")
            .assertIsDisplayed()
    }
    @Test
    fun productDetailsScreen_displaysDescription() {

        composeTestRule.setContent {
            ProductDetailsScreen(
                setTopAppBarState = {}
            )
        }

        composeTestRule.onNodeWithText("Description")
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(
            "Excellent condition 2022 Royal Enfield Classic 350 with only 3,200 km. Always garaged and regularly maintained. Includes aftermarket exhaust, custom seat, and LED lighting. Clean RC in hand. First owner, all service records available."
        ).assertIsDisplayed()
    }
    @Test
    fun productDetailsScreen_displaysSpecifications() {

        composeTestRule.setContent {
            ProductDetailsScreen(
                setTopAppBarState = {}
            )
        }

        composeTestRule.onNodeWithText("Specifications")
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Year")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("2024")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Kilometers")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("2000 km")
            .assertIsDisplayed()
    }
    @Test
    fun productDetailsScreen_displaysBottomButtons() {

        composeTestRule.setContent {
            ProductDetailsScreen(
                setTopAppBarState = {}
            )
        }

        composeTestRule.onNodeWithText("MESSAGE")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("MAKE OFFER")
            .assertIsDisplayed()
    }

    @Test
    fun productDetailsScreen_setsTopBarTitle() {

        var appBarState: AppBarState? = null

        composeTestRule.setContent {
            ProductDetailsScreen(
                setTopAppBarState = {
                    appBarState = it
                }
            )
        }

        composeTestRule.runOnIdle {
            assert(appBarState?.title == "Product Details")
        }
    }

}