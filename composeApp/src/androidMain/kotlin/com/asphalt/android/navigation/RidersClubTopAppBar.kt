package com.asphalt.android.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.asphalt.android.data.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralWhite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RidersClubTopAppBar(
    scope: CoroutineScope,
    drawerState: DrawerState,
    topAppBarState: AppBarState,
    onBack: () -> Unit,
    isDashboard: Boolean,

    ) {

    Surface(shadowElevation = Dimensions.size8) {
        if (topAppBarState.isCenterAligned)
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NeutralWhite),
                title = { Text(topAppBarState.title) },
                navigationIcon = {
                    SetNavIcon(scope, drawerState, onBack, isDashboard)
                },
                actions = topAppBarState.actions


            )
        else
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NeutralWhite),
                title = { Text(topAppBarState.title) },
                navigationIcon = {
                    SetNavIcon(scope, drawerState, onBack, isDashboard)

                },
                actions = topAppBarState.actions

            )
    }
}

@Composable
fun SetNavIcon(
    scope: CoroutineScope,
    drawerState: DrawerState,
    onBack: () -> Unit,
    isDashboard: Boolean
) {
    if (isDashboard)
        IconButton(onClick = {
            scope.launch {
                drawerState.open()
            }
        }) {
            Icon(Icons.Default.Menu, contentDescription = "Menu")
        }
    else
        IconButton(onClick = {
            scope.launch {
                onBack.invoke()
            }
        }) {
            Icon(painter = painterResource(R.drawable.ic_arrow_back), contentDescription = "Back")
        }
}