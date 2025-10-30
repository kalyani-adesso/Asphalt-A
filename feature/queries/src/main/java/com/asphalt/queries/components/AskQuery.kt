package com.asphalt.queries.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.android.model.CurrentUser
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.ScarletRed
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.theme.VividOrange
import com.asphalt.commonui.ui.BorderedButton
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.commonui.utils.ComposeUtils.HeaderWithInputField
import com.asphalt.commonui.utils.ComposeUtils.SectionTitle
import com.asphalt.queries.sealedclasses.QueryCategories
import com.asphalt.queries.viewmodels.AskQueryVM
import kotlinx.coroutines.launch

@Composable
fun AskQuery(
    askQueryVM: AskQueryVM,
    onDismiss: () -> Unit,
    onSubmit: (queryId: String) -> Unit,
    user: CurrentUser?
) {
    val askQuestion = askQueryVM.askQuestion.collectAsStateWithLifecycle()
    val questionError = askQueryVM.questionError.collectAsStateWithLifecycle()
    val categoryError = askQueryVM.categoryError.collectAsStateWithLifecycle()
    val description = askQueryVM.description.collectAsStateWithLifecycle()
    val descriptionError = askQueryVM.descriptionError.collectAsStateWithLifecycle()
    val selectedCategory = askQueryVM.selectedCategory.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    var anchorWidth by remember { mutableIntStateOf(0) }
    var expanded by remember { mutableStateOf(false) }
    val categories = QueryCategories.getAllCategories()
    LaunchedEffect(user) {
        askQueryVM.setUser(user)
    }



    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ComposeUtils.CommonContentBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Constants.DEFAULT_SCREEN_HORIZONTAL_PADDING)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = Constants.DEFAULT_SCREEN_HORIZONTAL_PADDING,
                    vertical = Dimensions.size25
                ),
                verticalArrangement = Arrangement.spacedBy(Dimensions.spacing20)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ComposeUtils.RoundedIconWithHeaderComponent(
                        VividOrange,
                        R.drawable.ic_split,
                        stringResource(R.string.ask_a_question),
                        stringResource(R.string.get_help_from_the_motorcycle_community)
                    )

                    Image(
                        painter = painterResource(R.drawable.ic_close),
                        null,
                        modifier = Modifier
                            .clickable {
                                onDismiss.invoke()
                            }
                    )
                }
                HeaderWithInputField(
                    stringResource(R.string.question_title),
                    askQuestion.value,
                    {
                        askQueryVM.updateQuestion(it)
                    },
                    stringResource(R.string.brief_description_of_the_question),
                    questionError.value,
                    stringResource(R.string.question_cannot_be_empty),
                    isSingleLine = false

                )
                SectionTitle(stringResource(R.string.category))

                Box(
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            anchorWidth = coordinates.size.width
                        }) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(Dimensions.padding50)
                                .background(
                                    NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10)
                                )
                                .clickable {
                                    expanded = true
                                }
                                .onGloballyPositioned { coordinates ->
                                    anchorWidth = coordinates.size.width // capture width in pixels
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(
                                    selectedCategory.value?.nameRes ?: R.string.select_a_category
                                ),
                                style = TypographyMedium.bodySmall,
                                color = if (selectedCategory.value?.nameRes != null) NeutralBlack else NeutralDarkGrey,
                                modifier = Modifier.padding(start = Dimensions.padding16)
                            )
                            Image(
                                painter = painterResource(R.drawable.ic_dropdown_arrow),
                                contentDescription = null,
                                modifier = Modifier.padding(end = Dimensions.padding16)
                            )
                        }


                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current) { anchorWidth.toDp() })
                                .background(NeutralWhite)
                        ) {
                            categories.forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            stringResource(option.nameRes),
                                            style = Typography.bodySmall
                                        )
                                    },
                                    onClick = {
                                        askQueryVM.updateSelectedCategory(option)
                                        expanded = false
                                    })
                            }
                        }
                        ComposeUtils.TexFieldError(
                            categoryError.value,
                            stringResource(R.string.category_cannot_be_empty)
                        )
                    }
                }



                HeaderWithInputField(
                    stringResource(R.string.description),
                    description.value,
                    {
                        askQueryVM.updateDescription(it)
                    },
                    stringResource(R.string.provide_more_details_about_your_question),
                    descriptionError.value,
                    stringResource(R.string.description_cannot_be_empty), isSingleLine = false
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing20)
                ) {
                    BorderedButton(
                        onClick = {
                            onDismiss.invoke()
                        },
                        borderColor = ScarletRed,
                        modifier = Modifier.weight(1f),
                        contentPaddingValues = PaddingValues(
                            Dimensions.size0
                        )
                    ) {
                        ComposeUtils.DefaultButtonContent(
                            stringResource(R.string.cancel).uppercase(),
                            color = ScarletRed
                        )
                    }
                    GradientButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            scope.launch {
                                if (askQueryVM.validateFields()) {
                                    askQueryVM.submitQuestion({
                                        onSubmit(it)
                                    })

                                }
                            }
                        },
                        contentPadding = PaddingValues(
                            Dimensions.size0
                        )
                    ) {
                        ComposeUtils.DefaultButtonContent(stringResource(R.string.ask_question).uppercase())
                    }
                }
            }

        }
    }

}