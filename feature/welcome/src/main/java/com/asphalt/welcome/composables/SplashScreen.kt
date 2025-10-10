package com.asphalt.welcome.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.asphalt.commonui.R
import com.asphalt.commonui.R.drawable
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.constants.Constants
import com.asphalt.welcome.viewmodels.WelcomeViewModel
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreen(onNavigateToLogin: () -> Unit, onNavigateToWelcome: () -> Unit) {
    val welcomeViewModel: WelcomeViewModel = koinViewModel()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrimaryDarkerLightB75),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(drawable.ic_adesso_logo), null)
        Spacer(modifier = Modifier.height(Dimensions.size60))
        Image(painter = painterResource(drawable.ic_riders_club_logo), null)
        Spacer(modifier = Modifier.height(Dimensions.size20))
        Text(
            stringResource(R.string.adesso_riders_club),
            color = NeutralWhite,
            fontSize = Dimensions.textSize26,
            style = TypographyBold.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
    val isGetStartedDone by welcomeViewModel.isGetStartedDone.collectAsState(initial = false)
    LaunchedEffect(Unit) {
        delay(Constants.SPLASH_TIMER)
        if (isGetStartedDone)
            onNavigateToLogin()
        else
            onNavigateToWelcome()
    }

}