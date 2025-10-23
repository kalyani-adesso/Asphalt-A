package com.asphalt.resetpassword.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.R
import com.asphalt.commonui.R.string
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralBlackGrey
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightGray25
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.BorderedButton
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.utils.ComposeUtils

@Composable
fun CreatePasswordScreen(onUpdateClick: () -> Unit, onBackClick: () -> Unit) {
    val scrollState = rememberScrollState()
    AsphaltTheme {
        Scaffold { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = NeutralWhite)
                    .padding(
                        bottom = paddingValues.calculateBottomPadding(),
                        //top = paddingValues.calculateTopPadding()
                    )
                    .verticalScroll(scrollState)
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Spacer(modifier = Modifier.height(80.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Text(
                        text = stringResource(string.create_new_password),
                        style = TypographyBold.headlineMedium
                    )
                    Text(
                        text = stringResource(string.enter_your_password),
                        modifier = Modifier.padding(top = Dimensions.padding15),
                        style = TypographyMedium.bodyMedium,
                        color = NeutralDarkGrey
                    )
                    Spacer(Modifier.height(Dimensions.spacing20))
                    Image(
                        painter = painterResource(R.drawable.ic_app_icon),
                        contentDescription = "",
                        modifier = Modifier
                            .height(Dimensions.size60)
                            .width(Dimensions.padding50)

                    )

                    Text(
                        text = stringResource(string.adesso_rider_club),
                        modifier = Modifier.padding(top = Dimensions.spacing20),
                        style = TypographyMedium.headlineSmall
                    )

                }
                Spacer(Modifier.height(Dimensions.spacing20))


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Dimensions.padding, end = Dimensions.padding),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(string.email),
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
                                color = NeutralLightGray25,
                                shape = RoundedCornerShape(Dimensions.padding10)
                            ), verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = "",
                            onValueChange = { },
                            placeholder = {
                                Text(
                                    text = stringResource(string.enter_email),
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
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_email_blue_icon),
                                    contentDescription = "Email icon",
                                    tint = Color.Unspecified

                                )
                            },
                            trailingIcon = {

                                Icon(
                                    painter = painterResource(R.drawable.ic_tick_green),
                                    contentDescription = "Email icon",
                                    tint = Color.Unspecified

                                )


                            }

                        )

                    }

                    Text(
                        text = stringResource(string.new_password),
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
                                NeutralLightGray25, shape = RoundedCornerShape(Dimensions.padding10)
                            )
                            .border(
                                width = Dimensions.padding1,
                                color = NeutralLightGray25,
                                shape = RoundedCornerShape(Dimensions.padding10)
                            ), verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = "",
                            onValueChange = { },
                            placeholder = {
                                Text(
                                    text = stringResource(string.enter_new_password),
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
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_password_icon_blue),
                                    contentDescription = "Email icon",
                                    tint = Color.Unspecified

                                )
                            },
                            trailingIcon = {

                                Icon(
                                    painter = painterResource(R.drawable.ic_eye_slash),
                                    contentDescription = "Email icon",
                                    tint = Color.Unspecified

                                )


                            }

                        )

                    }

                    Text(
                        text = stringResource(string.confirm_password),
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
                                NeutralLightGray25, shape = RoundedCornerShape(Dimensions.padding10)
                            )
                            .border(
                                width = Dimensions.padding1,
                                color = NeutralLightGray25,
                                shape = RoundedCornerShape(Dimensions.padding10)
                            ), verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = "",
                            onValueChange = { },
                            placeholder = {
                                Text(
                                    text = stringResource(string.confirm_new_password),
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
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_password_icon_blue),
                                    contentDescription = "Email icon",
                                    tint = Color.Unspecified

                                )
                            },
                            trailingIcon = {

                                Icon(
                                    painter = painterResource(R.drawable.ic_eye_slash),
                                    contentDescription = "Email icon",
                                    tint = Color.Unspecified

                                )


                            }

                        )

                    }
                    //Spacer(Modifier.height(Dimensions.spacing10))
                }

                Spacer(modifier = Modifier.height(Dimensions.spacing20))
                Column(
                    modifier = Modifier.padding(
                        start = Dimensions.padding,
                        end = Dimensions.padding
                    )
                ) {
                    GradientButton(
                        startColor = PrimaryDarkerLightB75,
                        endColor = PrimaryDarkerLightB50, onClick = {
                            onUpdateClick.invoke()
                        }
                    ) {
                        ComposeUtils.DefaultButtonContent(

                            stringResource(R.string.update_password).uppercase()
                        )

                    }
                    Spacer(modifier = Modifier.height(Dimensions.spacing20))
                    BorderedButton(
                        onClick = {
                            onBackClick.invoke()
                        },
                        modifier = Modifier
                            .height(Dimensions.size60)
                            .background(NeutralWhite)
                            .fillMaxWidth(),
                        buttonRadius = Dimensions.size10, borderColor = NeutralBlackGrey
                    ) {
                        ComposeUtils.DefaultButtonContent(
                            stringResource(R.string.back_to_sign_in).uppercase(),
                            color = NeutralBlackGrey
                        )
//                        Text(
//                            text = stringResource(R.string.back_to_sign_in),
//                            style = TypographyMedium.bodySmall,
//                            color = PrimaryDarkerLightB75
//                        )
                    }
                }
                Spacer(modifier = Modifier.height(Dimensions.spacing20))

            }


        }
    }
}

@Preview
@Composable
fun CreatePasswordReview() {
    CreatePasswordScreen({

    }, {

    })
}