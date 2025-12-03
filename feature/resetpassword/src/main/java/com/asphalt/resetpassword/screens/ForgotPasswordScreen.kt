package com.asphalt.resetpassword.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.android.network.KtorClient
import com.asphalt.android.network.user.UserAPIServiceImpl
import com.asphalt.android.repository.AuthenticatorImpl
import com.asphalt.android.repository.user.UserRepository
import com.asphalt.android.viewmodel.AuthViewModel
import com.asphalt.commonui.R
import com.asphalt.commonui.R.string
import com.asphalt.commonui.UIState
import com.asphalt.commonui.UIStateHandler
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralRed
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.BouncingCirclesLoader
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.resetpassword.viewmodel.ForgotPasswordViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForgotPasswordScreen(
    onSendClick: (String) -> Unit,
    viewModel: ForgotPasswordViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    AsphaltTheme {

        if (viewModel.showSuccess.value) {
            val successMessage = stringResource(R.string.reset_success_msg)
            LaunchedEffect(Unit) {
                UIStateHandler.sendEvent(UIState.SUCCESS(successMessage))
                onSendClick.invoke("")
            }

        }
        if (viewModel.showFailure.value) {
            val successMessage = stringResource(R.string.user_not_found)
            LaunchedEffect(Unit) {
                UIStateHandler.sendEvent(UIState.Error(successMessage))
            }

        }


        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = NeutralWhite)
                    .padding(
                        bottom = paddingValues.calculateBottomPadding(),
                        // top = paddingValues.calculateTopPadding()
                    ), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(80.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Text(
                        text = stringResource(string.forgot_password_q),
                        style = TypographyBold.headlineMedium
                    )
                    Text(
                        text = stringResource(string.sent_reset_instruction),
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


                Image(
                    painter = painterResource(R.drawable.ic_forgot_passowrd),
                    contentDescription = "",
                    modifier = Modifier.weight(1.5f)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Dimensions.padding, end = Dimensions.padding)
                        .weight(1f)

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
                                color = PrimaryDarkerLightB75,
                                shape = RoundedCornerShape(Dimensions.padding10)
                            ), verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = viewModel.emailState.value,
                            onValueChange = { viewModel.updateEmail(it) },
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
                                if (viewModel.isShowTick.value) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_tick_green),
                                        contentDescription = "Email icon",
                                        tint = Color.Unspecified

                                    )

                                }
                            }

                        )

                    }
                    if (viewModel.isShowError.value) {
                        Text(
                            text = stringResource(R.string.enter_valid_email),
                            Modifier.padding(top = Dimensions.size4),
                            style = Typography.bodySmall,
                            color = NeutralRed
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimensions.spacing20))
                    GradientButton(
                        startColor = PrimaryDarkerLightB75,
                        endColor = PrimaryDarkerLightB75, onClick = {
                            if (viewModel.sendCode())
                                viewModel.callRestPassword()
                            //onSendClick.invoke(viewModel.emailState.value)
                        }
                    , buttonHeight = Dimensions.size50) {
                        ComposeUtils.DefaultButtonContent(

                            stringResource(R.string.send_reset_code).uppercase()
                        )

                    }
                    //Spacer(Modifier.height(Dimensions.spacing10))
                }

            }


        }
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun ForgotPasswordPreview() {
    var modelauth: AuthViewModel = AuthViewModel(AuthenticatorImpl())
    val viewModel: ForgotPasswordViewModel = ForgotPasswordViewModel(
        modelauth,
        UserRepository(UserAPIServiceImpl(KtorClient()))
    )
    ForgotPasswordScreen({}, viewModel)
}
