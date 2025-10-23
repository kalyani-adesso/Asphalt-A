package com.asphalt.profile.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.profile.screens.components.EditProfile
import com.asphalt.profile.screens.sections.AchievementsSection
import com.asphalt.profile.screens.sections.ProfileSection
import com.asphalt.profile.screens.sections.TotalStatisticsSection
import com.asphalt.profile.screens.sections.YourVehiclesSection
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    androidUserVM: AndroidUserVM = koinViewModel(),
    setTopAppBarState: (AppBarState) -> Unit,

    ) {
    val user = androidUserVM.userState.collectAsStateWithLifecycle()
    var showEditProfile by remember { mutableStateOf(false) }

    setTopAppBarState(AppBarState(stringResource(R.string.profile), actions = {
        ComposeUtils.ColorIconRounded(
            backColor = PrimaryDarkerLightB75,
            resId = R.drawable.ic_edit,
            modifier = Modifier
                .padding(end = Dimensions.padding15)
                .clickable {
                    showEditProfile = true
                })
    }))

    val topPadding = Dimensions.size0
    val bottomPadding = Dimensions.size0
    ComposeUtils.DefaultColumnRoot(topPadding, bottomPadding) {
        Spacer(Modifier.height(Dimensions.size20))
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


@Preview
@Composable
fun ProfilePreview() {
    ProfileScreen(setTopAppBarState = {})

}