package com.asphalt.joinaride.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import com.asphalt.commonui.AppBarState
import kotlinx.serialization.Serializable

@Serializable
data object JoinRideKey : NavKey

@Composable
fun JoinRide(
    setTopAppBarState: (AppBarState) -> Unit
) {

    //JoinRideScreen(setTopAppBarState = setTopAppBarState)

}