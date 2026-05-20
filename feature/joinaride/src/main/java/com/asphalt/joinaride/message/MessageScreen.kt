package com.asphalt.joinaride.message

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.asphalt.android.model.connectedride.ConnectedRideDTO
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.DarkBrown
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GreenLIGHT25
import com.asphalt.commonui.theme.LightGreen
import com.asphalt.commonui.theme.LightPink
import com.asphalt.commonui.theme.LightYellow
import com.asphalt.commonui.theme.NeutralBrown
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.joinaride.viewmodel.MessageViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MessageScreenUI(
    onCancel: () -> Unit,
    ridesData: ConnectedRideDTO?,
    viewModel: MessageViewModel = koinViewModel(),
    androidUserVM: AndroidUserVM = koinViewModel()
) {

    val messagesList by viewModel.messagesList.collectAsStateWithLifecycle()
    val message by viewModel.customMessage.collectAsStateWithLifecycle()

    val currentUid = remember { viewModel.currentUid }
    val currentUser = remember { viewModel.currentUser }

    val name = androidUserVM.getUser(ridesData?.userID ?: "")?.name

    val listState = rememberLazyListState()

    LaunchedEffect(ridesData?.rideID, ridesData?.userID) {
        viewModel.listenForMessages(
            rideId = ridesData?.rideID ?: "",
            recevierId = ridesData?.userID ?: ""
        )
    }

    LaunchedEffect(messagesList.size) {
        if (messagesList.isNotEmpty()) {
            listState.animateScrollToItem(messagesList.lastIndex)
        }
    }

    Dialog(
        onDismissRequest = { onCancel() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.padding13),
            shape = RoundedCornerShape(Dimensions.padding10),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 650.dp)
                    .padding(Dimensions.padding16)
            ) {

                /* ---------------- HEADER ---------------- */

                Row(verticalAlignment = Alignment.CenterVertically) {

                    CircularNetworkImage(
                        imageUrl = "",
                        modifier = Modifier
                            .size(Dimensions.padding40)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(Dimensions.padding13))

                    Column {
                        Text(
                            text = name ?: "",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            when (ridesData?.status?.lowercase()) {
                                "connected" -> {
                                    Row(
                                        modifier = Modifier
                                            .background(
                                                color = LightGreen,
                                                shape = RoundedCornerShape(Dimensions.size5)
                                            )
                                            .height(Dimensions.padding16)
                                            .padding(
                                                start = Dimensions.size5,
                                                end = Dimensions.size5,
                                            ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Spacer(Modifier.width(Dimensions.size4))
                                        Text(
                                            text = ridesData.status,
                                            style = Typography.bodySmall.copy(fontSize = Dimensions.textSize12),
                                            color = GreenLIGHT25,
                                            modifier = Modifier
                                        )
                                    }
                                }

                                "delayed" -> {
                                    Row(
                                        modifier = Modifier
                                            .background(
                                                color = LightYellow,
                                                shape = RoundedCornerShape(Dimensions.size5)
                                            )
                                            .height(Dimensions.padding16)
                                            .padding(
                                                start = Dimensions.size5,
                                                end = Dimensions.size5,
                                            ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Spacer(Modifier.width(Dimensions.size4))
                                        Text(
                                            text = ridesData.status,
                                            style = Typography.bodySmall.copy(fontSize = Dimensions.textSize12),
                                            color = NeutralBrown,
                                            modifier = Modifier
                                        )
                                    }
                                }

                                "stopped" -> {
                                    Row(
                                        modifier = Modifier
                                            .background(
                                                color = LightPink,
                                                shape = RoundedCornerShape(Dimensions.size5)
                                            )
                                            .height(Dimensions.padding16)
                                            .padding(
                                                start = Dimensions.size5,
                                                end = Dimensions.size5,
                                            ),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Spacer(Modifier.width(Dimensions.size4))
                                        Text(
                                            text = ridesData.status,
                                            style = Typography.bodySmall.copy(fontSize = Dimensions.textSize12),
                                            color = DarkBrown,
                                            modifier = Modifier
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(Dimensions.padding16))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Dimensions.padding16))

                /* ---------------- MESSAGE LIST ---------------- */

                if (messagesList.isNotEmpty()) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(Dimensions.padding10) // space between items
                    ) {

                        items(messagesList) { msg ->

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(Dimensions.padding14),
                                border = BorderStroke(Dimensions.padding1, Color.LightGray),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFDFF3E4)
                                )
                            ) {

                                Column(
                                    modifier = Modifier.padding(Dimensions.padding14)
                                ) {

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {

                                        Row(verticalAlignment = Alignment.CenterVertically) {

                                            CircularNetworkImage(
                                                imageUrl = "",
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(CircleShape)
                                                    .border(
                                                        Dimensions.padding1,
                                                        Color.Green,
                                                        CircleShape
                                                    )
                                            )

                                            Spacer(modifier = Modifier.width(Dimensions.padding10))

                                            Text(
                                                text = msg.senderName ?: "",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp
                                            )
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {

                                            Icon(
                                                imageVector = Icons.Default.AccessTime,
                                                contentDescription = null,
                                                tint = Color.Gray,
                                                modifier = Modifier.size(Dimensions.padding14)
                                            )

                                            Spacer(modifier = Modifier.width(Dimensions.padding4))

                                            Text(
                                                text = formatTime(msg.timeStamp ?: 0),
                                                fontSize = 12.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(Dimensions.padding6))

                                    Text(
                                        text = msg.message ?: "",
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }


                Spacer(modifier = Modifier.height(12.dp))

                /* ---------------- QUICK MESSAGES ---------------- */

                Text("Quick Messages", fontSize = 14.sp)

                Spacer(modifier = Modifier.height(Dimensions.padding8))

                Column(verticalArrangement = Arrangement.spacedBy(Dimensions.padding8)) {

                    Row(horizontalArrangement = Arrangement.spacedBy(Dimensions.padding8)) {

                        QuickMessageButton(
                            "All good!",
                            Modifier.weight(1f)
                        ) {
                            viewModel.onQuickMessageClick("All good!")
                        }

                        QuickMessageButton(
                            "Taking a break",
                            Modifier.weight(1f)
                        ) {
                            viewModel.onQuickMessageClick("Taking a break")
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(Dimensions.padding8)) {

                        QuickMessageButton(
                            "Fuel stop",
                            Modifier.weight(1f)
                        ) {
                            viewModel.onQuickMessageClick("Fuel stop")
                        }

                        QuickMessageButton(
                            "Road issues",
                            Modifier.weight(1f)
                        ) {
                            viewModel.onQuickMessageClick("Road issues")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Dimensions.padding10))

                /* ---------------- MESSAGE INPUT ---------------- */

                Text("Custom Message", fontSize = 14.sp)

                Spacer(modifier = Modifier.height(Dimensions.padding6))

                OutlinedTextField(
                    value = message,
                    onValueChange = { viewModel.onCustomMessageChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    shape = RoundedCornerShape(Dimensions.padding8)
                )

                Spacer(modifier = Modifier.height(Dimensions.padding16))

                /* ---------------- BUTTONS ---------------- */

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.padding10),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    OutlinedButton(
                        onClick = { onCancel() },
                        modifier = Modifier
                            .weight(1f)
                            .height(55.dp),
                        shape = RoundedCornerShape(Dimensions.padding8)
                    )
                    {
                        Text(
                            text = "CANCEL",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    GradientButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            viewModel.sendMessage(
                                senderID = currentUid ?: "",
                                senderName = currentUser ?: "",
                                receiverID = ridesData?.userID ?: "",
                                receiverName = name ?: "",
                                onGoingRideID = ridesData?.rideID ?: "",
                                isRideOnGoing = true,
                                message = message,
                            )
                        },
                        buttonHeight = Dimensions.size60,
                        contentPadding = PaddingValues(Dimensions.size2)
                    )
                    {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        )
                        {
                            Text(
                                stringResource(R.string.send_reply).uppercase(),
                                color = NeutralWhite,
                                style = TypographyBold.titleMedium,
                                fontSize = Dimensions.textSize16,
                                modifier = Modifier.padding(start = Dimensions.padding8),
                            )
                        }
                    }
                }
            }
        }
    }
}

fun formatTime(timestamp: Long): String {

    val diff = System.currentTimeMillis() - timestamp

    val minutes = diff / (60 * 1000)

    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "$minutes min ago"
        else -> "${minutes / 60} hr ago"
    }
}