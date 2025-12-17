package com.asphalt.chat.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asphalt.chat.model.ChatTabModel
import com.asphalt.chat.viewmodel.ChatListViewModel
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.util.GetGradient
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatListingScreen(
    setTopAppBarState: (AppBarState) -> Unit,
    viewModel: ChatListViewModel = koinViewModel()
) {
    setTopAppBarState(
        AppBarState(
            title = stringResource(R.string.messages),
        )
    )
    AsphaltTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NeutralWhite)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.padding50)

                    .padding(start = Dimensions.padding16, end = Dimensions.padding16)
                    .background(
                        NeutralLightPaper, shape = RoundedCornerShape(Dimensions.padding10)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = "",
                    onValueChange = { },
                    placeholder = {
                        Text(
                            text = "Search riders, conversations...",//stringResource(R.string._search_name_number),
                            style = Typography.bodyMedium,
                            color = NeutralDarkGrey,


                            )
                    },
                    textStyle = Typography.bodyMedium.copy(NeutralDarkGrey),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),

                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,

                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,

                        ),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_search_blue),
                            contentDescription = "Email icon",
                            tint = Color.Unspecified

                        )
                    }

                )

            }
            Spacer(Modifier.height(Dimensions.size20))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.size92)
                    .padding(start = Dimensions.padding16)
                    .background(
                        NeutralLightPaper, shape = RoundedCornerShape(Dimensions.padding10)
                    )

            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    contentPadding = PaddingValues(start = Dimensions.padding10)
                ) {
                    items(ChatTabModel.getChatTabs()) { item ->
                        Box(
                            modifier = Modifier
                                .then(
                                    if (viewModel.tabSelection.value == item.id) {
                                        Modifier.background(
                                            brush = GetGradient(
                                                PrimaryDarkerLightB75,
                                                PrimaryDarkerLightB75
                                            ),
                                            shape = RoundedCornerShape(Dimensions.size10)
                                        )
                                    } else {
                                        Modifier.background(
                                            color = NeutralWhite,
                                            shape = RoundedCornerShape(Dimensions.size10)
                                        )
                                    }
                                )
                                .clickable {
                                    viewModel.updateTab(item.id)
                                }.padding(all = Dimensions.padding15), contentAlignment = Alignment.Center

                            // Rounded corners here

                        ) {
                            Text(
                                text = stringResource(item.name),//stringResource(R.string.upcoming),
                                style = TypographyMedium.titleMedium,
                                color = if (viewModel.tabSelection.value == item.id) {
                                    NeutralWhite
                                } else {
                                    NeutralBlack
                                }
                            )
                        }
                        Spacer(modifier = Modifier.width(Dimensions.padding10))
                    }
                }
            }
        }
    }

}

@Preview
@Composable
fun ChatListPreview() {
    var videModel: ChatListViewModel = viewModel()
    ChatListingScreen({}, videModel)
}