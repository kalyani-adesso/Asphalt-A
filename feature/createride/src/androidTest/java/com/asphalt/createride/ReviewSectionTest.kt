package com.asphalt.createride

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.asphalt.android.di.androidSharedModule
import com.asphalt.android.di.sharedModule
import com.asphalt.createride.di.createRideModule
import com.asphalt.createride.fake.FakeCreateRideViewModel
import com.asphalt.createride.ui.composables.DetailsSection
import com.asphalt.createride.ui.composables.ReviewSection
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
class ReviewSectionTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        startKoin {
            modules(
                modules =
                    sharedModule + androidSharedModule
                            + createRideModule,
            ) // must provide UserRepository, RidesRepository, etc.
        }
        val fakeViewModel = FakeCreateRideViewModel()
        composeTestRule.setContent {
            ReviewSection(viewModel = fakeViewModel)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun reviewSectionLabel_isDisplayed() {
        composeTestRule.onNodeWithText("Review Your Ride").assertIsDisplayed()
        composeTestRule.onNodeWithText("Date and Time").assertIsDisplayed()
        composeTestRule.onNodeWithText("Route").assertIsDisplayed()
        composeTestRule.onNodeWithText("Participants").assertIsDisplayed()

    }

    @Test
    fun rideTitle_isCorrect() {
        composeTestRule
            .onNodeWithTag("ride_title")
            .assertTextEquals("Sunday Morning Ride")
    }

    @Test
    fun rideDestination_isCorrect(){
        composeTestRule.onNodeWithTag("ride_destination").assertTextEquals("Refreshment to Beach")
    }

    @Test
    fun ride_participant_count_isCorrect(){
        composeTestRule.onNodeWithTag("participant_count").assertTextEquals("2 riders selected")
    }

    @Test
    fun rideType_isCorrect(){
        composeTestRule.onNodeWithTag("ride_type").assertTextEquals("Group Ride")
    }

    @Test
    fun startDate_isDisplayed(){
        composeTestRule.onNodeWithTag("date_time").assertTextEquals("Dec 31,2025 - 9:51 AM")

    }
    @Test
    fun start_and_end_location_isDisplayed(){
        composeTestRule.onNodeWithTag("route").assertTextEquals("Downtown - Beach")

    }

}