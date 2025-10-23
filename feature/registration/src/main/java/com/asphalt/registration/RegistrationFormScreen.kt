package com.asphalt.registration

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.asphalt.android.model.User
import com.asphalt.android.viewmodel.AuthViewModel
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.PrimaryBrighterLightW75
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.registration.viewmodel.RegistrationDetailsViewModel
import kotlinx.coroutines.launch


@Composable
fun RegistrationForm(
    viewModel: RegistrationDetailsViewModel) {

    val coroutineScope = rememberCoroutineScope()

    var userName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf<String?>(value = null) }

    Column(
        modifier= Modifier
            .fillMaxSize()
            .padding(Dimensions.padding)
    ) {
        Text(
            modifier = Modifier.padding(bottom = Dimensions.padding8),
            text = "Full Name",
            style = TypographyMedium.titleSmall
        )
        OutlinedTextField(
            value = userName,
            onValueChange = {userName = it},
            placeholder = { Text(text = "Full Name") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryDarkerLightB75,
                unfocusedBorderColor = PrimaryDarkerLightB75
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.padding),
            leadingIcon = {
                Icon(painter = painterResource(
                    id = R.drawable.shape),
                    contentDescription = "Email Icon",
                    tint = PrimaryDarkerLightB75,
                )
            }
        )

        Text(
            modifier = Modifier.padding(bottom = Dimensions.padding8),
            text = "Email Id",
            style = TypographyMedium.titleSmall
        )
        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            placeholder = { Text(text = "Email Id") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryDarkerLightB75,
                unfocusedBorderColor = PrimaryDarkerLightB75
            ),
            maxLines = 2,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.padding),
            leadingIcon = {
                Icon(painter = painterResource(
                    id = R.drawable.shape),
                    contentDescription = "Email Icon",
                    tint = PrimaryDarkerLightB75,
                )
            }
        )

        Text(
            modifier = Modifier.padding(bottom = Dimensions.padding8),
            text = "Password",
            style = TypographyMedium.titleSmall
        )
        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            placeholder = { Text(text = "Password") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryDarkerLightB75,
                unfocusedBorderColor = PrimaryDarkerLightB75
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.padding),
            leadingIcon = {
                Icon(painter = painterResource(
                    id = com.asphalt.commonui.R.drawable.ic_lock),
                    contentDescription = "Email Icon",
                    tint = PrimaryDarkerLightB75,
                )
            },
            trailingIcon = {
                when {
                    password.isNotEmpty() -> {
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

        Text(
            modifier = Modifier.padding(bottom = Dimensions.padding8),
            text = "Confirm Password",
            style = TypographyMedium.titleSmall
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {confirmPassword = it},
            placeholder = { Text(text = "Confirm Password") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryDarkerLightB75,
                unfocusedBorderColor = PrimaryDarkerLightB75
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.padding),
            leadingIcon = {
                Icon(painter = painterResource(
                    id = com.asphalt.commonui.R.drawable.ic_lock),
                    contentDescription = "Email Icon",
                    tint = PrimaryDarkerLightB75,
                )
            },
            trailingIcon = {
                when {
                    confirmPassword.isNotEmpty() -> {
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
        Spacer(modifier = Modifier.height(height = 30.dp))
        GradientButton(
            startColor = PrimaryBrighterLightW75,
            endColor = PrimaryDarkerLightB50,
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    val user = User(email = email,password=password, name = userName,"")
                    loading = true
                    resultMessage = ""

                    coroutineScope.launch {
                        // launch coroutine in composble scope
                        val result = viewModel.onSignupClick(user)
                        Log.d("TAG", "RegistrationForm: result $result")
//                        loading = false
//                        resultMessage = result.fold(
//                            onSuccess = {"Success: $it"},
//                            onFailure = {"Error: ${it.message}"}
//                        )
                    }
                }
            }
        )
        { ComposeUtils.DefaultButtonContent(buttonText = "CREATE ACCOUNT") }
    }
    Spacer(modifier = Modifier.height(20.dp))
}