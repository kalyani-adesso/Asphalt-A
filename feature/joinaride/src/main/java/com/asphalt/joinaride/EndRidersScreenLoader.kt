package com.asphalt.joinaride

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.AppLoader
import com.asphalt.commonui.R
import kotlinx.coroutines.delay

@Composable
fun EndRidersScreenLoader(
    setTopAppBarState: (AppBarState) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToSummaryEndRide : () -> Unit) {

    var isLoading by remember { mutableStateOf(true) }
    val loadingTitle = "Completing Ride"
    val loadingDesc = "Saving your ride and generating summary"
    val logoRes = R.drawable.ic_app_icon

    setTopAppBarState(
        AppBarState(title = stringResource(R.string.connected_ride)
        )
    )

    LaunchedEffect(Unit) {
        delay(3000)
        isLoading = false
        delay(1000)
        onNavigateToSummaryEndRide.invoke()
    }

    Box(
        modifier = modifier.fillMaxSize()) {
        if (isLoading) {
            AppLoader(
                logoRes = logoRes,
                title = loadingTitle,
                description = loadingDesc,
                showProgress = true
            )
        }
    }
}