package com.asphalt.commonui

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class NavigationBarTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun navigationBar_displaysTitle() {
        val title = "Dashboard"

        composeTestRule.setContent {
            MaterialTheme {
                NavigationBar(title = title)
            }
        }

        composeTestRule
            .onNodeWithText(title)
            .assertIsDisplayed()
    }

    @Test
    fun navigationBar_displaysBackButton() {
        composeTestRule.setContent {
            MaterialTheme {
                NavigationBar()
            }
        }

        composeTestRule
            .onNodeWithTag("BackButton")
            .assertIsDisplayed()
    }

    @Test
    fun navigationBar_backButtonClick_triggersCallback() {
        var backPressed = false

        composeTestRule.setContent {
            MaterialTheme {
                NavigationBar(
                    onBackPressed = { backPressed = true }
                )
            }
        }

        composeTestRule
            .onNodeWithTag("BackButton")
            .performClick()

        assertTrue(backPressed)
    }
}