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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.PrimaryBrighterLightW75
import com.asphalt.commonui.theme.PrimaryDarkerLightB50
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.GradientButton

@Composable
fun RegistrationDetailsScreen(modifier: Modifier = Modifier) {

    Scaffold(modifier = modifier
        .fillMaxSize()
        .background(color = Color.White))
    { paddingValues ->
        Column(
            modifier = modifier.padding(paddingValues),
        ) {
            RegistrationDetailsHeader()
        }
    }
}

@Composable
private fun RegistrationDetailsHeader() {

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
        RegistrationFeilds()
    }
}


@Composable
fun RegistrationFeilds(modifier: Modifier = Modifier) {

    Column(
        modifier= Modifier.fillMaxSize()
            .padding(Dimensions.padding)
    ) {
        Text(
            modifier = Modifier.padding(bottom = Dimensions.padding8),
            text = "First Name",
            style = TypographyMedium.titleSmall
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text(text = "First Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.padding),
            leadingIcon = {
                Icon(painter = painterResource(
                    id = R.drawable.shape),
                    contentDescription = "Email Icon",
                    tint = Color.Blue,
                )
            }
        )

        Text(
            modifier = Modifier.padding(bottom = Dimensions.padding8),
            text = "Last Name",
            style = TypographyMedium.titleSmall
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text(text = "Last Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.padding),
            leadingIcon = {
                Icon(painter = painterResource(
                    id = R.drawable.shape),
                    contentDescription = "Email Icon",
                    tint = Color.Blue,
                )
            }
        )

        Text(
            modifier = Modifier.padding(bottom = Dimensions.padding8),
            text = "Password",
            style = TypographyMedium.titleSmall
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text(text = "Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.padding),
            leadingIcon = {
                Icon(painter = painterResource(
                    id = R.drawable.shape),
                    contentDescription = "Email Icon",
                    tint = Color.Blue,
                )
            }
        )

        Text(
            modifier = Modifier.padding(bottom = Dimensions.padding8),
            text = "Confirm Password",
            style = TypographyMedium.titleSmall
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text(text = "Confirm Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.padding),
            leadingIcon = {
                Icon(painter = painterResource(
                    id = R.drawable.shape),
                    contentDescription = "Email Icon",
                    tint = Color.Blue,
                )
            }
        )
        Spacer(modifier = Modifier.height(30.dp))
        GradientButton(
            startColor = PrimaryBrighterLightW75,
            endColor = PrimaryDarkerLightB50,
            onClick = {},
            buttonText = "CREATE ACCOUNT"
        )


    }

}

@Preview
@Composable
fun RegistrationDetailsScreenPreview() {

    MaterialTheme {
        RegistrationDetailsScreen()
    }

}

