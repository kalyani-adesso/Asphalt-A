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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.AzureBlue
import com.asphalt.commonui.theme.LightGreen30
import com.asphalt.commonui.theme.LightOrange
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryBrighterLightW50
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.theme.VividFlameOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RidersClubSideMenu(drawerState: DrawerState, itemClick:(Int)->Unit,content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
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
                                painter = painterResource(R.drawable.ic_group_blue),
                                contentDescription = "error",
                                tint = LightGreen30
                            )
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        },
                        label = { Text("Connected Ride", style = TypographyMedium.bodyMedium) },
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
                                painter = painterResource(R.drawable.ic_marketplace),
                                contentDescription = "error",
                                tint = VividFlameOrange
                            )
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        },
                        label = { Text("Marketplace", style = TypographyMedium.bodyMedium) },
                        selected = false,
                        onClick = {}
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_messages),
                                contentDescription = "error",
                                tint = AzureBlue
                            )
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        },
                        label = { Text("Messages", style = TypographyMedium.bodyMedium) },
                        selected = false,
                        onClick = {}
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_settings),
                                contentDescription = "error",
                                tint = LightOrange
                            )
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        },
                        label = { Text("Settings", style = TypographyMedium.bodyMedium) },
                        selected = false,
                        onClick = {}
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_send),
                                contentDescription = "error",
                                tint = PrimaryBrighterLightW50
                            )
                        },
                        badge = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        },
                        label = { Text("Refer a Friend", style = TypographyMedium.bodyMedium) },
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
                        label = { Text("Logout", color = Color.Red, style = TypographyMedium.bodyMedium) },
                        selected = false,
                        onClick = {
                            itemClick.invoke(Constants.LOGOUT_CLICK)
                        }
                    )
                }
            }}
        }
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            content()
        }
    }
}}