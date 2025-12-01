package com.asphalt.android.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryBrighterLightB33
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.VividRed
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.dashboard.viewmodels.NotificationViewModel
import com.asphalt.profile.screens.sections.ProfileLabel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinActivityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RidersClubTopAppBar(
    scope: CoroutineScope,
    drawerState: DrawerState,
    topAppBarState: AppBarState,
    onBack: () -> Unit,
    isDashboard: Boolean,
    notificationsClick: () -> Unit,
    notificationViewModel: NotificationViewModel = koinActivityViewModel(),


    ) {
    val notificationList = notificationViewModel.notificationState.collectAsStateWithLifecycle()
    val hasUnreadNotifications: State<Boolean> = remember {
        derivedStateOf {
            notificationList.value.any {
                !it.isRead
            }
        }
    }

    Surface {
        Column(
            modifier = Modifier
                .background(NeutralWhite)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(
                Dimensions.size20
            )
        ) {

            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NeutralWhite),
                title = { },
                navigationIcon = {
                    SetNavIcon(scope, onBack, isDashboard)

                },
                actions = {



                    IconButton(onClick = {
                        notificationsClick.invoke()
                    }) {
                        Box {
                            Icon(
                                painter = painterResource(R.drawable.ic_notification),
                                null,
                                tint = PrimaryDarkerLightB75
                            )
                            if (hasUnreadNotifications.value)
                                RoundedBox(
                                    backgroundColor = VividRed,
                                    modifier = Modifier
                                        .size(Dimensions.size8)
                                        .align(Alignment.TopEnd)
                                        .offset(x = Dimensions.spacingNeg1),
                                    cornerRadius = Dimensions.size8
                                ) {}
                        }
                    }
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = PrimaryDarkerLightB75
                        )

                    }

                }


            )
            Row(

                modifier = if (!isDashboard) Modifier
                    .fillMaxWidth()
                    .padding(
                        start = Constants.DEFAULT_SCREEN_HORIZONTAL_PADDING,
                        bottom = Dimensions.size20
                    ) else Modifier.fillMaxWidth().padding(bottom = Dimensions.size25),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CreateHeader(isDashboard, topAppBarState)
                Spacer(modifier = Modifier.weight(1f))
                val actions = topAppBarState.actions
                actions()
            }
        }
    }
}

@Composable
fun SetNavIcon(
    scope: CoroutineScope,
    onBack: () -> Unit,
    isDashboard: Boolean
) {
    if (!isDashboard)

        IconButton(onClick = {
            scope.launch {
                onBack.invoke()
            }
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = "Back",
                tint = PrimaryDarkerLightB75
            )
        }
}

@Composable
fun CreateHeader(isDashboard: Boolean, topAppBarState: AppBarState) {
    if (isDashboard){
        Box {
            val dashboardHeaderBlock = topAppBarState.dashboardHeader
            dashboardHeaderBlock()
        }

    }else


    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            Dimensions.padding2
        )
    ) {
        Text(
            topAppBarState.title,
            style = TypographyBold.bodyMedium,
            fontSize = Dimensions.textSize19,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (topAppBarState.subtitle.isNotEmpty())
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    Dimensions.spacing5
                )
            ) {
                // Use this if location is required to be shown in dashboard screen
//                if (isDashboard) {
//
//                    RoundedBox(
//                        modifier = Modifier.size(Dimensions.size4),
//                        backgroundColor = ShamrockGreen,
//                        cornerRadius = Dimensions.size4
//                    ) { }
//                }
                Text(
                    topAppBarState.subtitle,
                    style = Typography.bodyMedium,
                    fontSize = Dimensions.textSize14,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

    }


}