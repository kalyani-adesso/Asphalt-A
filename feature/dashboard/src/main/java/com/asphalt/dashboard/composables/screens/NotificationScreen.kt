package com.asphalt.dashboard.composables.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.commonui.theme.BrightTeal
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GrayLight15
import com.asphalt.commonui.theme.NeutralBlackGrey
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.VividOrange
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.ComposeUtils.ColorIconRounded
import com.asphalt.dashboard.data.NotificationData
import com.asphalt.dashboard.viewmodels.NotificationViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun NotificationScreen(
    setTopAppBarState: (AppBarState) -> Unit,
    viewModel: NotificationViewModel = koinActivityViewModel()
) {
    setTopAppBarState(AppBarState(title = stringResource(R.string.notifications)))
    val notificationList = viewModel.notificationState.collectAsState()
    AsphaltTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(NeutralWhite)
                .padding(top = Dimensions.size30)
        ) {
            items(notificationList.value) { notificationItem ->
                NotificationItem(notificationItem, viewModel)
                Spacer(Modifier.height(Dimensions.spacing15))
            }
        }

    }

}

@Composable
fun NotificationItem(
    notificationItem: NotificationData,
    viewModel: NotificationViewModel
) {
    RoundedBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = Dimensions.padding16, end = Dimensions.padding16)
            .clickable {
                viewModel.updateReadStatus(notificationItem.id)
            },
        cornerRadius = Dimensions.size10,
        backgroundColor = if (notificationItem.isRead) NeutralLightPaper else GrayLight15
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = Dimensions.padding15,
                    vertical = Dimensions.size22
                )
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    Dimensions.spacing10
                )
            ) {
                ColorIconRounded(
                    backColor = if (notificationItem.notificationType == Constants.RIDE_REMINDER)
                        VividOrange
                    else
                        BrightTeal,
                    resId = if (notificationItem.notificationType == Constants.RIDE_REMINDER)
                        R.drawable.ic_two_wheeler
                    else
                        R.drawable.ic_group_icon_plus
                )
                Column(verticalArrangement = Arrangement.spacedBy(Dimensions.size3)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            notificationItem.title ?: "",
                            style = TypographyBold.bodyMedium,
                            color = NeutralBlackGrey
                        )
                        Text(
                            text = notificationItem.date ?: "", style = Typography.titleSmall,
                            fontSize = Dimensions.textSize12,
                            color = NeutralDarkGrey
                        )
                    }

                    Text(
                        notificationItem.description ?: "",
                        style = Typography.titleSmall,
                        fontSize = Dimensions.textSize12,
                        color = NeutralDarkGrey
                    )
                }
            }


        }
    }
}

@Preview
@Composable
fun NotificationPreview() {
    NotificationScreen({})
}