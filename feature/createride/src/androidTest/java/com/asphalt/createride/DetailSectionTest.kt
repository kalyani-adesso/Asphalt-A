package com.asphalt.createride

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.asphalt.createride.fake.FakeCreateRideViewModel
import com.asphalt.createride.ui.composables.DetailsSection
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailSectionTest {
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
    fun rideTitleLabel_isDisplayed(){
        val fakeViewModel = FakeCreateRideViewModel()
        composeTestRule.setContent {
            DetailsSection(fakeViewModel)
        }
        composeTestRule.onNodeWithText("Ride Title").assertIsDisplayed()
    }

    @Test
    fun descriptionLabel_isDisplayed(){
        val fakeViewModel = FakeCreateRideViewModel()
        composeTestRule.setContent {
            DetailsSection(fakeViewModel)
        }
        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
    }

    @Test
    fun startDateLabel_isDisplayed(){
        val fakeViewModel = FakeCreateRideViewModel()
        composeTestRule.setContent {
            DetailsSection(fakeViewModel)
        }
        composeTestRule.onNodeWithText("Start Date").assertIsDisplayed()
    }

    @Test
    fun endDateLabel_isDisplayed(){
        val fakeViewModel = FakeCreateRideViewModel()
        composeTestRule.setContent {
            DetailsSection(fakeViewModel)
        }
        composeTestRule.onNodeWithText("End Date").assertIsDisplayed()
    }



    @Test
    fun timeLabel_isDisplayed(){
        val fakeViewModel = FakeCreateRideViewModel()
        composeTestRule.setContent {
            DetailsSection(fakeViewModel)
        }
        composeTestRule.onAllNodesWithText("Time").assertCountEquals(2)
    }

}