package com.asphalt.dashboard.composables.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.AsphaltTheme
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

@Composable
fun NotificationScreen(viewModel: NotificationViewModel = viewModel()) {
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
            .padding(start = Dimensions.padding16, end = Dimensions.padding16),
        cornerRadius = Dimensions.size10,
        backgroundColor = if (notificationItem.isRead) GrayLight15 else NeutralLightPaper
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
                    backColor = VividOrange,
                    resId = R.drawable.ic_two_wheeler
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
    NotificationScreen()
}