package com.asphalt.profile.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.profile.screens.sections.ProfileSection

@Composable
fun ProfileScreen() {
    Scaffold() { paddingValues ->
        val topPadding = paddingValues.calculateTopPadding()
        val bottomPadding = paddingValues.calculateBottomPadding()
        ComposeUtils.DefaultColumnRootWithScroll(topPadding, bottomPadding) {
            ProfileSection()
        }
    }
}


@Preview
@Composable
fun ProfilePreview() {
    ProfileScreen()

}