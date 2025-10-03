package com.asphalt.dashboard.composables.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.ui.DonutPreview
import com.asphalt.commonui.utils.Constants
import com.asphalt.dashboard.composables.screens.sections.CreateOrJoinRide
import com.asphalt.dashboard.composables.screens.sections.RideStatsPreview
import com.asphalt.dashboard.composables.screens.sections.UpcomingRide

@Composable
fun DashBoardScreen() {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = Constants.DEFAULT_SCREEN_HORIZONTAL_PADDING,
                    end = Constants.DEFAULT_SCREEN_HORIZONTAL_PADDING
                )
                .verticalScroll(rememberScrollState())
        ) {
            CreateOrJoinRide({}, {})
            Spacer(Modifier.height(Dimensions.spacing20))
            RideStatsPreview()
            Spacer(Modifier.height(Dimensions.spacing20))
            UpcomingRide()
            DonutPreview()
        }

    }
}