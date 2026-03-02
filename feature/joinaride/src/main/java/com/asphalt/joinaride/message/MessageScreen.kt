package com.asphalt.joinaride.message

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
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
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralGrey
import com.asphalt.commonui.theme.NeutralLightGrey
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PaleGreen
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.joinaride.viewmodel.MessageViewModel

@Composable
fun MessageScreen(
    viewModel: MessageViewModel = viewModel(),
    onCancel: () -> Unit,
    onSubmit: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState()

    Dialog(
        onDismissRequest = { onCancel },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {

        Box(
            contentAlignment = Alignment.Center,
        ) {
            Card(
                shape = RoundedCornerShape(Dimensions.padding20),
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(Dimensions.padding14)
                ) {
                    Text(
                        text = "Message ${uiState.value.driverName} ",
                        style = Typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(Dimensions.padding4))

                    Text(
                        text = "Delayed by ${uiState.value.delayMinutes} min",
                        style = Typography.labelSmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(Dimensions.padding16))

                    // Incoming message card

                    Card(
                        shape = RoundedCornerShape(Dimensions.padding10),
                        colors = CardDefaults.cardColors(
                            containerColor = PaleGreen
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))

                        Column(
                            modifier = Modifier.padding(Dimensions.padding14)
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Row(verticalAlignment = Alignment.CenterVertically) {

                                    Image(
                                        painter = painterResource(
                                            id = R.drawable.ic_profile

                                        ),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .border(
                                                1.dp,
                                                Color(0xFF00C853),
                                                CircleShape
                                            ),
                                        contentScale = ContentScale.Crop
                                    )

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = "Sooraj Sajan",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }

                                Text(
                                    text = "Just now",
                                    color = Color.Gray
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Fuel Stop...",
                                fontSize = Typography.labelSmall.fontSize
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Dimensions.padding20))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.padding14)
                ) {
                    Text(
                        text = "Quick Messages",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing12),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        QuickMessageButton(
                            "All good!",
                            modifier = Modifier.weight(1f)
                        ) {
                            viewModel.onEvent(
                                MessageDriverEvent.OnQuickMessageClick("All good!")
                            )
                        }
                        QuickMessageButton(
                            "Taking a break",
                            modifier = Modifier.weight(1f)
                        ) {
                            viewModel.onEvent(
                                MessageDriverEvent.OnQuickMessageClick("Taking a break")
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(Dimensions.spacing12))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing12),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        QuickMessageButton(
                            "Fuel stop",
                            modifier = Modifier.weight(1f)
                        ) {
                            viewModel.onEvent(
                                MessageDriverEvent.OnQuickMessageClick("Fuel stop")
                            )
                        }
                        QuickMessageButton(
                            "Road issue",
                            modifier = Modifier.weight(1f)
                        ) {
                            viewModel.onEvent(
                                MessageDriverEvent.OnQuickMessageClick("Road issue")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimensions.padding24))

                    Text(
                        text = "Custom Message",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = "",
                        onValueChange = {
                            viewModel.onEvent(
                                MessageDriverEvent.OnCustomMessageChange(it)
                            )
                        },
                        placeholder = { Text("Type your message...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        shape = RoundedCornerShape(Dimensions.spacing12)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            Dimensions.padding16, Alignment.CenterHorizontally
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Dimensions.padding18)
                    ) {

                        Button(
                            onClick = {
                                //onDismiss.invoke()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeutralWhite),
                            modifier = Modifier
                                .weight(1f)
                                .height(Dimensions.size60)
                                .border(
                                    1.dp, color = NeutralGrey,
                                    shape = RoundedCornerShape(size = Dimensions.size10)
                                ),
                        ) {
                            Text(
                                stringResource(R.string.cancel).uppercase(),
                                color = NeutralBlack,
                                style = TypographyBold.titleMedium,
                                fontSize = Dimensions.textSize16,
                                modifier = Modifier.padding(start = Dimensions.padding8),
                            )
                        }

                        GradientButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                // viewModel.submitRating(rideId = ridesData.ridesID ?: "", userId = ridesData.createdBy?:"")
                                //onSubmit.invoke()
                                // viewModel.updateRateStatus(rideId = ridesData.ridesID ?: "",rating)

                                // onDismiss.invoke()

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
}

@Composable
@Preview
fun MessageScreenPreview() {
    MessageScreen(onCancel = {}) { }
}

