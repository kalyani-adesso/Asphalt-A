package com.asphalt.profile.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.profile.screens.sections.ProfileSection
import com.asphalt.profile.screens.sections.YourVehiclesSection
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    androidUserVM: AndroidUserVM = koinViewModel()
) {
    val user = androidUserVM.userState.collectAsStateWithLifecycle()

    Scaffold() { paddingValues ->
        val topPadding = paddingValues.calculateTopPadding()
        val bottomPadding = paddingValues.calculateBottomPadding()
        ComposeUtils.DefaultColumnRootWithScroll(topPadding, bottomPadding) {
            ProfileSection(user.value)
            YourVehiclesSection(user.value)

        }
    }
}


@Preview
@Composable
fun ProfilePreview() {
    ProfileScreen()

}