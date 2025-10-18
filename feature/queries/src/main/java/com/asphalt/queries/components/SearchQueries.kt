package com.asphalt.queries.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralGrey30
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.utils.ComposeUtils


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchQueries() {
    var searchText by rememberSaveable { mutableStateOf("") }
    var isActive by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    ComposeUtils.CustomTextField(
        borderColor = if (isActive) NeutralGrey30 else null,
        borderStroke = if (isActive) Dimensions.padding1 else null,
        value =
            searchText,
        onValueChanged = {
            searchText = it
            isActive = true
        },
        placeHolderText =
            "Search Questions",
        backColor = NeutralLightPaper,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                println("Custom Search Submitted: $searchText")
                isActive = false
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                // Clear button
                IconButton(onClick = { searchText = "" }) {
                    Icon(
                        painterResource(R.drawable.ic_close),
                        contentDescription = "Clear search"
                    )
                }
            }
        },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_search_blue),
                contentDescription = null,
                tint = PrimaryDarkerLightB75
            )
        }
    )
}