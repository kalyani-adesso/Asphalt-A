package com.asphalt.chat.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.asphalt.android.datastore.DataStoreManager
import com.asphalt.android.network.KtorClient
import com.asphalt.android.network.user.UserAPIServiceImpl
import com.asphalt.android.repository.AuthenticatorImpl
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.repository.chat.ChatRepository
import com.asphalt.android.repository.user.UserRepository
import com.asphalt.android.viewmodel.AuthViewModel
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.chat.model.ChatParamsModel
import com.asphalt.chat.viewmodel.ChatScreenViewModel
import com.asphalt.commonui.R
import com.asphalt.commonui.R.string
import com.asphalt.commonui.theme.BlueLite25
import com.asphalt.commonui.theme.BlueLite34
import com.asphalt.commonui.theme.BlueLite36
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightGrayishBlue50
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.commonui.ui.RoundedBox
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatDialog(
    initaliseChat: Boolean = true,
    receiverID: String,
    viewModel: ChatScreenViewModel = koinViewModel(),
    isGroupChat: Boolean = false,
    onDismiss: () -> Unit,
    chatParams: ChatParamsModel? = null
) {
    if (initaliseChat) {
        if (isGroupChat) {
            chatParams?.let { viewModel.initializeGroupChat(it) }
        } else {
            viewModel.initialise1V1Chat(receiverID)
        }

    }
    val listState = rememberLazyListState()
    var msgText by remember { mutableStateOf("") }
    val messages by viewModel.chatMessage.collectAsState()
    Dialog(
        onDismissRequest = {
            viewModel.clearChat()
            onDismiss
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(modifier = Modifier.padding(horizontal = Dimensions.padding20)) {
            Box(
                modifier = Modifier
                    .background(NeutralWhite, RoundedCornerShape(Dimensions.padding20))
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            ) {
                /* val messages = listOf(
                     ChatMessage("Hello!", true),
                     ChatMessage("Hi! How are you?", false),
                     ChatMessage("I'm good, thanks!", true),
                     ChatMessage("Nice to hear 😊 gggggg ggggggggg ggggggg", false),
                     ChatMessage("Nice to hear 😊", false),
                     ChatMessage("Nice to hear 😊", false),
                     ChatMessage("Nice to hear 😊", false),
                     ChatMessage("Nice to hear 😊", false),
                     ChatMessage("Nice to hear 😊", false),
                     ChatMessage("Nice to hear 😊", false),
                 )*/
                Column(Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .height(Dimensions.size71)
                            .fillMaxWidth()
                            .background(
                                BlueLite34, RoundedCornerShape(
                                    Dimensions.size20, Dimensions.size20, 0.dp, 0.dp
                                )
                            ),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = Dimensions.size10),
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularNetworkImage(
                                    modifier = Modifier.border(
                                        width = Dimensions.size2pt5,
                                        color = NeutralWhite,
                                        shape = CircleShape
                                    ),
                                    size = Dimensions.padding40,
                                    imageUrl = "" ?: ""
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(start = Dimensions.size10), // take remaining horizontal space
                                    verticalArrangement = Arrangement.Center, // center vertically relative to image
                                    horizontalAlignment = Alignment.Start // align text to start horizontally
                                ) {
                                    Text(
                                        text = if (isGroupChat) {
                                            chatParams?.title ?: ""
                                        } else {
                                            viewModel.getName(receiverID)
                                        },
                                        overflow = TextOverflow.Ellipsis,
                                        style = TypographyBold.bodyMedium,
                                        color = NeutralWhite, maxLines = 1
                                    )
                                    /*Spacer(modifier = Modifier.height(Dimensions.size8))
                                    Text(
                                        "Weekend Ride - Kochi to Kanyakumari rrrrr",
                                        modifier = Modifier,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = Typography.bodySmall, color = NeutralWhite
                                    )*/
                                }
                            }
                            RoundedBox(
                                modifier = Modifier
                                    .size(Dimensions.size30)
                                    .clickable {
                                        viewModel.clearChat()
                                        onDismiss.invoke()
                                    },
                                cornerRadius = Dimensions.size10,
                                backgroundColor = PrimaryDarkerLightB75
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_close_white),
                                    contentDescription = ""
                                )
                            }

                        }

                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(BlueLite36)
                    ) {


                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = Dimensions.size10, end = Dimensions.size10),
                            state = listState,
                            reverseLayout = true
                        ) {
                            items(messages) { msg ->
                                ChatBubble(msg)
                            }
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimensions.padding1)
                            .background(color = NeutralLightGrayishBlue50)
                            .shadow(elevation = Dimensions.padding15)
                    )
                    Row(
                        modifier = Modifier.padding(
                            start = Dimensions.padding10, end = Dimensions.padding10,
                            top = Dimensions.padding10
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(Dimensions.size50)
                                .weight(1f)
                                .background(
                                    NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10)
                                )
                                .border(
                                    width = Dimensions.padding1,
                                    color = PrimaryDarkerLightB75,
                                    shape = RoundedCornerShape(Dimensions.padding10)
                                ), verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = msgText,
                                onValueChange = { msgText = it },
                                placeholder = {
                                    Text(
                                        text = stringResource(string.type_msg),
                                        style = Typography.bodySmall,
                                        color = NeutralDarkGrey
                                    )
                                },
                                textStyle = Typography.bodySmall,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,

                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    errorIndicatorColor = Color.Transparent
                                ),

                                )


                        }
                        Spacer(modifier = Modifier.width(Dimensions.size10))
                        RoundedBox(
                            modifier = Modifier
                                .size(Dimensions.size44)
                                .clickable {
                                    if (msgText.isNotEmpty()) {
                                        if (isGroupChat) {
                                            chatParams?.let {
                                                viewModel.sendGroupChatMessage(
                                                    it,
                                                    msgText
                                                )
                                            }
                                        } else {
                                            viewModel.send1V1Chat(receiverID, msgText)
                                        }

                                        /* if (viewModel.chatMessage.value.size > 0 && viewModel.chatMessage.value.size % 2 == 0) {
                                             viewModel.updateChatMessage(
                                                 ChatMessage(
                                                     text = msgText,
                                                     true
                                                 )
                                             )
                                         } else {
                                             viewModel.updateChatMessage(
                                                 ChatMessage(
                                                     text = msgText,
                                                     false
                                                 )
                                             )
                                         }*/

                                        msgText = ""
                                    }

                                },
                            cornerRadius = Dimensions.size10,
                            backgroundColor = if (msgText.isNotEmpty()) PrimaryDarkerLightB75 else BlueLite25,
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_send_white),
                                contentDescription = "",
                            )
                        }

                    }
                    Spacer(Modifier.height(Dimensions.size10))
                }
            }
        }
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun ChatPreview() {

    var dataStoreManager = DataStoreManager(LocalContext.current)
    var androidVM = AndroidUserVM(
        UserRepoImpl(), dataStoreManager, UserRepository(
            UserAPIServiceImpl(
                KtorClient()
            )
        )
    )
    val viewModel: ChatScreenViewModel = ChatScreenViewModel(androidVM, ChatRepository())
    ChatDialog(false, "", viewModel, false, {})
}
