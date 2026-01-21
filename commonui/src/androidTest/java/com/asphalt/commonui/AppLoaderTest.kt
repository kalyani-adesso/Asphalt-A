package com.asphalt.commonui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class AppLoaderTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun appLoader_displaysTitleAndDescription() {
        composeTestRule.setContent {
            AppLoader(
                title = "Loading Data",
                description = "Please wait"
            )
        }

        composeTestRule
            .onNodeWithTag("AppLoaderTitle")
            .assertIsDisplayed()
            .assertTextEquals("Loading Data")

        composeTestRule
            .onNodeWithTag("AppLoaderDescription")
            .assertIsDisplayed()
            .assertTextEquals("Please wait")
    }

    @Test
    fun appLoader_displaysLogo() {
        composeTestRule.setContent {
            AppLoader()
        }

        composeTestRule
            .onNodeWithTag("AppLogo")
            .assertIsDisplayed()
    }

    @Test
    fun appLoader_showsProgressIndicator_whenEnabled() {
        composeTestRule.setContent {
            AppLoader(showProgress = true)
        }

        composeTestRule
            .onNodeWithTag("AppLoaderProgress")
            .assertIsDisplayed()
    }

    @Test
    fun appLoader_hidesProgressIndicator_whenDisabled() {
        composeTestRule.setContent {
            AppLoader(showProgress = false)
        }

        composeTestRule
            .onNodeWithTag("AppLoaderProgress")
            .assertDoesNotExist()
    }

    @Test
    fun appLoader_rootIsDisplayed() {
        composeTestRule.setContent {
            AppLoader()
        }

        composeTestRule
            .onNodeWithTag("AppLoaderRoot")
            .assertIsDisplayed()
    }

}