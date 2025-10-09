package com.asphalt.registration

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.PrimaryBrighterLightW75
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.registration.viewmodel.RegistrationPasswordViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegistrationPasswordScreen(
    id: String,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onNavigateToRegistrationDetails: (String) -> Unit = {},
    viewModel: RegistrationPasswordViewModel = koinViewModel()
) {

    LaunchedEffect(viewModel) {
        viewModel.handleNavigation(onBackPressed, onNavigateToRegistrationDetails)
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    )
    { paddingValues ->
        Column(
            modifier = modifier.padding(paddingValues),
        ) {
            PasswordHeader(viewModel)
        }
    }
}

@Composable
private fun PasswordHeader(
    viewModel: RegistrationPasswordViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = Dimensions.padding,
                vertical = Dimensions.padding50
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Image(
            painter = painterResource(
                id = com.asphalt.commonui.R.drawable.ic_password
            ),
            contentDescription = "App Logo"
        )

        Text(
            modifier = Modifier.padding(vertical = Dimensions.padding),
            text = "Confirm Your Email",
            style = TypographyMedium.headlineLarge
        )

        Text(
            modifier = Modifier.padding(bottom = Dimensions.padding40),
            text = "We’ve sent 5 digits verification code \n to hello@abc.com",
            style = Typography.titleSmall
        )

        Password(password = "", remainingMs = 0, viewModel)
    }
}

@Composable
fun Password(
    password: String,
    remainingMs: Long,
    viewModel: RegistrationPasswordViewModel
) {

    var password by remember { mutableStateOf(value = password) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = Dimensions.padding)
    ) {
        Text(
            modifier = Modifier.padding(bottom = Dimensions.padding),
            text = "Enter Verification Code",
            style = TypographyMedium.titleSmall
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text(text = "Verification Code") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.padding32),
            leadingIcon = {
                Icon(
                    painter = painterResource(
                        id = R.drawable.shape
                    ),
                    contentDescription = "Email Icon",
                    tint = PrimaryDarkerLightB75
                )
            },
            trailingIcon = {
                Text(
                    text = "Resend in ${formatMillis(remainingMs)}",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.padding(8.dp)
                )
            }
        )

        GradientButton(
            startColor = PrimaryBrighterLightW75,
            endColor = PrimaryDarkerLightB50,
            onClick = { viewModel.onContinueClick("") },
        ) {
            ComposeUtils.DefaultButtonContent("VERIFY ACCOUNT")
        }
    }
}

@SuppressLint("DefaultLocale")
fun formatMillis(ms: Long): String {
    val totalSeconds = (ms / 1000).toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

@Preview
@Composable
fun RegistrationPasswordScreenPreview() {

    MaterialTheme {
        RegistrationPasswordScreen(id = "11")
    }

}