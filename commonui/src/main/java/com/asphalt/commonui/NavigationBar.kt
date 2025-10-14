package com.asphalt.commonui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    title: String = "",
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium

                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        active = false,
                        activeContent = {},
                        inactiveContent = {
                            Image(
                                painter = painterResource(id = R.drawable.ic_back_arrow),
                                contentDescription = "")
                        }
                    )
                }
            }
        )
    }
}

@Preview()
@Composable
private fun NavigationBarPreview() {
    MaterialTheme {
       NavigationBar(
            title = "Dashboard",
            onBackPressed = {}
        )
    }
}