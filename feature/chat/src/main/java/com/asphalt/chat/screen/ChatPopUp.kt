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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.asphalt.commonui.R
import com.asphalt.commonui.R.string
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

@Composable
fun ChatDialog(
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(modifier = Modifier.padding(horizontal = Dimensions.padding20)) {
            Box(
                modifier = Modifier
                    .background(NeutralWhite, RoundedCornerShape(Dimensions.padding20))
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            ) {
                val messages = listOf(
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
                )
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
                        Row(modifier = Modifier.padding(horizontal = Dimensions.size10)) {
                            Row(modifier = Modifier.weight(1f)) {
                                CircularNetworkImage(
                                    modifier = Modifier.border(
                                        width = Dimensions.size2pt5,
                                        color = NeutralWhite,
                                        shape = CircleShape
                                    ),
                                    size = Dimensions.padding40,
                                    imageUrl = "" ?: ""
                                )
                                Column(modifier = Modifier.padding(start = Dimensions.size10)) {
                                    Text(
                                        "Hari",
                                        overflow = TextOverflow.Ellipsis,
                                        style = TypographyBold.bodyMedium,
                                        color = NeutralWhite
                                    )
                                    Spacer(modifier = Modifier.height(Dimensions.size8))
                                    Text(
                                        "Weekend Ride - Kochi to Kanyakumari rrrrr",
                                        modifier = Modifier,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = Typography.bodySmall, color = NeutralWhite
                                    )
                                }
                            }
                            RoundedBox(
                                modifier = Modifier
                                    .size(Dimensions.size30)
                                    .clickable {
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
                    Box(modifier = Modifier.weight(1f).background(BlueLite36)) {


                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = Dimensions.size10, end = Dimensions.size10)
                        ) {
                            items(messages) { msg ->
                                ChatBubble(msg)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.fillMaxWidth().height(Dimensions.padding1)
                        .background(color = NeutralLightGrayishBlue50).shadow(elevation = Dimensions.padding15))
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
                                value = text,
                                onValueChange = { text = it },
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
                            modifier = Modifier.size(Dimensions.size44),
                            cornerRadius = Dimensions.size10,
                            backgroundColor = PrimaryDarkerLightB75,
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

data class ChatMessage(
    val text: String,
    val isSender: Boolean
)

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ChatBubble(message: ChatMessage) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val bubbleWidth = screenWidth / 2
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimensions.size4),
        horizontalArrangement = if (message.isSender)
            Arrangement.End else Arrangement.Start
    ) {

        Box(
            modifier = Modifier
                .shadow(Dimensions.spacing12, shape = RoundedCornerShape(Dimensions.spacing12))
                .background(
                    if (message.isSender) NeutralWhite else BlueLite34,
                    RoundedCornerShape(Dimensions.spacing12)
                )
                .widthIn(
                    max = bubbleWidth
                )
                .padding(Dimensions.size10)
        ) {
            Text(message.text, style = Typography.bodySmall)
        }
    }
}

@Preview
@Composable
fun ChatPreview() {
    ChatDialog({})
}
