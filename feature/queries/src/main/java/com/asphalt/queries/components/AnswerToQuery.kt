package com.asphalt.queries.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GrayDark
import com.asphalt.commonui.theme.GreenLIGHT25
import com.asphalt.commonui.theme.LightSilver
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralBlack10
import com.asphalt.commonui.theme.NeutralBlack50
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightGrayishBlue50
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PaleGreen
import com.asphalt.commonui.theme.PaleLavenderBlue30
import com.asphalt.commonui.theme.PaleMintyBlue30
import com.asphalt.commonui.theme.PaleOrange
import com.asphalt.commonui.theme.PalePink
import com.asphalt.commonui.theme.PrimaryBlue50
import com.asphalt.commonui.theme.PrimaryBrighterLightB33
import com.asphalt.commonui.theme.SafetyOrange
import com.asphalt.commonui.theme.ScienceBlue
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.commonui.ui.CustomLabel
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.commonui.utils.Utils
import com.asphalt.queries.constants.QueryConstants
import com.asphalt.queries.data.Query
import com.asphalt.queries.sealedclasses.QueryCategories
import com.asphalt.queries.viewmodels.QueriesVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerToQuery(
    dismissPopup: () -> Unit,
    queryId: Int?,
    queriesVM: QueriesVM,
    queryList: List<Query>
) {
    val answerToQuery = queriesVM.answerToQuery.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val query = queryList.find { it -> it.id == queryId }
    val isPostAnswerEnabled = remember { derivedStateOf { answerToQuery.value.isNotEmpty() } }
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = { dismissPopup() },
        sheetState = sheetState,
        scrimColor = NeutralBlack50,
        dragHandle = {},
        containerColor = Color.Transparent,
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            NeutralWhite, PaleMintyBlue30, PaleLavenderBlue30
                        )
                    )
                )
                .fillMaxWidth()
                .fillMaxHeight(QueryConstants.BOTTOM_SHEET_HEIGHT_RATIO)
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = Dimensions.padding15,
                    vertical = Dimensions.size25
                ), verticalAlignment = Alignment.CenterVertically
            ) {

                ComposeUtils.SectionTitle(stringResource(R.string.answer_to_query))
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    dismissPopup.invoke()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        null,
                        modifier = Modifier.size(
                            Dimensions.size10
                        )
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                item {
                    query?.let {
                        Column(
                            modifier = Modifier
                                .padding(
                                    vertical = Dimensions.padding20,
                                    horizontal = Dimensions.padding15
                                )
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(Dimensions.padding15)
                        ) {
                            RoundedBox(backgroundColor = NeutralWhite) {

                                Column(
                                    modifier = Modifier.padding(Dimensions.padding20),
                                    verticalArrangement = Arrangement.spacedBy(Dimensions.padding15)
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
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        CircularNetworkImage(
                                            imageUrl = query.postedByUrl,
                                            size = Dimensions.spacing33,
                                            borderColor = PalePink,
                                            borderWidth = Dimensions.size3,
                                            placeholderPainter = painterResource(R.drawable.profile_placeholder)

                                        )
                                        ComposeUtils.SectionTitle(
                                            query.postedByName,
                                            modifier = Modifier.padding(start = Dimensions.spacing10)
                                        )
                                        Spacer(Modifier.weight(1f))
                                        ComposeUtils.SectionSubtitle(
                                            Utils.formatRelativeTime(query.postedOn),
                                            color = GrayDark
                                        )
                                    }
                                    HorizontalDivider(color = LightSilver)

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing10),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(if (query.isUserLiked) R.drawable.ic_thumbs_up_filled else R.drawable.ic_thumb_up),
                                            null,
                                            modifier = Modifier
                                                .size(
                                                    Dimensions.spacing15pt75,
                                                    Dimensions.spacing15
                                                )
                                                .clickable {
                                                    scope.launch {

                                                        queriesVM.likeOrRemoveLikeOfQuestion(query.id)
                                                    }
                                                }
                                                .offset(y = Dimensions.spacingNeg2)
                                        )
                                        Text(
                                            text = query.likeCount.toString(),
                                            style = TypographyMedium.bodySmall
                                        )
                                        Image(
                                            painter = painterResource(R.drawable.ic_chat_bubble_blue),
                                            null,
                                            modifier = Modifier.size(Dimensions.spacing15)
                                        )
                                        Text(
                                            text = query.answerCount.toString(),
                                            style = TypographyMedium.bodySmall
                                        )


                                    }
                                }
                            }
                            if (query.answers.isNotEmpty())
                                Answers(
                                    query.answers,
                                    queriesVM,
                                    modifier = Modifier.padding(end = Dimensions.padding28),
                                    showDivider = false
                                )


                        }
                    }
                }
            }



            Box(
                modifier = Modifier
                    .background(NeutralWhite)
                    .fillMaxWidth()
                    .clickable(enabled = false, onClick = {})
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = Dimensions.padding24,
                        vertical = Dimensions.padding20
                    )
                ) {

                    Row(horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing12)) {

                        RoundedBox(
                            modifier = Modifier.size(Dimensions.size58),
                            borderColor = PrimaryBrighterLightB33,
                            borderStroke = Dimensions.size4,
                            cornerRadius = Dimensions.size58,
                            contentAlignment = Alignment.Center
                        ) {

                            CircularNetworkImage(
                                imageUrl = "",
                                size = Dimensions.size52,
                                placeholderPainter = painterResource(R.drawable.profile_placeholder)
                            )
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(Dimensions.spacing12)) {
                            ComposeUtils.CustomTextField(
                                answerToQuery.value,
                                {
                                    queriesVM.updateAnswerToQuery(it)
                                },
                                stringResource(R.string.share_your_knowledge),
                                isSingleLine = false,
                                borderColor = NeutralLightGrayishBlue50,
                                borderStroke = Dimensions.size1pt5,
                                heightMin = Dimensions.padding100,
                                textStyle = TypographyMedium.bodyMedium
                            )

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RoundedBox(
                                    borderColor = NeutralBlack10,
                                    modifier = Modifier
                                        .height(Dimensions.size35)
                                        .width(Dimensions.padding87)
                                        .clickable {
                                            dismissPopup.invoke()
                                        },
                                    borderStroke = Dimensions.radius1,
                                    cornerRadius = Dimensions.radius5
                                ) {
                                    Text(
                                        stringResource(R.string.cancel),
                                        color = NeutralBlack,
                                        style = TypographyBold.titleMedium,
                                        fontSize = Dimensions.textSize14,
                                        modifier = Modifier.align(
                                            Alignment.Center
                                        )
                                    )
                                }
                                Spacer(Modifier.weight(1.0f))
                                RoundedBox(

                                    backgroundColor = if (isPostAnswerEnabled.value) ScienceBlue else PrimaryBlue50,
                                    modifier = Modifier
                                        .height(Dimensions.size35)
                                        .width(Dimensions.size132)
                                        .clickable(enabled = isPostAnswerEnabled.value) {
                                            scope.launch {
                                                queriesVM.postAnswer(query)
                                                dismissPopup.invoke()
                                            }
                                        },
                                    cornerRadius = Dimensions.radius5,
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(
                                            Dimensions.spacing15,
                                            Alignment.CenterHorizontally
                                        ), verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_post),
                                            null,
                                            tint = NeutralWhite,
                                        )

                                        Text(
                                            stringResource(R.string.post_answer),
                                            color = NeutralWhite,
                                            style = TypographyBold.titleMedium,
                                            fontSize = Dimensions.textSize14
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
