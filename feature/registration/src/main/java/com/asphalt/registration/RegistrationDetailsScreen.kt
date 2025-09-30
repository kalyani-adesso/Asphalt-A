package com.asphalt.registration

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
            .padding(horizontal = Dimensions.padding, vertical = Dimensions.padding50),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(vertical = Dimensions.padding),
            text = "Create Your Account",
            style = TypographyMedium.labelLarge
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
            style = TypographyMedium.labelLarge
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
            placeholder = { Text(text = "Enter email or phone number") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.padding32),
            leadingIcon = {
                Icon(painter = painterResource(
                    id = R.drawable.shape),
                    contentDescription = "Email Icon",
                    tint = Color.Blue,
                )
            }
        )

        RegistrationFooter("")

    }
    
}
@Composable
fun RegistrationFooter( emailPhone: String) {

    var emailOrPhone by remember { mutableStateOf(value = emailPhone) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier.padding(bottom = Dimensions.padding),
            text = "Email or Phone Number",
            style = TypographyMedium.titleSmall
        )

        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text(text = "Enter email or phone number") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.padding32),
            leadingIcon = {
                Icon(painter = painterResource(
                    id = R.drawable.shape),
                    contentDescription = "Email Icon",
                    tint = Color.Blue,
                )
            },
            trailingIcon = {
                Icon(painter = painterResource(
                    id = R.drawable.ic_correct),
                    contentDescription = "Email Icon",
                    tint = Color.Green,
                )
            }
        )

        GradientButton(
            startColor = PrimaryBrighterLightW75,
            endColor = PrimaryDarkerLightB50,
            onClick = {},
            buttonText = "CONTINUE"
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

