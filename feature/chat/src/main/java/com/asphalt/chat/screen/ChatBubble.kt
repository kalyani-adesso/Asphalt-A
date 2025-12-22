package com.asphalt.chat.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.chat.model.ChatMessage
import com.asphalt.commonui.theme.BlueLite34
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.ui.CircularNetworkImage

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
            Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!message.isSender) {
            CircularNetworkImage(
                modifier = Modifier.border(
                    width = Dimensions.size2pt5,
                    color = NeutralWhite,
                    shape = CircleShape
                ),
                size = Dimensions.padding28,
                imageUrl = "" ?: ""
            )
            Spacer(Modifier.width(Dimensions.size8))
        }
        Box(
            modifier = Modifier
                .shadow(Dimensions.spacing12, shape = RoundedCornerShape(Dimensions.spacing12))
                .background(
                    if (message.isSender) BlueLite34 else NeutralWhite,
                    RoundedCornerShape(Dimensions.spacing12)
                )
                .widthIn(
                    max = bubbleWidth
                )
                .padding(Dimensions.size10)
        ) {
            Text(
                message.text,
                style = Typography.bodySmall,
                color = if (message.isSender) NeutralWhite else NeutralBlack
            )
        }


    }
}

@Preview
@Composable
fun BubblePReview() {
    ChatBubble(ChatMessage("Test", true))
}