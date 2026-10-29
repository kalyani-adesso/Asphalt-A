package com.asphalt.marketplace

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.asphalt.marketplace.ui.composable.InputAndDropDown
import org.junit.Rule
import org.junit.Test

class InputAndDropDownUITest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shows_both_titles() {
        composeTestRule.setContent {
            InputAndDropDown(
                title1 = "Brand",
                title2 = "Condition",
                input1 = {},
                input2 = {}
            )
        }

        composeTestRule.onNodeWithText("Brand").assertExists()
        composeTestRule.onNodeWithText("Condition").assertExists()
    }

    @Test
    fun input_field_accepts_text() {
        var text = ""

        composeTestRule.setContent {
            InputAndDropDown(
                title1 = "Brand",
                title2 = "Condition",
                input1 = { text = it },
                input2 = {}
            )
        }

        composeTestRule
            .onNodeWithTag("rideTitleInput")
            .performTextInput("Yamaha")

        assert(text == "Yamaha")
    }

    @Test
    fun dropdown_default_value_visible() {
        composeTestRule.setContent {
            InputAndDropDown(
                title1 = "Brand",
                title2 = "Condition",
                input1 = {},
                input2 = {}
            )
        }

        composeTestRule.onNodeWithText("Used").assertExists()
    }
    @Test
    fun dropdown_clickable() {
        composeTestRule.setContent {
            InputAndDropDown(
                title1 = "Brand",
                title2 = "Condition",
                input1 = {},
                input2 = {}
            )
        }

        composeTestRule
            .onNodeWithText("Used")
            .performClick()
    }
}