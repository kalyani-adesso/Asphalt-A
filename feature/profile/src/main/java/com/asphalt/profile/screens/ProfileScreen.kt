package com.asphalt.profile.screens

import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.profile.screens.components.EditProfile
import com.asphalt.profile.screens.sections.AchievementsSection
import com.asphalt.profile.screens.sections.ProfileSection
import com.asphalt.profile.screens.sections.TotalStatisticsSection
import com.asphalt.profile.screens.sections.YourVehiclesSection
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    androidUserVM: AndroidUserVM = koinViewModel()
) {
    val user = androidUserVM.userState.collectAsStateWithLifecycle()

    Scaffold() { paddingValues ->
        var showEditProfile by remember { mutableStateOf(false) }
        val topPadding = paddingValues.calculateTopPadding()
        val bottomPadding = paddingValues.calculateBottomPadding()
        ComposeUtils.DefaultColumnRootWithScroll(topPadding, bottomPadding) {

            ProfileSection(user.value)
            YourVehiclesSection()
            TotalStatisticsSection()
            AchievementsSection()
            if (showEditProfile)
                EditProfile(onDismiss = {
                    showEditProfile = false

                })


        }
    }
}


@Preview
@Composable
fun ProfilePreview() {
    ProfileScreen()

}