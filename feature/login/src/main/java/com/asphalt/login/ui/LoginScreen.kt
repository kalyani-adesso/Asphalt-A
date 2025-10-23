package com.asphalt.login.ui


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.android.datastore.DataStoreManager
import com.asphalt.android.repository.AuthenticatorImpl
import com.asphalt.android.viewmodel.AuthViewModel
import com.asphalt.commonui.R
import com.asphalt.commonui.R.string
import com.asphalt.commonui.constants.PreferenceKeys
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightGray25
import com.asphalt.commonui.theme.NeutralMidGrey
import com.asphalt.commonui.theme.NeutralRed
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.LoaderPopup
import com.asphalt.login.viewmodel.LoginScreenViewModel
import org.koin.compose.currentKoinScope
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel = koinViewModel(),
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onDashboardNav: () -> Unit,
    dataStoreManager: DataStoreManager = currentKoinScope().get()
) {
    val context = LocalContext.current
    //var checked by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
//    val dataStoreManager : DataStoreManager = koinInject()

    var emailState = viewModel.emailTextState.collectAsState()
    var isValidEmail = viewModel.isEmailVaild.collectAsState()
    var passwordState = viewModel.passwordTextState.collectAsState()
    var validateState = viewModel.validateState.collectAsState()
    var isLoginSuccess = viewModel.isLoginSuccess.collectAsState()
    var showFailureMessage = viewModel.showFailureMessage.collectAsState()
    var showLoader = viewModel.showLoader.collectAsState()

    if (showLoader.value) {
        LoaderPopup()
    }
    LaunchedEffect(isLoginSuccess.value) {
        if (isLoginSuccess.value) {
            if (dataStoreManager.getBoolean(PreferenceKeys.IS_LOGGED_IN_BEFORE) ?: false) {
                onDashboardNav.invoke()
                viewModel.resetLogin()

            } else {
                onSignInClick.invoke()
                viewModel.resetLogin()
            }
        }
    }
    if (showFailureMessage.value) {
        Toast.makeText(context, stringResource(string.user_not_found), Toast.LENGTH_SHORT).show()
        viewModel.updateMessage(false)
    }
    val scrollState = rememberScrollState()
    Scaffold() { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = paddingValues.calculateBottomPadding(),
                    top = paddingValues.calculateTopPadding()
                )
                .verticalScroll(scrollState)
                .imePadding()
                .background(Color.White)
        ) {
            Spacer(modifier = Modifier.height(Dimensions.spacing80))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(text = stringResource(string.welcome), style = TypographyBold.headlineMedium)
                Text(
                    text = stringResource(string.login_explore_continue),
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
            Spacer(modifier = Modifier.height(Dimensions.spacing40))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Dimensions.padding, end = Dimensions.padding),
            ) {
                Text(
                    text = stringResource(string.email_phone),
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
                            NeutralLightGray25, shape = RoundedCornerShape(Dimensions.padding10)
                        )
                        .then(
                            if (emailState.value.isEmpty()) {
                                Modifier.border(
                                    width = Dimensions.padding1,
                                    color = NeutralLightGray25,
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
                        onValueChange = { viewModel.updateEmailState(it) },
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
                            if (isValidEmail.value) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_tick_green),
                                    contentDescription = "Email icon",
                                    tint = Color.Unspecified

                                )
                            }

                        }

                    )


                }
                if (validateState.value.isShowEmailError) {
                    Text(
                        text = stringResource(validateState.value.emailError),
                        Modifier.padding(top = Dimensions.size4),
                        style = Typography.bodySmall,
                        color = NeutralRed
                    )
                }

                Text(
                    text = stringResource(string.password),
                    modifier = Modifier.padding(top = Dimensions.size20),
                    style = TypographyBold.titleMedium,
                    color = NeutralBlack
                )
                Spacer(modifier = Modifier.height(Dimensions.spacing10))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimensions.padding50)
                        .background(
                            NeutralLightGray25, shape = RoundedCornerShape(Dimensions.padding10)
                        )
                        .then(
                            if (passwordState.value.isEmpty()) {
                                Modifier.border(
                                    width = Dimensions.padding1,
                                    color = NeutralLightGray25,
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
                        value = passwordState.value,
                        onValueChange = { viewModel.updatePassword(it) },
                        placeholder = {
                            Text(
                                text = stringResource(string.enter_password),
                                style = Typography.bodySmall,
                                color = NeutralDarkGrey

                            )
                        },
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                        visualTransformation = if (showPassword) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()

                        },
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
                                painter = painterResource(R.drawable.ic_password_icon_blue), // Specify the icon (e.g., Email)
                                contentDescription = "Email icon", tint = Color.Unspecified

                            )
                        },
                        trailingIcon = {
                            if (!passwordState.value.isEmpty()) {
                                Icon(
                                    painter = painterResource(if (showPassword) R.drawable.ic_eye_slash else R.drawable.ic_eye), // Specify the icon (e.g., Email)
                                    contentDescription = "Email icon",
                                    tint = Color.Unspecified,
                                    modifier = Modifier.clickable {
                                        showPassword = !showPassword
                                    }

                                )
                            }

                        })

                }
                if (validateState.value.isShowPasswordError) {
                    Text(
                        text = stringResource(validateState.value.passwordError),
                        Modifier.padding(top = Dimensions.size4),
                        style = Typography.bodySmall,
                        color = NeutralRed
                    )
                }

                Spacer(modifier = Modifier.height(Dimensions.spacing18))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    ) {
                        Box(
                            modifier = Modifier.size(20.dp) // smaller than 48.dp to reduce visual padding

                        ) {

                            Checkbox(
                                modifier = Modifier.padding(start = 0.dp, end = 0.dp),
                                colors = CheckboxDefaults.colors(
                                    checkedColor = PrimaryDarkerLightB75,
                                    uncheckedColor = NeutralMidGrey,
                                    checkmarkColor = Color.White
                                ),
                                checked = viewModel.isrememberMe.value,
                                onCheckedChange = { viewModel.isrememberMe.value = it })
                        }
                        Spacer(modifier = Modifier.width(Dimensions.padding8))
                        Text(
                            text = stringResource(string.keep_signed_in),
                            style = Typography.bodySmall,
                            color = NeutralDarkGrey
                        )
                    }
                    Text(
                        text = stringResource(string.forgot_password),
                        style = TypographyMedium.bodySmall,
                        color = PrimaryDarkerLightB75
                    )
                }
                Spacer(modifier = Modifier.height(Dimensions.spacing27))
                GradientButton(
                    PrimaryDarkerLightB75,
                    PrimaryDarkerLightB50,
                    buttonText = stringResource(string.sign_in)
                ) {

                    viewModel.callLogin()
                }
            }
            Spacer(modifier = Modifier.height(Dimensions.spacing20))

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
                        text = stringResource(string.you_can_connect),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = Dimensions.padding8),
                        maxLines = 1,
                        style = Typography.labelSmall,
                        color = NeutralDarkGrey
                    )
                    Divider(
                        color = NeutralMidGrey,
                        thickness = Dimensions.spacing1,
                        modifier = Modifier.weight(1f)
                    )

                }
                Spacer(modifier = Modifier.height(Dimensions.spacing16))
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_facebook), contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(Dimensions.spacing10))
                    Image(
                        painter = painterResource(R.drawable.ic_google), contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(Dimensions.spacing10))
                    Image(
                        painter = painterResource(R.drawable.ic_apple), contentDescription = ""
                    )
                }
                Spacer(modifier = Modifier.height(Dimensions.spacing30))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSignUpClick.invoke()
                        },
                    horizontalArrangement = Arrangement.Center,

                    ) {
                    Text(
                        text = stringResource(string.dont_have_account),
                        style = TypographyMedium.bodySmall
                    )
                    Spacer(Modifier.width(Dimensions.size3))
                    Text(
                        text = stringResource(string.sign_up_here),
                        style = TypographyMedium.bodySmall,
                        color = PrimaryDarkerLightB75
                    )
                }
            }
        }

    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun LoginPreview() {
    var modelauth: AuthViewModel = AuthViewModel(AuthenticatorImpl())


    var dataStoreManager = DataStoreManager(LocalContext.current)
    var viewModel: LoginScreenViewModel = LoginScreenViewModel(modelauth, dataStoreManager)

    LoginScreen(viewModel, onSignInClick = {

    }, onSignUpClick = {

    }, onDashboardNav = {

    }, dataStoreManager)

}

