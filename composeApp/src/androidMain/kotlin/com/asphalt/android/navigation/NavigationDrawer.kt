package com.asphalt.android.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawer(modifier: Modifier = Modifier) {

    var currentKey by rememberSaveable {
        mutableStateOf<androidx.navigation3.runtime.NavKey>(value = NavKey.DashboardNavKey)
    }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerHeader()
                DrawerTextItem(
                    label = "Dashboard") {
                    currentKey = NavKey.DashboardNavKey
                    scope.launch { drawerState.close() }
                }

                DrawerTextItem(
                    label = "Rides") {
                    currentKey = NavKey.RidersKey
                    scope.launch { drawerState.close() }
                }
            }
        }
    ) {
    }

}

@Composable
fun DrawerHeader() {

    Text(
        text = "userName",
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
fun DrawerTextItem(label:String,onClick: ()  -> Unit) {

    ListItem(
        headlineContent = { Text(text = label)},
        modifier = Modifier.padding(horizontal = 8.dp)
            .clickable { onClick()}

    )
}