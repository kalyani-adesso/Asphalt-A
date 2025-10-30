package com.asphalt.queries.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Charcoal
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.ElectricGreen
import com.asphalt.commonui.theme.GrayDark
import com.asphalt.commonui.theme.LightSilver
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PaleMint
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.commonui.ui.CustomLabel
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.commonui.utils.Utils
import com.asphalt.queries.data.Answer
import com.asphalt.queries.viewmodels.QueriesVM
import kotlinx.coroutines.launch

@Composable
fun Answers(
    answers: List<Answer>,
    queriesVM: QueriesVM,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true, queryId: String
) {

    Column(verticalArrangement = Arrangement.spacedBy(Dimensions.spacing20), modifier = modifier) {
        answers.forEach {
            AnswerComponent(it, queriesVM, queryId)
        }
        if (showDivider)
            HorizontalDivider(color = LightSilver)
    }
}

@Composable
fun AnswerComponent(answer: Answer, queriesVM: QueriesVM, queryId: String) {
    val scope = rememberCoroutineScope()
    RoundedBox(backgroundColor = PaleMint, modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.padding15, vertical = Dimensions.padding20),
            verticalArrangement = Arrangement.spacedBy(Dimensions.padding15)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                CircularNetworkImage(
                    imageUrl = answer.answeredByUrl,
                    size = Dimensions.size38,
                    borderColor = ElectricGreen,
                    borderWidth = Dimensions.size2pt5,
                    placeholderPainter = painterResource(R.drawable.profile_placeholder)

                )
                ComposeUtils.SectionTitle(
                    answer.answeredByName, modifier = Modifier.padding(start = Dimensions.spacing10)
                )
                if (answer.isMechanic) {
                    CustomLabel(
                        modifier = Modifier.padding(start = Dimensions.padding10),
                        textColor = Charcoal,
                        backColor = NeutralWhite,
                        text = stringResource(R.string.mechanic),
                        iconRes = R.drawable.ic_spanner_orange
                    )
                }
                Spacer(Modifier.weight(1f))
                ComposeUtils.SectionSubtitle(
                    Utils.formatRelativeTime(Utils.formatClientMillisToISO(answer.answeredOn)),
                    color = GrayDark
                )
            }
            Text(
                text = answer.answer,
                color = NeutralDarkGrey,
                style = Typography.titleSmall,
                fontSize = Dimensions.textSize14
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing10),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(if (answer.isUserLiked) R.drawable.ic_thumbs_up_filled else R.drawable.ic_thumb_up),
                    null,
                    modifier = Modifier
                        .size(Dimensions.spacing15pt75, Dimensions.spacing15)
                        .clickable {
                            scope.launch {

                                queriesVM.addOrRemoveLikeOfAnswer(answer.id,answer.isUserLiked,queryId)
                            }
                        }
                        .offset(y = Dimensions.spacingNeg2)
                )
                Text(text = answer.likeCount.toString(), style = TypographyMedium.bodySmall)
                Image(
                    painter = painterResource(if (answer.isUserDisliked) R.drawable.ic_thumbs_down_filled else R.drawable.ic_thumb_down),
                    null,
                    modifier = Modifier
                        .size(Dimensions.spacing15pt75, Dimensions.spacing15)
                        .clickable {
                            scope.launch {
                                queriesVM.addOrRemoveDislikeOfAnswer(
                                    answer.id,
                                    answer.isUserDisliked, queryId
                                )
                            }
                        }
                )
                Text(text = answer.dislikeCount.toString(), style = TypographyMedium.bodySmall)

            }

        }
    }
}