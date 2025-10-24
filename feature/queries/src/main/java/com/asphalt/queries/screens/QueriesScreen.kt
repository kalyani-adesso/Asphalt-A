package com.asphalt.queries.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.queries.components.AnswerToQuery
import com.asphalt.queries.components.AskQuery
import com.asphalt.queries.components.FilterCategories
import com.asphalt.queries.components.Queries
import com.asphalt.queries.components.SearchQueries
import com.asphalt.queries.data.Query
import com.asphalt.queries.viewmodels.AskQueryVM
import com.asphalt.queries.viewmodels.QueriesVM
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueriesScreen(
    setTopAppBarState: (AppBarState) -> Unit,
    queriesVM: QueriesVM = koinViewModel(),
    androidUserVM: AndroidUserVM = koinViewModel()

) {
    val askQueryVM: AskQueryVM = koinViewModel()
    var showQueryPopup by remember { mutableStateOf(false) }
    val user = androidUserVM.userState.collectAsStateWithLifecycle()
    var showAnswerPopup by remember { mutableStateOf(false) }

    setTopAppBarState(
        AppBarState(
            title = stringResource(R.string.queries),
            isCenterAligned = false,
            actions = {
                RoundedBox(
                    borderColor = PrimaryDarkerLightB75,
                    borderStroke = Dimensions.padding1,
                    cornerRadius = Dimensions.size10,
                    modifier = Modifier
                        .padding(end = Dimensions.padding15)
                        .clickable {
                            showQueryPopup = true
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .height(Dimensions.padding30),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.ic_add),
                            tint = PrimaryDarkerLightB75,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(Dimensions.spacing5))
                        Text(
                            stringResource(R.string.ask_question).uppercase(),
                            color = PrimaryDarkerLightB75,
                            fontSize = Dimensions.textSize12,
                            style = TypographyBold.titleMedium
                        )
                    }
                }
            })
    )
    val selectedCategory = remember { mutableIntStateOf(0) }
    val queryList = queriesVM.filteredQueryList.collectAsStateWithLifecycle()
    LaunchedEffect(selectedCategory.intValue) {
        queriesVM.setFilterCategory(selectedCategory.intValue)
    }
    val selectedQueryId: MutableState<Int?> = remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    if (showAnswerPopup) {
        AnswerToQuery({
            showAnswerPopup = false
            selectedQueryId.value = null
            queriesVM.clearAnswerToQuestion()
        }, selectedQueryId.value, queriesVM,queryList.value)
    }
    if (showQueryPopup)
        AskQuery(askQueryVM = askQueryVM, onSubmit = {
            scope.launch {
                queriesVM.loadQueries()
                showQueryPopup = false
            }
        }, onDismiss = {
            showQueryPopup = false
        }, user = user.value)
    else askQueryVM.clearAll()
    ComposeUtils.DefaultColumnRoot(
        Dimensions.size0,
        bottomPadding = Dimensions.size0,
        isScrollable = false
    ) {
        Spacer(Modifier.height(Dimensions.padding20))
        SearchQueries()
        FilterCategories(selectedCategory)
        Queries(queryList.value, queriesVM) { it ->
            selectedQueryId.value = it
            showAnswerPopup = true
        }

    }

}

@Composable
@Preview
fun QueriesScreenPreview() {
    QueriesScreen(queriesVM = viewModel(), setTopAppBarState = {})
}