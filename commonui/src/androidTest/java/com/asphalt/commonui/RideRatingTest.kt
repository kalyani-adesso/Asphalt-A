package com.asphalt.commonui

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class RideRatingTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun showsCorrectNumberOfStars() {
        composeRule.setContent {
            ReadOnlyRatingBar(maxRating = 5)
        }

        (0 until 5).forEach { index ->
            composeRule.onNodeWithTag("star_$index").assertExists()
        }
    }

    @Test
    fun ratingBar_correctFilledState() {
        composeRule.setContent {
            ReadOnlyRatingBar(rating = 4)
        }

        composeRule
            .onNodeWithTag("star_0")
            .assert(
                SemanticsMatcher.expectValue(
                SemanticsProperties.StateDescription, "filled"
            ))

        composeRule
            .onNodeWithTag("star_4")
            .assert(SemanticsMatcher.expectValue(
                SemanticsProperties.StateDescription, "empty"
            ))
    }


}