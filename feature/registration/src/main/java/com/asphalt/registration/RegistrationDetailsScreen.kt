package com.asphalt.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.android.viewmodel.AuthViewModel
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.registration.viewmodel.RegistrationDetailsViewModel
import com.asphalt.registration.viewmodel.SignUpUiState
import org.koin.compose.viewmodel.koinViewModel
import org.w3c.dom.Text

@Composable
fun RegistrationDetailsScreen(
    registrationDetailsVM : RegistrationDetailsViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}) {

    LaunchedEffect(registrationDetailsVM) {
        registrationDetailsVM.handleNavigation(onBackPressed, onNavigateToLogin)
    }

    val uiState = registrationDetailsVM.signupState.collectAsState()

    Surface(
        modifier = modifier
            .fillMaxSize()
            .focusable(false)
    ) {
        BoxWithConstraints(modifier = modifier.fillMaxSize()) {
            val contentMaxWidth = if (maxWidth < 480.dp) maxWidth else 480.dp

            Scaffold(modifier = modifier
                .fillMaxSize()
                .background(color = Color.White))
            { paddingValues ->
                Column(
                    modifier = modifier.padding(paddingValues)
                        .fillMaxWidth()
                        .width(contentMaxWidth).verticalScroll(state = rememberScrollState()),
                ) {
                    RegistrationDetailsHeader(registrationDetailsVM)
                    when(val state  = uiState) {
                        is SignUpUiState.Loading -> {
                            CircularProgressIndicator()
                        }
                        is SignUpUiState.Success -> {
                            RegistrationDetailsHeader(registrationDetailsVM)
                        }
                        is SignUpUiState.Error -> {
                            Text(text = state.errorMessage.errorTitle)
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun RegistrationDetailsHeader(
    viewModel: RegistrationDetailsViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
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
        RegistrationForm(viewModel)
    }
}

@Preview
@Composable
fun RegistrationDetailsScreenPreview() {

    MaterialTheme {
        RegistrationDetailsScreen()
    }
}