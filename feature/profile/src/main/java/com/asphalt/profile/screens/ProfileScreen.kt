package com.asphalt.profile.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.profile.screens.sections.AchievementsSection
import com.asphalt.profile.screens.sections.ProfileSection
import com.asphalt.profile.screens.sections.TotalStatisticsSection
import com.asphalt.profile.screens.sections.YourVehiclesSection
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
            YourVehiclesSection()
            TotalStatisticsSection()
            AchievementsSection()

        }
    }
}


@Preview
@Composable
fun ProfilePreview() {
    ProfileScreen()

}