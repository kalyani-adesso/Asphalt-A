package com.asphalt.commonui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.asphalt.commonui.R

val Typography = Typography(
    bodyLarge = TextStyle(
        fontSize = 18.sp,
        lineHeight = 27.sp,
        fontFamily = FontFamily(Font(R.font.klavika_regular)),
        fontWeight = FontWeight.Normal,
        color = NeutralBlackGrey,
        letterSpacing = 0.04.sp,
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontFamily = FontFamily(Font(R.font.klavika_regular)),
        fontWeight = FontWeight.Normal,
        color = NeutralBlackGrey,
        letterSpacing = 0.03.sp,
    ),
    bodySmall = TextStyle(
        fontSize = 14.sp,
        lineHeight = 21.sp,
        fontFamily = FontFamily(Font(R.font.klavika_regular)),
        fontWeight = FontWeight.Normal,
        color = NeutralBlackGrey,
        letterSpacing = 0.03.sp,
    ),
    displayLarge = TextStyle(
        fontSize = 57.sp,
        lineHeight = 85.5.sp,
        fontFamily = FontFamily(Font(R.font.klavika_regular)),
        fontWeight = FontWeight.Normal,
        color = NeutralBlackGrey,
        letterSpacing = 0.11.sp
    ),
    displayMedium = TextStyle(
        fontSize = 48.sp,
        lineHeight = 72.sp,
        fontFamily = FontFamily(Font(R.font.klavika_regular)),
        fontWeight = FontWeight.Normal,
        color = NeutralBlackGrey,
        letterSpacing = 0.1.sp
    ),
    displaySmall = TextStyle(
        fontSize = 40.sp,
        lineHeight = 60.sp,
        fontFamily = FontFamily(Font(R.font.klavika_regular)),
        fontWeight = FontWeight.Normal,
        color = NeutralBlackGrey,
        letterSpacing = 0.08.sp
    ),
    headlineLarge = TextStyle(
        fontSize = 33.sp,
        lineHeight = 49.5.sp,
        fontFamily = FontFamily(Font(R.font.klavika_regular)),
        fontWeight = FontWeight.Normal,
        color = NeutralBlackGrey,
        letterSpacing = 0.07.sp
    ),
    headlineMedium = TextStyle(
        fontSize = 28.sp,
        lineHeight = 42.sp,
        fontFamily = FontFamily(Font(R.font.klavika_regular)),
        fontWeight = FontWeight.Normal,
        color = NeutralBlackGrey,
        letterSpacing = 0.06.sp,
    ),
    headlineSmall = TextStyle(
        fontSize = 23.sp,
        lineHeight = 34.5.sp,
        fontFamily = FontFamily(Font(R.font.klavika_regular)),
        fontWeight = FontWeight.Normal,
        color = NeutralBlackGrey,
        letterSpacing = 0.05.sp,
    ),
    titleLarge = TextStyle(
        fontSize = 20.sp,
        lineHeight = 30.sp,
        fontFamily = FontFamily(Font(R.font.klavika_regular)),
        fontWeight = FontWeight.Normal,
        color = NeutralBlackGrey,
        letterSpacing = 0.04.sp,
    ),
    titleMedium = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontFamily = FontFamily(Font(R.font.klavika_regular)),
        fontWeight = FontWeight.Normal,
        color = NeutralBlackGrey,
        letterSpacing = 0.03.sp,
    ),
    titleSmall = TextStyle(
        fontSize = 14.sp,
        lineHeight = 21.sp,
        fontFamily = FontFamily(Font(R.font.klavika_regular)),
        fontWeight = FontWeight.Normal,
        color = NeutralBlackGrey,
        letterSpacing = 0.03.sp,
    ),
    labelLarge = TextStyle(
        fontSize = 18.sp,
        lineHeight = 27.sp,
        fontFamily = FontFamily(Font(R.font.klavika_regular)),
        fontWeight = FontWeight.Normal,
        color = NeutralBlackGrey,
        letterSpacing = 0.04.sp,
    ),
    labelMedium = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontFamily = FontFamily(Font(R.font.klavika_regular)),
        fontWeight = FontWeight.Normal,
        color = NeutralBlackGrey,
        letterSpacing = 0.03.sp,
    ),
    labelSmall = TextStyle(
        fontSize = 14.sp,
        lineHeight = 21.sp,
        fontFamily = FontFamily(Font(R.font.klavika_regular)),
        fontWeight = FontWeight.Normal,
        color = NeutralBlackGrey,
        letterSpacing = 0.03.sp,
    )
)

val TypographyBold = Typography(
    bodyLarge = Typography.bodyLarge.copy(
        fontFamily = FontFamily(Font(R.font.klavika_bold)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.09.sp
    ),
    bodyMedium = Typography.bodyMedium.copy(
        fontFamily = FontFamily(Font(R.font.klavika_bold)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.08.sp
    ),
    bodySmall = Typography.bodySmall.copy(
        fontFamily = FontFamily(Font(R.font.klavika_bold)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.07.sp
    ),
    displayLarge = Typography.displayLarge.copy(
        fontFamily = FontFamily(Font(R.font.klavika_bold)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.29.sp
    ),
    displayMedium = Typography.displayMedium.copy(
        fontFamily = FontFamily(Font(R.font.klavika_bold)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.24.sp
    ),
    displaySmall = Typography.displaySmall.copy(
        fontFamily = FontFamily(Font(R.font.klavika_bold)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.2.sp
    ),
    headlineLarge = Typography.headlineLarge.copy(
        fontFamily = FontFamily(Font(R.font.klavika_bold)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.17.sp
    ),
    headlineMedium = Typography.headlineMedium.copy(
        fontFamily = FontFamily(Font(R.font.klavika_bold)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.14.sp
    ),
    headlineSmall = Typography.headlineSmall.copy(
        fontFamily = FontFamily(Font(R.font.klavika_bold)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.12.sp
    ),
    titleLarge = Typography.titleLarge.copy(
        fontFamily = FontFamily(Font(R.font.klavika_bold)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.1.sp
    ),
    titleMedium = Typography.titleMedium.copy(
        fontFamily = FontFamily(Font(R.font.klavika_bold)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.08.sp
    ),
    titleSmall = Typography.titleSmall.copy(
        fontFamily = FontFamily(Font(R.font.klavika_bold)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.07.sp
    ),
    labelLarge = Typography.labelLarge.copy(
        fontFamily = FontFamily(Font(R.font.klavika_bold)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.09.sp
    ),
    labelMedium = Typography.labelMedium.copy(
        fontFamily = FontFamily(Font(R.font.klavika_bold)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.08.sp
    ),
    labelSmall = Typography.labelSmall.copy(
        fontFamily = FontFamily(Font(R.font.klavika_bold)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.07.sp
    )
)

val TypographyBlack = Typography(
    bodyLarge = Typography.bodyLarge.copy(
        fontFamily = FontFamily(Font(R.font.klavika_medium)),
        fontWeight = FontWeight.Black,
        letterSpacing = 0.18.sp
    ),
    bodyMedium = Typography.bodyMedium.copy(
        fontFamily = FontFamily(Font(R.font.klavika_medium)),
        fontWeight = FontWeight.Black,
        letterSpacing = 0.16.sp
    ),
    bodySmall = Typography.bodySmall.copy(
        fontFamily = FontFamily(Font(R.font.klavika_medium)),
        fontWeight = FontWeight.Black,
        letterSpacing = 0.14.sp
    ),
    displayLarge = Typography.displayLarge.copy(
        fontFamily = FontFamily(Font(R.font.klavika_medium)),
        fontWeight = FontWeight.Black,
        letterSpacing = 0.57.sp
    ),
    displayMedium = Typography.displayMedium.copy(
        fontFamily = FontFamily(Font(R.font.klavika_medium)),
        fontWeight = FontWeight.Black,
        letterSpacing = 0.48.sp
    ),
    displaySmall = Typography.displaySmall.copy(
        fontFamily = FontFamily(Font(R.font.klavika_medium)),
        fontWeight = FontWeight.Black,
        letterSpacing = 0.4.sp
    ),
    headlineLarge = Typography.headlineLarge.copy(
        fontFamily = FontFamily(Font(R.font.klavika_medium)),
        fontWeight = FontWeight.Black,
        letterSpacing = 0.33.sp
    ),
    headlineMedium = Typography.headlineMedium.copy(
        fontFamily = FontFamily(Font(R.font.klavika_medium)),
        fontWeight = FontWeight.Black,
        letterSpacing = 0.28.sp
    ),
    headlineSmall = Typography.headlineSmall.copy(
        fontFamily = FontFamily(Font(R.font.klavika_medium)),
        fontWeight = FontWeight.Black,
        letterSpacing = 0.23.sp
    ),
    titleLarge = Typography.titleLarge.copy(
        fontFamily = FontFamily(Font(R.font.klavika_medium)),
        fontWeight = FontWeight.Black,
        letterSpacing = 0.2.sp
    ),
    titleMedium = Typography.titleMedium.copy(
        fontFamily = FontFamily(Font(R.font.klavika_medium)),
        fontWeight = FontWeight.Black,
        letterSpacing = 0.16.sp
    ),
    titleSmall = Typography.titleSmall.copy(
        fontFamily = FontFamily(Font(R.font.klavika_medium)),
        fontWeight = FontWeight.Black,
        letterSpacing = 0.14.sp
    ),
    labelLarge = Typography.labelLarge.copy(
        fontFamily = FontFamily(Font(R.font.klavika_medium)),
        fontWeight = FontWeight.Black,
        letterSpacing = 0.18.sp
    ),
    labelMedium = Typography.labelMedium.copy(
        fontFamily = FontFamily(Font(R.font.klavika_medium)),
        fontWeight = FontWeight.Black,
        letterSpacing = 0.03.sp
    ),
    labelSmall = Typography.labelSmall.copy(
        fontFamily = FontFamily(Font(R.font.klavika_medium)),
        fontWeight = FontWeight.Black,
        letterSpacing = 0.14.sp
    )
)

val BodyXSRegular = TextStyle(
    fontSize = 12.sp,
    lineHeight = 18.sp,
    fontFamily = FontFamily(Font(R.font.klavika_regular)),
    fontWeight = FontWeight.Normal,
    color = NeutralBlackGrey,
    letterSpacing = 0.02.sp
)
val BodyXSBold = TextStyle(
    fontSize = 12.sp,
    lineHeight = 18.sp,
    fontFamily = FontFamily(Font(R.font.klavika_regular)),
    fontWeight = FontWeight.Bold,
    color = NeutralBlackGrey,
    letterSpacing = 0.06.sp
)
val BodyXSBlack = TextStyle(
    fontSize = 12.sp,
    lineHeight = 18.sp,
    fontFamily = FontFamily(Font(R.font.klavika_regular)),
    fontWeight = FontWeight.Black,
    color = NeutralBlackGrey,
    letterSpacing = 0.12.sp
)