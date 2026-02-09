package com.asphalt.dashboard

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.asphalt.android.di.androidSharedModule
import com.asphalt.android.di.sharedModule
import com.asphalt.chat.di.chatModule
import com.asphalt.dashboard.composables.screens.RidesDetailsScreen
import com.asphalt.dashboard.di.dashboardModule
import com.asphalt.dashboard.fake.FakeRidesDetailsViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
class RidesDetailsScreenTest {

    @get:Rule
    val composeRule = createComposeRule()
    private lateinit var viewModel: FakeRidesDetailsViewModel

    @Before
    fun setup() {
        startKoin {

            modules(
                modules =
                    sharedModule + dashboardModule + androidSharedModule
                            + chatModule
            )
        }

        viewModel = FakeRidesDetailsViewModel().apply {
            setRide()
            setRiders()
            setDeleteVisible(true)
        }

        composeRule.setContent {
            RidesDetailsScreen(
                rideId = "ride_1",
                setTopAppBarState = {},
                viewModel = viewModel,
                onBack = {}
            )
        }
    }


    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun rideTitle_isDisplayed() {
        composeRule
            .onNodeWithText("Morning Ride")
            .assertIsDisplayed()
    }

    @Test
    fun startAndEndLocations_areDisplayed() {
        composeRule.onNodeWithText("New York - Boston").assertIsDisplayed()
    }
    @Test
    fun organizer_badge_isDisplayed() {
        composeRule.onNodeWithText("Organizer").assertIsDisplayed()
    }
    @Test
    fun deleteButton_isVisible_forOrganizer() {
        composeRule.onNodeWithText("DELETE RIDE").assertIsDisplayed()
    }

    @Test
    fun deleteButton_isHidden_forNonOrganizer() {
        viewModel.setDeleteVisible(false)
        composeRule.waitForIdle()

        composeRule.onNodeWithText("DELETE RIDE").assertDoesNotExist()
    }

}