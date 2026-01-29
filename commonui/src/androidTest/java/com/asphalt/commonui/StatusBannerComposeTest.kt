package com.asphalt.commonui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StatusBannerComposeTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun banner_isVisible_whenShowBannerTrue() {
        composeRule.setContent {
            StatusBanner(
                type = BannerType.SUCCESS,
                message = "Success message",
                showBanner = true,
                onDismiss = {}
            )
        }

        composeRule
            .onNodeWithText("Success message")
            .assertIsDisplayed()
    }

    @Test
    fun banner_isNotVisible_whenShowBannerFalse() {
        composeRule.setContent {
            StatusBanner(
                type = BannerType.SUCCESS,
                message = "Hidden message",
                showBanner = false,
                onDismiss = {}
            )
        }

        composeRule
            .onNodeWithText("Hidden message")
            .assertDoesNotExist()
    }

    @Test
    fun errorBanner_displaysErrorMessage() {
        composeRule.setContent {
            StatusBanner(
                type = BannerType.ERROR,
                message = "Something went wrong",
                showBanner = true,
                onDismiss = {}
            )
        }

        composeRule
            .onNodeWithText("Something went wrong")
            .assertIsDisplayed()
    }

    @Test
    fun banner_autoDismisses_afterTimeout() {
        composeRule.mainClock.autoAdvance = false

        var showBanner by mutableStateOf(true)

        composeRule.setContent {
            StatusBanner(
                message = "Auto dismiss",
                showBanner = showBanner,
                autoDismissMillis = 1_000,
                onDismiss = { showBanner = false }
            )
        }

        composeRule
            .onNodeWithText("Auto dismiss")
            .assertIsDisplayed()

        // autoDismiss + fadeOut duration
        composeRule.mainClock.advanceTimeBy(1_400)

        composeRule.waitForIdle()

        composeRule
            .onNodeWithText("Auto dismiss")
            .assertDoesNotExist()
    }

}