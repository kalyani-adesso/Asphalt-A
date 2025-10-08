package com.asphalt.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asphalt.android.model.User
import com.asphalt.android.viewmodel.AuthViewModel
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.PrimaryBrighterLightW75
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.GradientButton
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegistrationDetailsScreen(
    authViewModel: AuthViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
    onNavigateToDashboard: () -> Unit = {}) {

    Scaffold(modifier = modifier
        .fillMaxSize()
        .background(color = Color.White))
    { paddingValues ->
        Column(
            modifier = modifier.padding(paddingValues),
        ) {
            RegistrationDetailsHeader(authViewModel)
        }
    }
}

@Composable
private fun RegistrationDetailsHeader(
    viewModel: AuthViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.padding, vertical = Dimensions.padding32),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(vertical = Dimensions.padding),
            text = "Create Your Account",
            style = TypographyMedium.bodyLarge
        )
        Text(
            modifier = Modifier.padding(bottom = Dimensions.padding24),
            text = "Create your account to get started",
            style = Typography.titleSmall
        )
        Image(painter = painterResource(
            id = com.asphalt.commonui.R.drawable.ic_app_icon),
            contentDescription = "App Logo"
        )
        Text(
            modifier = Modifier.padding(vertical = Dimensions.spacing20),
            text = "adesso Rider's Club",
            style = TypographyMedium.bodyMedium
        )
        RegistrationFeilds(viewModel)
    }
}


@Composable
fun RegistrationFeilds(
    viewModel: AuthViewModel) {

    val coroutineScope = rememberCoroutineScope()

    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf<String?>(value = null) }

    Column(
        modifier= Modifier.fillMaxSize()
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.padding),
            leadingIcon = {
                Icon(painter = painterResource(
                    id = com.asphalt.commonui.R.drawable.ic_lock),
                    contentDescription = "Email Icon",
                    tint = PrimaryDarkerLightB75,
                )
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.padding),
            leadingIcon = {
                Icon(painter = painterResource(
                    id = com.asphalt.commonui.R.drawable.ic_lock),
                    contentDescription = "Email Icon",
                    tint = PrimaryDarkerLightB75,
                )
            }
        )
        Spacer(modifier = Modifier.height(30.dp))
        GradientButton(
            startColor = PrimaryBrighterLightW75,
            endColor = PrimaryDarkerLightB50,
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    val user = User(email = email,password=password,"","")
                    loading = true
                    resultMessage = ""

                    coroutineScope.launch {
                            // launch coroutine in composble scope
                            val result = viewModel.SignUp(user)
                            loading = false
                            resultMessage = result.fold(
                                onSuccess = {"Success: $it"},
                                onFailure = {"Error: ${it.message}"}
                            )
                    }
                }
            },
            buttonText = "CREATE ACCOUNT"
        )
    }
    Spacer(modifier = Modifier.height(20.dp))
    resultMessage?.let {
        Text(text = it,
            color = if (resultMessage!!.startsWith("Error")) Color.Red else Color.Green)
    }
}

@Preview
@Composable
fun RegistrationDetailsScreenPreview() {

    MaterialTheme {
        RegistrationDetailsScreen()
    }
}