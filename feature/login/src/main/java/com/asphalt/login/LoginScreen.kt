package com.asphalt.login


import android.R
import android.provider.CalendarContract
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralBlackGrey
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralGrey
import com.asphalt.commonui.theme.NeutralMidGrey
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBlack
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.login.viewmodel.LoginScreenViewModel


import java.nio.file.WatchEvent

@Composable
fun LoginScreen(viewModel: LoginScreenViewModel = viewModel()) {
    var checked by remember { mutableStateOf(false) }

    var emailState = viewModel._emailTextState.collectAsState()
    var text2 by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(Dimensions.spacing85))
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(text = "Welcome", style = TypographyBold.headlineMedium)
            Text(
                text = "Let’s login for explore continues",
                modifier = Modifier.padding(top = Dimensions.padding15),
                style = TypographyBlack.bodyMedium,
                color = NeutralDarkGrey
            )
            Spacer(Modifier.height(Dimensions.spacing20))
            Image(
                painter = painterResource(com.asphalt.commonui.R.drawable.ic_app_icon),
                contentDescription = "",
                modifier = Modifier
                    .height(Dimensions.size60)
                    .width(Dimensions.padding50)

            )
            //Image(painter = painterResource(R.drawable.),contentDescription = null)
            Text(
                text = "adesso Riders's Club",
                modifier = Modifier.padding(top = Dimensions.spacing20),
                style = TypographyBlack.headlineSmall
            )

        }
        Spacer(modifier = Modifier.height(Dimensions.spacing40))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimensions.padding30, end = Dimensions.padding30),
        ) {
            Text(
                text = "Email or Phone Number",
                modifier = Modifier.padding(top = Dimensions.spacing20),
                style = TypographyBold.titleMedium,
                color = NeutralBlack
            )
            Spacer(modifier = Modifier.height(Dimensions.spacing10))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.padding50)
                    .background(
                        Color(0xFFF8F7FB), shape = RoundedCornerShape(Dimensions.padding10)
                    )
                    .then(
                        if (emailState.value.isEmpty()) {
                            Modifier.border(
                                width = Dimensions.padding1,
                                color = Color(0xFFF8F7FB),
                                shape = RoundedCornerShape(Dimensions.padding10)
                            )
                        } else {
                            Modifier.border(
                                width = Dimensions.padding1,
                                color = PrimaryDarkerLightB75,
                                shape = RoundedCornerShape(Dimensions.padding10)
                            )
                        }
                    ), verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = emailState.value,
                    onValueChange = { viewModel.updateEmailState(it)  },
                    //label = { Text("Enter your email") },
                    placeholder = { Text("Enter your email", style = Typography.bodySmall) },
                    textStyle = Typography.bodySmall,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
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
                            painter = painterResource(com.asphalt.commonui.R.drawable.ic_email_blue_icon), // Specify the icon (e.g., Email)
                            contentDescription = "Email icon", tint = Color.Unspecified

                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(com.asphalt.commonui.R.drawable.ic_tick_green), // Specify the icon (e.g., Email)
                            contentDescription = "Email icon", tint = Color.Unspecified
                            // Set the icon color
                        )
                    }

                )


            }
            Text(
                text = "Password",
                modifier = Modifier.padding(top = 20.dp),
                style = TypographyBold.titleMedium,
                color = NeutralBlack
            )
            Spacer(modifier = Modifier.height(Dimensions.spacing10))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.padding50)
                    .background(
                        Color(0xFFF8F7FB), shape = RoundedCornerShape(Dimensions.padding10)
                    )
                    .then(
                        if (text2.isEmpty()) {
                            Modifier.border(
                                width = Dimensions.padding1,
                                color = Color(0xFFF8F7FB),
                                shape = RoundedCornerShape(Dimensions.padding10)
                            )
                        } else {
                            Modifier.border(
                                width = Dimensions.padding1,
                                color = PrimaryDarkerLightB75,
                                shape = RoundedCornerShape(Dimensions.padding10)
                            )
                        }
                    ), verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = text2,
                    onValueChange = { text2 = it },
                    placeholder = {
                        Text(
                            "Enter your password", style = Typography.bodySmall
                        )
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),//if (passwordVisible) VisualTransformation.None
                    textStyle = Typography.bodySmall,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                            painter = painterResource(com.asphalt.commonui.R.drawable.ic_password_icon_blue), // Specify the icon (e.g., Email)
                            contentDescription = "Email icon", tint = Color.Unspecified

                        )
                    },
                    trailingIcon = {
                        if (!text2.isEmpty()) {
                            Icon(
                                painter = painterResource(com.asphalt.commonui.R.drawable.ic_eye_slash), // Specify the icon (e.g., Email)
                                contentDescription = "Email icon", tint = Color.Unspecified
                                // Set the icon color
                            )
                        }

                    })
                //Image()
            }
            Spacer(modifier = Modifier.height(Dimensions.size14))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        modifier = Modifier,
                        colors = CheckboxDefaults.colors(
                            checkedColor = PrimaryDarkerLightB75,
                            uncheckedColor = NeutralMidGrey,
                            checkmarkColor = Color.White
                        ),
                        checked = checked, onCheckedChange = { checked = it })
                    Text(text = "Keep me signed in", style = Typography.bodySmall)
                }
                Text(
                    text = "Forgot password",
                    style = TypographyBlack.bodySmall,
                    color = PrimaryDarkerLightB75
                )
            }
            Spacer(modifier = Modifier.height(Dimensions.spacing16))
            GradientButton(
                PrimaryDarkerLightB75, PrimaryDarkerLightB50, buttonText = "SIGN IN"
            ) {}
        }
        Spacer(modifier = Modifier.height(Dimensions.spacing20))
        //===
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimensions.padding30, end = Dimensions.padding30)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    color = NeutralMidGrey,
                    thickness = Dimensions.spacing1,
                    modifier = Modifier.weight(1f)

                )
                Text(
                    text = "You can connect with",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = Dimensions.padding8),
                    maxLines = 1, style = Typography.labelSmall, color = NeutralDarkGrey
                )
                Divider(
                    color = NeutralMidGrey,
                    thickness = Dimensions.spacing1,
                    modifier = Modifier.weight(1f)
                )

            }
            Spacer(modifier = Modifier.height(Dimensions.spacing16))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(com.asphalt.commonui.R.drawable.ic_facebook),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(Dimensions.spacing10))
                Image(
                    painter = painterResource(com.asphalt.commonui.R.drawable.ic_google),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(Dimensions.spacing10))
                Image(
                    painter = painterResource(com.asphalt.commonui.R.drawable.ic_apple),
                    contentDescription = ""
                )
            }
            Spacer(modifier = Modifier.height(Dimensions.spacing30))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Don’t have an account?", style = TypographyBlack.bodySmall)
                Text(
                    "Sign Up here",
                    style = TypographyBlack.bodySmall,
                    color = PrimaryDarkerLightB75
                )
            }
        }
    }


}

@Preview
@Composable
fun LoginPreview() {
    LoginScreen()
}

