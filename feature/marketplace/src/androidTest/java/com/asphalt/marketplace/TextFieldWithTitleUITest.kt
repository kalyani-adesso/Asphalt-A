package com.asphalt.marketplace

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.asphalt.marketplace.ui.composable.TextFieldWithTitle
import org.junit.Rule
import org.junit.Test

class TextFieldWithTitleUITest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shows_title_correctly() {
        composeTestRule.setContent {
            TextFieldWithTitle(
                title = "Price",
                value = "",
                onValueChange = {}
            )
        }

        composeTestRule.onNodeWithText("Price").assertExists()
    }

    @Test
    fun shows_placeholder_when_empty() {
        composeTestRule.setContent {
            TextFieldWithTitle(
                title = "Title",
                value = "",
                onValueChange = {}
            )
        }

        composeTestRule
            .onNodeWithText("eg: Yamaha - R15 2020 ")
            .assertExists()
    }

    @Test
    fun accepts_user_input() {
        var text = ""

        composeTestRule.setContent {
            TextFieldWithTitle(
                title = "Title",
                value = text,
                onValueChange = { text = it }
            )
        }

        composeTestRule
            .onNodeWithTag("rideTitleInput")
            .performTextInput("Bike")

        assert(text == "Bike")
    }
    @Test
    fun supports_multiline_input() {
        composeTestRule.setContent {
            TextFieldWithTitle(
                title = "Description",
                maxLines = 3,
                value = "",
                onValueChange = {}
            )
        }

        composeTestRule.onNodeWithText("Description").assertExists()
    }
}