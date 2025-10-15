package com.asphalt.profile.screens.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.BrightYellow
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralLightGrey
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.BorderedButton
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.ComposeUtils

@Composable
fun AchievementsSection() {
    ComposeUtils.CommonContentBox {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = Dimensions.padding16,
                    vertical = Dimensions.padding24,
                )
                .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Dimensions.spacing20)
        ) {
            ComposeUtils.RoundedIconWithHeaderComponent(
                BrightYellow,
                R.drawable.ic_trophy,
                stringResource(R.string.achievements), stringResource(R.string.badges_earned)
            )
            RoundedBox(
                cornerRadius = Dimensions.size10,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimensions.padding21),
                borderColor = NeutralLightGrey,
                borderStroke = Dimensions.spacing1
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = Dimensions.size25)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(
                        Dimensions.spacing15,
                        Alignment.CenterVertically
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painter = painterResource(R.drawable.ic_trophy_blue), null)
                    ComposeUtils.SectionTitle(stringResource(R.string.no_badges_earned_yet))
                    ComposeUtils.SectionSubtitle(
                        stringResource(R.string.complete_quizzes),
                        modifier = Modifier.offset(y = Dimensions.spacingNeg4)
                    )
                    ComposeUtils.SectionSubtitle(
                        stringResource(R.string.to_earn_your_first_badge),
                        modifier = Modifier.offset(y = Dimensions.spacingNeg8)
                    )
                    BorderedButton(onClick = {

                    }, buttonRadius = Dimensions.size10, buttonHeight = Dimensions.size50) {
                        Text(
                            stringResource(R.string.start_earning_badges),
                            color = PrimaryDarkerLightB75,
                            style = TypographyBold.bodyMedium,
                            fontSize = Dimensions.textSize14
                        )

                    }

                }

            }


        }
    }
}