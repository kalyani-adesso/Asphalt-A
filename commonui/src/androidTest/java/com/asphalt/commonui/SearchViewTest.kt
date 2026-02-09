package com.asphalt.commonui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class SearchViewTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchView_displaysTextField() {
        composeTestRule.setContent {
            SearchView(
                query = "",
                onQueryChange = {},
                onClearClick = {}
            )
        }

        composeTestRule
            .onNodeWithTag("SEARCH_TEXT_FIELD")
            .assertIsDisplayed()
    }

    @Test
    fun searchView_updatesQueryOnTextInput() {
        val queryState = mutableStateOf("")

        composeTestRule.setContent {
            SearchView(
                query = queryState.value,
                onQueryChange = { queryState.value = it },
                onClearClick = {}
            )
        }

        composeTestRule
            .onNodeWithTag("SEARCH_TEXT_FIELD")
            .performTextInput("Hello")

        assertEquals("Hello", queryState.value)
    }

    @Test
    fun clearIcon_visible_whenQueryNotEmpty() {
        composeTestRule.setContent {
            SearchView(
                query = "Test",
                onQueryChange = {},
                onClearClick = {}
            )
        }

        composeTestRule
            .onNodeWithTag("CLEAR_ICON")
            .assertIsDisplayed()
    }
    @Test
    fun clearIcon_notVisible_whenQueryEmpty() {
        composeTestRule.setContent {
            SearchView(
                query = "",
                onQueryChange = {},
                onClearClick = {}
            )
        }

        composeTestRule
            .onNodeWithTag("CLEAR_ICON")
            .assertDoesNotExist()
    }
    @Test
    fun clearIcon_click_clearsQuery() {
        val queryState = mutableStateOf("Search")

        composeTestRule.setContent {
            SearchView(
                query = queryState.value,
                onQueryChange = { queryState.value = it },
                onClearClick = { queryState.value = "" }
            )
        }

        composeTestRule
            .onNodeWithTag("CLEAR_ICON")
            .performClick()

        assertEquals("", queryState.value)
    }

}