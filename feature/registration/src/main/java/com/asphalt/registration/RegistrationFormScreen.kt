package com.asphalt.registration

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.asphalt.android.model.User
import com.asphalt.android.viewmodel.AuthViewModel
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.Dimensions.padding10
import com.asphalt.commonui.theme.PrimaryBrighterLightW75
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.ui.LoaderPopup
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.registration.viewmodel.RegistrationDetailsViewModel
import com.asphalt.registration.viewmodel.SignUpUiEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun RegistrationForm(
    viewModel: RegistrationDetailsViewModel,
    navigateToLogin:()->Unit) {

    val state by viewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var showLoader = viewModel.showLoader.collectAsState()

    if (showLoader.value) {
        LoaderPopup()
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when(event) {
                is SignUpUiEvent.Success -> {
                    delay(1500)
                    navigateToLogin.invoke()
                    viewModel.onEvent(SignUpUiEvent.clearMessage)
                    Toast.makeText(context, event.success, Toast.LENGTH_SHORT).show()

                }
                is SignUpUiEvent.Error -> {
                    viewModel.onEvent(SignUpUiEvent.clearMessage)
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                else -> null
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.padding)
    ) {
        Text(
            modifier = Modifier.padding(bottom = Dimensions.padding8),
            text = "Full Name",
            style = TypographyMedium.titleSmall
        )
        OutlinedTextField(
            value = state.fullName,
            onValueChange = { viewModel.onEvent(SignUpUiEvent.FullNameChagned(it))},
            placeholder = { Text(text = "Full Name") },
            isError = state.fullNameError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryDarkerLightB75,
                unfocusedBorderColor = PrimaryDarkerLightB75
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.padding),
            shape = RoundedCornerShape(padding10),
            leadingIcon = {
                Icon(
                    painter = painterResource(
                        id = R.drawable.shape
                    ),
                    contentDescription = "Email Icon",
                    tint = PrimaryDarkerLightB75,
                )
            }
        )
        state.fullNameError?.message?.let { Text(it, modifier = Modifier.fillMaxWidth(),
            color =  Color.Red)}

        Text(
            modifier = Modifier.padding(bottom = Dimensions.padding8),
            text = "Email Id",
            style = TypographyMedium.titleSmall
        )
        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.onEvent(SignUpUiEvent.EmailChagned(it))},
            placeholder = { Text(text = "Email Id") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryDarkerLightB75,
                unfocusedBorderColor = PrimaryDarkerLightB75
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email,imeAction = ImeAction.Next),
            maxLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.padding),
            shape = RoundedCornerShape(padding10),
            leadingIcon = {
                Icon(
                    painter = painterResource(
                        id = R.drawable.shape
                    ),
                    contentDescription = "Email Icon",
                    tint = PrimaryDarkerLightB75,
                )
            }
        )
        state.emailError?.message?.let { Text(it, modifier = Modifier.fillMaxWidth(),
            color =  Color.Red)}

        Text(
            modifier = Modifier.padding(bottom = Dimensions.padding8),
            text = "Password",
            style = TypographyMedium.titleSmall
        )
        OutlinedTextField(
            value = state.password,
            onValueChange = {viewModel.onEvent(SignUpUiEvent.PasswordChagned(it))},
            placeholder = { Text(text = "Password") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryDarkerLightB75,
                unfocusedBorderColor = PrimaryDarkerLightB75
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,imeAction = ImeAction.Next),
            maxLines = 1,
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.padding),
            shape = RoundedCornerShape(padding10),
            leadingIcon = {
                Icon(
                    painter = painterResource(
                        id = com.asphalt.commonui.R.drawable.ic_lock
                    ),
                    contentDescription = "Email Icon",
                    tint = PrimaryDarkerLightB75,
                )
            },
            trailingIcon = {
                when {
                    state.password.isNotEmpty() -> {
                        val image = if (passwordVisible) {
                            R.drawable.ic_eye_closed
                        } else {
                            R.drawable.ic_eyy_open
                        }
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(id = image),
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    }
                }
            }
        )
        state.passwordError?.message?.let { Text(text = it, modifier = Modifier.fillMaxWidth(),
            color =  Color.Red)}
        Text(
            modifier = Modifier.padding(bottom = Dimensions.padding8),
            text = "Confirm Password",
            style = TypographyMedium.titleSmall
        )
        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = { viewModel.onEvent(SignUpUiEvent.CofirmPasswordChagned(it)) },
            placeholder = { Text(text = "Confirm Password") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryDarkerLightB75,
                unfocusedBorderColor = PrimaryDarkerLightB75
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,imeAction = ImeAction.Next),
            maxLines = 1,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.padding),
            shape = RoundedCornerShape(padding10),
            leadingIcon = {
                Icon(
                    painter = painterResource(
                        id = com.asphalt.commonui.R.drawable.ic_lock
                    ),
                    contentDescription = "Email Icon",
                    tint = PrimaryDarkerLightB75,
                )
            },
            trailingIcon = {
                when {
                    state.confirmPassword.isNotEmpty() -> {
                        val image = if (confirmPasswordVisible) {
                            R.drawable.ic_eye_closed
                        } else {
                            R.drawable.ic_eyy_open
                        }
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                painter = painterResource(id = image),
                                contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                            )
                        }
                    }
                }
            }
        )
        state.confirmPasswordError?.message?.let { Text(text = it, modifier = Modifier.fillMaxWidth(),
            color =  Color.Red)}

        Spacer(modifier = Modifier.height(height = 30.dp))

            GradientButton(
                startColor = PrimaryBrighterLightW75,
                endColor = PrimaryDarkerLightB50,
                onClick = {
                    viewModel.onEvent(SignUpUiEvent.Submit)
                }
            )
            { ComposeUtils.DefaultButtonContent(buttonText = "CREATE ACCOUNT") }
        }


    Spacer(modifier = Modifier.height(height = 20.dp))
}