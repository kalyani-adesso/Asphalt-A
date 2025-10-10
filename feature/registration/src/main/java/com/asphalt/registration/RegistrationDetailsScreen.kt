package com.asphalt.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.asphalt.android.model.User
import com.asphalt.android.viewmodel.AuthViewModel
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.PrimaryBrighterLightW75
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.utils.ComposeUtils
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegistrationDetailsScreen(
    authViewModel: AuthViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
    onNavigateToDashboard: () -> Unit = {}) {

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
                        .width(contentMaxWidth),
                ) {
                    RegistrationDetailsHeader(authViewModel)
                }
            }
        }
    }

}

@Composable
private fun RegistrationDetailsHeader(
    viewModel: AuthViewModel) {

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