package com.asphalt.registration

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NaturalGreen
import com.asphalt.commonui.theme.PrimaryBrighterLightW75
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.registration.navigation.NavigationRegistrationCodeKey
import com.asphalt.registration.viewmodel.RegistrationCodeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegistrationCodeScreen(
    id: String,
    modifier: Modifier = Modifier,
    onNavigateToRegistrationPassword: (String) -> Unit = {},
    onBackPressed: () -> Unit = {},
    registrationCodeViewModel: RegistrationCodeViewModel = koinViewModel()

    ) {

    LaunchedEffect(registrationCodeViewModel) {
        registrationCodeViewModel.handleNavigation(onBackPressed, onNavigateToRegistrationPassword)
    }

    Scaffold(modifier = modifier
        .fillMaxSize()
        .background(color = Color.White))
    { paddingValues ->
        Column(
            modifier = modifier.padding(paddingValues),
        ) {
            RegistrationHeader(registrationCodeViewModel)
        }
    }
}

@Composable
private fun RegistrationHeader(
    viewModel: RegistrationCodeViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.padding, vertical = Dimensions.padding50),
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
            modifier = Modifier.padding(vertical = Dimensions.padding32),
            text = "adesso Rider's Club",
            style = TypographyMedium.bodyMedium
        )

        Image(painter = painterResource(
            id = com.asphalt.commonui.R.drawable.ic_email),
            contentDescription = "App Logo"
        )

        Registration("",viewModel)
    }
}

@Composable
fun Registration(
    emailPhone: String,
    viewModel: RegistrationCodeViewModel) {

    val validEmail by remember { mutableStateOf(false) }
    var email by rememberSaveable { mutableStateOf(emailPhone) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.padding)
    ) {
        Text(
            modifier = Modifier.padding(bottom = Dimensions.padding),
            text = "Email or Phone Number",
            style = TypographyMedium.titleSmall
        )

        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            placeholder = { Text(text = "Enter email or phone number") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryDarkerLightB75,
                unfocusedBorderColor = PrimaryDarkerLightB75
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.padding32),
            shape = RoundedCornerShape(size = Dimensions.spacing8),
            leadingIcon = {
                Icon(painter = painterResource(
                    id = R.drawable.shape),
                    contentDescription = "Email Icon",
                    tint = PrimaryDarkerLightB75,
                )
            },
            trailingIcon = {
                if (validEmail) {
                    Icon(painter = painterResource(
                        id = R.drawable.ic_correct),
                        contentDescription = "Success",
                        tint = NaturalGreen,
                    )
                }
            }
        )

        GradientButton(
            startColor = PrimaryBrighterLightW75,
            endColor = PrimaryDarkerLightB50,
            onClick = { viewModel.onContinueClick(email)},
            buttonText = "CONTINUE"
        )
    }
}

@Preview
@Composable
fun RegistrationScreenPreview() {

    MaterialTheme {
        RegistrationCodeScreen(id = "123")
    }
}