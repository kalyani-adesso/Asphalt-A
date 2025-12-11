package com.asphalt.createride

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.asphalt.android.di.androidSharedModule
import com.asphalt.android.di.sharedModule
import com.asphalt.createride.di.createRideModule
import com.asphalt.createride.fake.FakeCreateRideViewModel
import com.asphalt.createride.ui.CreateRideScreen
import com.asphalt.createride.ui.composables.DetailsSection
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
class DetailSectionTest {
    @Before
    fun setup() {
        startKoin {
            modules(
                modules =
                    sharedModule + androidSharedModule
                            + createRideModule,
            ) // must provide UserRepository, RidesRepository, etc.
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun rideTypeLabel_isDisplayed() {
        val fakeViewModel = FakeCreateRideViewModel()
        composeTestRule.setContent {
            DetailsSection(fakeViewModel)
        }
        composeTestRule.onNodeWithText("Ride Type").assertIsDisplayed()

    }

    @Test
    fun rideTitleLabel_isDisplayed() {
        val fakeViewModel = FakeCreateRideViewModel()
        composeTestRule.setContent {
            DetailsSection(fakeViewModel)
        }
        composeTestRule.onNodeWithText("Ride Title").assertIsDisplayed()
    }

    @Test
    fun descriptionLabel_isDisplayed() {
        val fakeViewModel = FakeCreateRideViewModel()
        composeTestRule.setContent {
            DetailsSection(fakeViewModel)
        }
        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
    }

    @Test
    fun startDateLabel_isDisplayed() {
        val fakeViewModel = FakeCreateRideViewModel()
        composeTestRule.setContent {
            DetailsSection(fakeViewModel)
        }
        composeTestRule.onNodeWithText("Start Date").assertIsDisplayed()
    }

    @Test
    fun endDateLabel_isDisplayed() {
        val fakeViewModel = FakeCreateRideViewModel()
        composeTestRule.setContent {
            DetailsSection(fakeViewModel)
        }
        composeTestRule.onNodeWithText("End Date").assertIsDisplayed()
    }


    @Test
    fun timeLabel_isDisplayed() {
        val fakeViewModel = FakeCreateRideViewModel()
        composeTestRule.setContent {
            DetailsSection(fakeViewModel)
        }
        composeTestRule.onAllNodesWithText("Time").assertCountEquals(2)
    }

    @Test
    fun clickingRideType_opensDropdown() {

        val fakeViewModel = FakeCreateRideViewModel()

        composeTestRule.setContent {
            DetailsSection(fakeViewModel)
        }

        // Click the ride type box
        composeTestRule.onNodeWithTag("Ride_Type").performClick()

        // Dropdown items must appear
        composeTestRule.onNodeWithText("Solo Ride").assertIsDisplayed()
        composeTestRule.onNodeWithText("Group Ride").assertIsDisplayed()
    }

    @Test
    fun selectSoloRide_isShownInTextView() {

        val fakeViewModel = FakeCreateRideViewModel()

        composeTestRule.setContent {
            DetailsSection(fakeViewModel)
        }

        // Click the ride type box
        composeTestRule.onNodeWithTag("Ride_Type").performClick()

        // Dropdown items must appear
        composeTestRule.onNodeWithText("Solo Ride").assertIsDisplayed()
        composeTestRule.onNodeWithText("Group Ride").assertIsDisplayed()

        composeTestRule.onNodeWithText("Solo Ride").performClick()
        composeTestRule.onNodeWithText("Solo Ride").assertIsDisplayed()

    }
    
}


