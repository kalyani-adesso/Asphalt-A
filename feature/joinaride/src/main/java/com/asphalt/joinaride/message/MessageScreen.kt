package com.asphalt.joinaride.message

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralGrey
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PaleGreen
import com.asphalt.commonui.theme.PrimaryBrighterLightW75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.joinaride.viewmodel.MessageViewModel
import io.ktor.util.collections.getValue
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MessageScreenUI(
    onCancel: () -> Unit,
    onSend: () -> Unit,
    viewModel: MessageViewModel = koinViewModel(),
    androidUserVM: AndroidUserVM = koinViewModel(),

) {

    val message by remember { viewModel::customMessage }
    val currentUid = remember { viewModel.currentUid }
    val currentUser = remember { viewModel.currentUser }

    Log.d("TAG", "MessageScreenUI: $currentUid + $currentUser")

    Dialog(
        onDismissRequest = { onCancel() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(Dimensions.padding),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.padding8),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.padding)
            ) {
                // HEADER
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_profile),
                        contentDescription = null,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(Dimensions.padding13))

                    Column {
                        Text(
                            text = "Aromal",// receiver name
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF00C853))
                            )
                            Spacer(modifier = Modifier.width(Dimensions.padding6))
                            Text(
                                text = "Connected",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(Dimensions.padding20))
                // Incoming Message Card
                Card(
                    shape = RoundedCornerShape(Dimensions.padding8),
                    colors = CardDefaults.cardColors(
                        containerColor = PaleGreen
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(Dimensions.padding)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {

                                Image(
                                    painter = painterResource(id = R.drawable.ic_profile),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(Dimensions.radius40)
                                        .clip(CircleShape)
                                        .border(
                                            1.dp,
                                            Color(0xFF00C853),
                                            CircleShape
                                        ),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(Dimensions.padding10))

                                Text(
                                    text = "Sooraj Sajan",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                            Text(
                                text = "Just now",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.height(Dimensions.padding13))

                        Text(
                            text = "Fuel Stop...",
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(Dimensions.padding20))
                // Quick Messages Title
                Text(
                    text = "Quick Messages",
                    fontSize = 14.sp,
                )
                Spacer(modifier = Modifier.height(Dimensions.padding8))
                // Quick Message Buttons
                Column(verticalArrangement = Arrangement.spacedBy(Dimensions.padding)) {

                    Row(horizontalArrangement = Arrangement.spacedBy(Dimensions.padding)) {
                        QuickMessageButton("All good!",
                            Modifier.weight(1f), onClick = {
                                viewModel.onQuickMessageClick("All good!")
                            })
                        QuickMessageButton("Taking a break", Modifier.weight(1f), onClick = {
                            viewModel.onQuickMessageClick("Taking a break")
                        })
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(Dimensions.padding)) {
                        QuickMessageButton("Fuel stop", Modifier.weight(1f), onClick = {
                            viewModel.onQuickMessageClick("Fuel stop")
                        })
                        QuickMessageButton("Road issues", Modifier.weight(1f), onClick = {
                            viewModel.onQuickMessageClick("Road issues")
                        })
                    }
                }
                Spacer(modifier = Modifier.height(Dimensions.padding24))
                // Custom Message Title
                Text(
                    text = "Custom Message",
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.height(Dimensions.padding8))

                OutlinedTextField(
                    value = message,
                    onValueChange = {viewModel.onCustomMessageChange(it)},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    shape = RoundedCornerShape(Dimensions.padding8)
                )
                Spacer(modifier = Modifier.height(Dimensions.padding24))
                // Buttons Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.padding),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { onCancel() },
                        modifier = Modifier
                            .weight(1f)
                            .height(55.dp),
                        shape = RoundedCornerShape(Dimensions.padding8)
                    ) {
                        Text(
                            text = "CANCEL",
                            fontWeight = FontWeight.Bold
                        )
                    }
                    GradientButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            viewModel.sendMessage(senderID = currentUid?: "", senderName = currentUser?:"",
                                receiverID = "", receiverName = "", onGoingRideID = "", isRideOnGoing = true,
                                message = viewModel.customMessage)
                        },
                        buttonHeight = Dimensions.size60,
                        contentPadding = PaddingValues(Dimensions.size2)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
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
@Composable
@Preview
fun MessageScreenPreview() {
    MessageScreenUI(onCancel = {}, onSend = {})
}