package com.asphalt.commonui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// not used yet, but can be used in the future
val DarkColorScheme = darkColorScheme(
    // primary = Purple80,
    // secondary = PurpleGrey80,
    // tertiary = Pink80
)

val LightColorScheme = lightColorScheme(
    primary = PrimaryDarkerLightB50,
    onPrimary = NeutralWhite,
    secondary = PrimaryBrighterLightW25,
    tertiary = PrimaryLight,
    background = NeutralWhite,
    error = Color.Green

    /* Other default colors to override
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
     */
)