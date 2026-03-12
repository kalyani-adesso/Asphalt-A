package com.asphalt.commonui.utils

import android.app.AlertDialog

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.RedLight
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.TypographyMedium

@Composable
fun CustomLogoutDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String = "Logout",
    message: String = "Are you sure you want to logout?",
    positiveButton: String = "Yes",
    negButton: String = "No"
) {
    if (showDialog) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Card(
                shape = RoundedCornerShape(Dimensions.padding16),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.size8),
                colors = CardDefaults.cardColors(
                    NeutralWhite
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = Dimensions.padding24, bottom = Dimensions.padding24),

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title, style = TypographyBold.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(Dimensions.padding16))
                    Text(
                        text = message,
                        style = Typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(Dimensions.padding24))
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(end = Dimensions.padding30),
                        horizontalArrangement = Arrangement.End
                        ) {
                        Text(
                            text = negButton,//stringResource(R.string.cancel),
                            style = TypographyMedium.bodyMedium,
                            color = RedLight,
                            modifier = Modifier.clickable {
                                onDismiss()
                            }
                        )
                        Spacer(modifier = Modifier.width(Dimensions.size20))
                        Text(
                            positiveButton,//stringResource(R.string.ok),
                            style = TypographyMedium.bodyMedium,
                            color = PrimaryDarkerLightB75,
                            modifier = Modifier.clickable {
                                onConfirm()
                            })

                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CustomLogoutDialogPreview() {
    CustomLogoutDialog(true, {}, {})
}