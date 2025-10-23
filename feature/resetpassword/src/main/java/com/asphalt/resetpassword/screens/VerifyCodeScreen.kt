package com.asphalt.resetpassword.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.asphalt.commonui.R
import com.asphalt.commonui.R.string
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.utils.ComposeUtils

@Composable
fun VerifyScreen(onVerifyClick:()->Unit) {
    AsphaltTheme {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = NeutralWhite)
                    .padding(
                        bottom = paddingValues.calculateBottomPadding(),
                        top = paddingValues.calculateTopPadding()
                    ), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(Dimensions.spacing54))
                Image(
                    painter = painterResource(R.drawable.ic_mail_box),
                    contentDescription = "",
                    modifier = Modifier
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center

                ) {
                    val message = buildAnnotatedString {
                        append("We’ve sent a 5-digit verification code to ")

                        withStyle(style = SpanStyle(color = PrimaryDarkerLightB75)) { // blue color
                            append("\nhari@test.com")
                        }
                    }
                    Text(
                        text = stringResource(string.check_email),
                        style = TypographyBold.headlineMedium
                    )
                    Text(
                        text = message,
                        modifier = Modifier.padding(top = Dimensions.padding15),
                        style = TypographyMedium.bodyMedium,
                        color = NeutralDarkGrey, textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(Dimensions.spacing20))

                }
                Spacer(Modifier.height(Dimensions.spacing20))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Dimensions.padding, end = Dimensions.padding)
                        .weight(1f)

                ) {
                    Text(
                        text = stringResource(string.enter_verification_code),
                        modifier = Modifier.padding(top = Dimensions.spacing20),
                        style = TypographyBold.titleMedium,
                        color = NeutralBlack
                    )
                    Spacer(modifier = Modifier.height(Dimensions.spacing10))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimensions.size56)
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
                            value = "",
                            onValueChange = { },
                            placeholder = {
                                Text(
                                    text = stringResource(string.enter_code),
                                    style = Typography.bodyMedium,
                                    color = NeutralDarkGrey
                                )
                            },
                            textStyle = Typography.bodyMedium,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,

                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent
                            ),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_sms_pencil),
                                    contentDescription = "Email icon",
                                    tint = Color.Unspecified

                                )
                            },
//                            trailingIcon = {
//
//                                Icon(
//                                    painter = painterResource(R.drawable.ic_tick_green),
//                                    contentDescription = "Email icon",
//                                    tint = Color.Unspecified
//
//                                )
//
//
//                            }

                        )
                        Box(
                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Resend in 3:03",
                                modifier = Modifier.padding(end = Dimensions.size),
                                style = Typography.bodyLarge,
                                color = NeutralDarkGrey
                            )
                        }


                    }
                    Spacer(modifier = Modifier.height(Dimensions.spacing20))
                    GradientButton(
                        startColor = PrimaryDarkerLightB75,
                        endColor = PrimaryDarkerLightB50, onClick = {
                            onVerifyClick.invoke()
                        }
                    ) {
                        ComposeUtils.DefaultButtonContent(

                            stringResource(string.verify_code).uppercase()
                        )

                    }
                    //Spacer(Modifier.height(Dimensions.spacing10))
                }

            }


        }
    }
}


@Preview
@Composable
fun VerifyScreenPreview() {
    VerifyScreen({})
}
