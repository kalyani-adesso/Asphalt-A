package com.asphalt.android.navigation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHost
import androidx.navigation.compose.rememberNavController
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.NeutralMidGrey
import com.asphalt.dashboard.composables.screens.DashBoardScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer() {

    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    ModalNavigationDrawer (
        drawerState = drawerState,
        drawerContent = {

            ModalDrawerSheet{
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(14.dp)
                ) {
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_connected_rides),
                                contentDescription = "error",
                                tint = Color.Black)
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        },
                        label = {Text("Connected Rides")},
                        selected = false,
                        onClick = {}
                    )

                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_knowledge),
                                contentDescription = "error",
                                tint = Color.Black)
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        },
                        label = {Text("Knowledge Circle")},
                        selected = false,
                        onClick = {}
                    )

                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_market),
                                contentDescription = "error",
                                tint = Color.Black)
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        },
                        label = {Text("Marketplace")},
                        selected = false,
                        onClick = {}
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_settings),
                                contentDescription = "error",
                                tint = Color.Black)
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        },
                        label = {Text("Settings")},
                        selected = false,
                        onClick = {}
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_send),
                                contentDescription = "error",
                                tint = Color.Black)
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        },
                        label = {Text("Refer a Friend")},
                        selected = false,
                        onClick = {}
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(16.dp),
                        thickness = 0.6.dp,
                        color = Color.Gray
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_logout),
                                contentDescription = "error",
                                tint = Color.Red)
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Red
                            )
                        },
                        label = {Text("Logout", color = Color.Red)},
                        selected = false,
                        onClick = {}
                    )
                }
            }

        },
        content = {
//            Scaffold(
//                topBar = {
//                    TopAppBar(
//                        title = {Text("Kalyani")},
//                        navigationIcon = {
//                            IconButton(onClick = {
//                                scope.launch { drawerState.open() }
//                            }) {
//                                Icon(Icons.Default.Menu, contentDescription = "menu")
//                            }
//                        }
//                    )
//                }
//            ) { innerPadding ->
//                Column(modifier = Modifier.padding(innerPadding)) {
//                    Log.d("TAG", "NavigationDrawer: clicked")
//                }
//            }
        }
    )
}