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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.registration.viewmodel.RegistrationDetailsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegistrationDetailsScreen(
    authViewModel: RegistrationDetailsViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
    onNavigateToLogin: () -> Unit = {}
) {

    Surface(
        modifier = modifier
            .fillMaxSize()
            .focusable(false)
    ) {
        BoxWithConstraints(modifier = modifier.fillMaxSize()) {
            val contentMaxWidth = if (maxWidth < 480.dp) maxWidth else 480.dp

            Scaffold(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = Color.White)
            )
            { paddingValues ->
                Column(
                    modifier = modifier
                        .padding(paddingValues)
                        .fillMaxWidth()
                        .width(contentMaxWidth)
                        .verticalScroll(rememberScrollState()),
                ) {
                    RegistrationDetailsHeader(authViewModel, { onNavigateToLogin.invoke() })
                }
            }
        }
    }

}

@Composable
private fun RegistrationDetailsHeader(
    viewModel: RegistrationDetailsViewModel,
    onNavigateToLogin: () -> Unit
) {

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
        Image(
            painter = painterResource(
                id = com.asphalt.commonui.R.drawable.ic_app_icon
            ),
            contentDescription = "App Logo"
        )
        Text(
            modifier = Modifier.padding(vertical = Dimensions.spacing20),
            text = "adesso Rider's Club",
            style = TypographyMedium.bodyMedium
        )
        RegistrationForm(viewModel, navigateToLogin = {
            onNavigateToLogin.invoke()
        })
    }
}

@Preview
@Composable
fun RegistrationDetailsScreenPreview() {

    MaterialTheme {
        RegistrationDetailsScreen()
    }
}