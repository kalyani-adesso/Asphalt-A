package com.asphalt.android.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RidersClubSideMenu(drawerState: DrawerState, itemClick:(Int)->Unit,content: @Composable () -> Unit) {

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = NeutralWhite) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(NeutralLightPaper)
                        .padding(14.dp)
                ) {
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_connected_rides),
                                contentDescription = "error",
                                tint = Color.Black
                            )
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        },
                        label = { Text("Connected Rides") },
                        selected = false,
                        onClick = {}
                    )
//
//                    NavigationDrawerItem(
//                        icon = {
//                            Icon(
//                                painter = painterResource(R.drawable.ic_knowledge),
//                                contentDescription = "error",
//                                tint = Color.Black
//                            )
//                        },
//                        badge = {
//                            Icon(
//                                painter = painterResource(R.drawable.ic_arrow),
//                                contentDescription = "",
//                                tint = Color.Black
//                            )
//                        },
//                        label = { Text("Knowledge Circle") },
//                        selected = false,
//                        onClick = {}
//                    )

                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_market),
                                contentDescription = "error",
                                tint = Color.Black
                            )
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        },
                        label = { Text("Marketplace") },
                        selected = false,
                        onClick = {}
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_settings),
                                contentDescription = "error",
                                tint = Color.Black
                            )
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        },
                        label = { Text("Settings") },
                        selected = false,
                        onClick = {}
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_send),
                                contentDescription = "error",
                                tint = Color.Black
                            )
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        },
                        label = { Text("Refer a Friend") },
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
                                tint = Color.Red
                            )
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Red
                            )
                        },
                        label = { Text("Logout", color = Color.Red) },
                        selected = false,
                        onClick = {
                            itemClick.invoke(Constants.LOGOUT_CLICK)
                        }
                    )
                }
            }
        }
    ) {
        content()
    }
}