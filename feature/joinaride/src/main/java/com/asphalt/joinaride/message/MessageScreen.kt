package com.asphalt.joinaride.message

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

@Composable
fun MessageScreen(
    viewModel: MessageViewModel,
    onCancel: () -> Unit,
    onSend: () -> Unit
) {

    //val state = viewModel.uiState

    // Close dialog after success
//    LaunchedEffect(state.isSuccess) {
//        if (state.isSuccess) {
//            onSubmit()
//        }
//    }

    Dialog(
        onDismissRequest = { onCancel() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {

        Card(
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(NeutralWhite)
                .padding(14.dp)
        ) {

            Column(modifier = Modifier
                .background(NeutralWhite)) {

                Text(
                    text = "Message from Sooraj Sajan",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                QuickMessageSection(viewModel)

                Spacer(modifier = Modifier.height(20.dp))

                Text("Custom Message")

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = "state.messageText",
                    onValueChange = {
//                        viewModel.onEvent(
//                            MessageEvent.OnCustomMessageChange(it)
//                        )
                    },
                    //placeholder = { Text("Type your message...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Button(
                        onClick = {
                            //onNavigateToDashboard.invoke()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeutralWhite),
                        modifier = Modifier
                            .weight(weight = 1f)
                            .height(height = Dimensions.size60)
                            .border(
                                width = 1.dp, color = PrimaryBrighterLightW75,
                                shape = RoundedCornerShape(size = Dimensions.size10)
                            ),
                    ) {
                        Text(
                            stringResource(R.string.cancel).uppercase(),
                            color = PrimaryBrighterLightW75,
                            style = TypographyBold.titleMedium,
                            fontSize = Dimensions.textSize16,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }

                    GradientButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                           // onNavigateToDashboard.invoke()

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
                                modifier = Modifier.padding(start = 8.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickMessageSection(viewModel: MessageViewModel) {

    val quickMessages = listOf(
        "All good!",
        "Taking a break",
        "Fuel stop",
        "Road issue"
    )

    Column(modifier = Modifier.background(color = NeutralWhite)) {

        Text("Quick Messages")

        Spacer(modifier = Modifier.height(8.dp))

        quickMessages.chunked(2).forEach { rowItems ->

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                rowItems.forEach { message ->

                    QuickMessageButton(
                        text = message,
                        modifier = Modifier.weight(1f)
                    ){

                    }

                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
@Preview
fun MessageScreenPreview() {
    MessageScreen(onCancel = {}, onSend = {}, viewModel = viewModel())
}

