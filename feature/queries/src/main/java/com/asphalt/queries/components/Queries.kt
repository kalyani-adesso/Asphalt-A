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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.BrightGray
import com.asphalt.commonui.theme.Charcoal
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GrayDark
import com.asphalt.commonui.theme.GreenLIGHT25
import com.asphalt.commonui.theme.LightSilver
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.PaleGreen
import com.asphalt.commonui.theme.PaleOrange
import com.asphalt.commonui.theme.PalePink
import com.asphalt.commonui.theme.SafetyOrange
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.commonui.ui.CustomLabel
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.commonui.utils.Utils
import com.asphalt.queries.data.Query
import com.asphalt.queries.sealedclasses.QueryCategories
import com.asphalt.queries.viewmodels.QueriesVM
import kotlinx.coroutines.launch

@Composable
fun Queries(queries: List<Query>, queriesVM: QueriesVM, selectQueryForAnswer: (String) -> Unit) {

    LazyColumn(verticalArrangement = Arrangement.spacedBy(Dimensions.spacing20)) {
        items(queries) {
            ComposeUtils.CommonContentBox {
                QueryComponent(it, queriesVM, selectQueryForAnswer)
            }
        }
    }
}

@Composable
fun QueryComponent(query: Query, queriesVM: QueriesVM, selectQueryForAnswer: (String) -> Unit) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(
                vertical = Dimensions.padding20, horizontal = Dimensions.padding15
            )
            .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Dimensions.padding15)
    ) {

        ComposeUtils.SectionTitle(query.title)
        Row(horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing10)) {

            QueryCategories.getCategoryNameById(query.categoryId)
                ?.let {
                    CustomLabel(
                        textColor = SafetyOrange,
                        backColor = PaleOrange,
                        text = stringResource(it)
                    )
                }
            if (query.isAnswered) CustomLabel(
                textColor = GreenLIGHT25, backColor = PaleGreen, text =
                    stringResource(R.string.answered)
            )
        }
        Text(
            text = query.description,
            color = NeutralDarkGrey,
            style = Typography.titleSmall,
            fontSize = Dimensions.textSize14
        )
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            CircularNetworkImage(
                imageUrl = query.postedByUrl,
                size = Dimensions.spacing33,
                borderColor = PalePink,
                borderWidth = Dimensions.size3,
                placeholderPainter = painterResource(R.drawable.profile_placeholder)

            )
            ComposeUtils.SectionTitle(
                query.postedByName, modifier = Modifier.padding(start = Dimensions.spacing10)
            )
            Spacer(Modifier.weight(1f))
            ComposeUtils.SectionSubtitle(
                Utils.formatRelativeTime(
                    Utils.formatClientMillisToISO(
                        query.postedOn
                    )
                ), color = GrayDark
            )
        }
        HorizontalDivider(color = LightSilver)
        if (query.answers.isNotEmpty())
            Answers(query.answers, queriesVM, queryId = query.id)
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing10),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(if (query.isUserLiked) R.drawable.ic_thumbs_up_filled else R.drawable.ic_thumb_up),
                null,
                modifier = Modifier
                    .size(Dimensions.spacing15pt75, Dimensions.spacing15)
                    .clickable {
                        scope.launch {

                            queriesVM.likeOrRemoveLikeOfQuestion(query.id,query.isUserLiked)
                        }
                    }
                    .offset(y = Dimensions.spacingNeg2)
            )
            Text(text = query.likeCount.toString(), style = TypographyMedium.bodySmall)
            Image(
                painter = painterResource(R.drawable.ic_chat_bubble_blue),
                null,
                modifier = Modifier.size(Dimensions.spacing15)
            )
            Text(text = query.answerCount.toString(), style = TypographyMedium.bodySmall)
            Spacer(Modifier.weight(1f))
            RoundedBox(
                borderColor = BrightGray,
                borderStroke = Dimensions.padding1, cornerRadius = Dimensions.radius5,
                modifier = Modifier.clickable {

                    selectQueryForAnswer(query.id)
                }
            ) {
                Row(
                    modifier = Modifier.padding(
                        vertical = Dimensions.padding8,
                        horizontal = Dimensions.size17
                    )
                ) {

                    Text(
                        text = stringResource(R.string.answer),
                        style = TypographyMedium.bodySmall,
                        fontSize = Dimensions.textSize12,
                        color = Charcoal
                    )
                }
            }

        }

    }
}