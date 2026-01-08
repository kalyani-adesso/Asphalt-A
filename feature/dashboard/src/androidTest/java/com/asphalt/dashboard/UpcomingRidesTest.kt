package com.asphalt.dashboard

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.asphalt.dashboard.composables.screens.UpcomingRides
import com.asphalt.dashboard.fake.FakeRidesScreenViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UpcomingRidesTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun upcomingRide_displaysTitleAndPlace() {
        val ride = FakeRidesScreenViewModel().fakeUpcomingRide()

        composeRule.setContent {
            UpcomingRides(
                upconing = ride,
                upComingViewDetails = {}
            )
        }

        composeRule.onNodeWithTag("rideTitle")
            .assertIsDisplayed()
            .assertTextEquals("Sunday Morning Ride")

        composeRule.onNodeWithTag("ridePlace")
            .assertIsDisplayed()
            .assertTextEquals("Central Park - Central Tower")

        composeRule.onNodeWithText("UPCOMING")
            .assertIsDisplayed()

        composeRule.onNodeWithText("UPCOMING")
            .assertIsDisplayed()

        composeRule.onNodeWithText("Start: Sat, Jan 31")
            .assertIsDisplayed()

        composeRule.onNodeWithText("End: Sun, Feb 01")
            .assertIsDisplayed()

        composeRule.onNodeWithText("4 Riders")
            .assertIsDisplayed()


    }
    @Test
    fun rideItem_clickViewDetails_invokesCallback() {
        var clickedRideId: String? = null
        val ride = FakeRidesScreenViewModel().fakeUpcomingRide()

        composeRule.setContent {
            UpcomingRides(
                upconing = ride,
                upComingViewDetails = { rideId -> clickedRideId = rideId }
            )
        }

        // Click the "View Details" button
        composeRule.onNodeWithText("VIEW DETAILS")
            .performClick()

        // Assert callback invoked
        assert(clickedRideId == "ride_123")
    }
}