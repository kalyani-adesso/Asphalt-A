package com.asphalt.chat.screen

import android.annotation.SuppressLint
import android.icu.text.ListFormatter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.android.datastore.DataStoreManager
import com.asphalt.android.model.chat.ChatRoom
import com.asphalt.android.model.chat.getOtherUserId
import com.asphalt.android.model.chat.memberIds
import com.asphalt.android.network.KtorClient
import com.asphalt.android.network.user.UserAPIServiceImpl
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.repository.chat.ChatRepository
import com.asphalt.android.repository.user.UserRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.chat.model.ChatParamsModel
import com.asphalt.chat.model.ChatTabModel
import com.asphalt.chat.viewmodel.ChatListViewModel
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GreenLIGHT
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.theme.VividRed
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.commonui.util.GetGradient
import com.asphalt.commonui.utils.Utils
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatListingScreen(
    setTopAppBarState: (AppBarState) -> Unit,
    chatItemClick: (List<String>) -> Unit, groupChatClick: (ChatParamsModel) -> Unit,
    viewModel: ChatListViewModel = koinViewModel(),
    androidUserVM: AndroidUserVM = koinViewModel()
) {
    val chatList by viewModel.chatModel.collectAsStateWithLifecycle()
    val text by viewModel.searchQuery.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getChatList()
    }

    val selectedTab by viewModel.tabSelection.collectAsState()

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
                    value = text,
                    onValueChange = { viewModel.updateSearchQuery(it) },
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
                                }
                                .padding(all = Dimensions.padding15),
                            contentAlignment = Alignment.Center

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
            Spacer(modifier = Modifier.height(Dimensions.padding20))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f)
                    .padding(horizontal = Dimensions.padding16)
                    .background(
                        color = NeutralLightPaper, shape = RoundedCornerShape(Dimensions.size10)
                    )
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        Spacer(modifier = Modifier.height(Dimensions.padding16))
                    }
                    items(chatList) { chatRoom ->
                        Column {
                            ChatList(
                                chatItemClick,
                                chatRoom,
                                viewModel,
                                androidUserVM,
                                groupChatClick
                            )
                            Spacer(modifier = Modifier.height(Dimensions.size10))

                        }
                    }

                }
            }
        }
    }

}

@Composable
fun ChatList(
    chatItemClick: (List<String>) -> Unit,
    chatRoom: ChatRoom,
    viewModel: ChatListViewModel,
    androidUserVM: AndroidUserVM,
    groupChatClick: (ChatParamsModel) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimensions.size71)// size83
            .padding(start = Dimensions.padding16, end = Dimensions.padding16)
            .clickable {
                if (chatRoom.type == Constants.GROUP_CHAT) {
                    groupChatClick.invoke(
                        ChatParamsModel(
                            chatRoom.id,
                            chatRoom.memberIds(),
                            chatRoom.name ?: ""
                        )
                    )
                } else {
                    chatItemClick.invoke(
                        listOf(
                            chatRoom.getOtherUserId(androidUserVM.getCurrentUserUID()) ?: ""
                        )
                    )
                }

            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White // or use NeutralWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(
                    start = Dimensions.padding16, end = Dimensions.padding16,
                    top = Dimensions.padding16
                ),
            //verticalArrangement = Arrangement.Center
        ) {

            Row(
            ) {
                Box(modifier = Modifier.size(Dimensions.padding40)) {

                    CircularNetworkImage(
                        modifier = Modifier.border(
                            width = Dimensions.size2pt5,
                            color =
                                GreenLIGHT,
                            shape = CircleShape
                        ),
                        size = Dimensions.padding40,
                        imageUrl = if (chatRoom.type.equals(Constants.GROUP_CHAT)) {
                            ""
                        } else {
                            androidUserVM.getUser(
                                chatRoom.getOtherUserId(androidUserVM.getCurrentUserUID())
                                    ?: ""
                            )?.profilePic ?: ""
                        }
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_online_icon),
                        contentDescription = "Online Status",
                        modifier = Modifier
                            .size(Dimensions.size14)
                            .align(Alignment.BottomEnd)
                    )
                }
                Spacer(Modifier.width(Dimensions.size5))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier

                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = if (chatRoom.type.equals(Constants.GROUP_CHAT)) {
                                    chatRoom.name ?: ""
                                } else {
                                    androidUserVM.getUser(
                                        chatRoom.getOtherUserId(androidUserVM.getCurrentUserUID())
                                            ?: ""
                                    )?.name ?: ""
                                },
                                modifier = Modifier.weight(1f),
                                style = TypographyBold.bodySmall,
                                color = NeutralBlack,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Row() {
                                Image(
                                    painter = if (chatRoom.isFavorite)
                                        painterResource(R.drawable.ic_favorite_golden) else
                                        painterResource(R.drawable.ic_favorite_icon_gray),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .width(Dimensions.size20)
                                        .height(
                                            Dimensions.size20
                                        )
                                        .clickable {
                                            viewModel.updateFavorite(
                                                !chatRoom.isFavorite,
                                                chatRoom.id
                                            )
                                        }
                                )
                                Spacer(modifier = Modifier.width(Dimensions.size10))

                                Text(
                                    text = Utils.getTime(chatRoom.lastTimestamp),
                                    style = Typography.bodySmall,
                                    color = NeutralDarkGrey,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = Dimensions.textSize12
                                )
                            }
                        }
                        if (false) {
                            Spacer(Modifier.height(Dimensions.size5))
                            Text(
                                text = "",
                                style = Typography.bodySmall,
                                color = PrimaryDarkerLightB75,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis, fontSize = Dimensions.textSize12
                            )

                        }
                        Spacer(Modifier.height(Dimensions.size3))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = chatRoom.lastMessage,
                                modifier = Modifier.weight(1f),
                                style = Typography.bodySmall,
                                color = NeutralDarkGrey,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = Dimensions.textSize12
                            )
                            if(chatRoom.unreadCounts.isNotEmpty())
                                chatRoom.unreadCounts[androidUserVM.getCurrentUserUID()]?.let {
                                    if(it > 0L)
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .size(Dimensions.size20)
                                                .clip(CircleShape)
                                                .background(VividRed)
                                        ) {
                                            Text(
                                                text = if (chatRoom.unreadCounts.isNotEmpty()) {
                                                    "${chatRoom.unreadCounts[androidUserVM.getCurrentUserUID()]}"
                                                } else {
                                                    "0"
                                                },
                                                style = TypographyBold.bodySmall,
                                                color = NeutralWhite,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                fontSize = Dimensions.textSize12
                                            )
                                        }
                                }
                        }
                    }
                }
            }
        }

    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun ChatListPreview() {
    var dataStoreManager = DataStoreManager(LocalContext.current)
    var androidVM = AndroidUserVM(
        UserRepoImpl(), dataStoreManager, UserRepository(
            UserAPIServiceImpl(
                KtorClient()
            )
        )
    )
    var videModel: ChatListViewModel = ChatListViewModel(ChatRepository(), androidVM)
    ChatListingScreen({}, {}, {}, videModel, androidVM)
}