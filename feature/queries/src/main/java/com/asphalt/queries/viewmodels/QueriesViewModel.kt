package com.asphalt.queries.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.queries.AnswerDomain
import com.asphalt.android.model.queries.QueryDomain
import com.asphalt.android.repository.queries.QueryRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.UIState
import com.asphalt.commonui.utils.Utils
import com.asphalt.queries.constants.QueryConstants.CATEGORY_ALL_ID
import com.asphalt.queries.data.Answer
import com.asphalt.queries.data.Query
import com.asphalt.queries.repositories.QueryRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QueriesVM(
    val queryRepo: QueryRepo,
    val androidUserVM: AndroidUserVM,
    val queryRepository: QueryRepository
) : ViewModel() {
    fun setFilterCategory(categoryID: Int) {
        filterCategory.value = categoryID
    }

    private val _answerToQuery = MutableStateFlow("")
    val answerToQuery = _answerToQuery.asStateFlow()

    private val _queryList = MutableStateFlow<List<Query>>(emptyList())
    private var filterCategory = MutableStateFlow(CATEGORY_ALL_ID)

    val filteredQueryList: StateFlow<List<Query>> =
        combine(_queryList, filterCategory) { queries, categoryId ->
            val filteredQuery =
                if (categoryId == CATEGORY_ALL_ID) queries else queries.filter { it.categoryId == categoryId }
            filteredQuery.sortedByDescending { it.postedOn }

        }.stateIn(
            viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )


    init {
        viewModelScope.launch {
            loadQueries()
        }
    }


    suspend fun loadQueries() {
//        _queryState.value = UIState.Loading

        when (val result = queryRepository.getQueries()) {
            is APIResult.Success -> {
                val uiList = result.data.toQueryListUiModel()
                _queryList.value = uiList
//                _queryState.value = UIState.Success(uiList)
            }
            is APIResult.Error -> {
                val msg = result.exception.message ?: "Something went wrong"
//                _queryState.value = UIState.Error(msg)
            }
        }
//        _queryList.value = queryRepository.getQueries().toQueryListUiModel()

//        _queryList.value = queryRepo.loadQueryList()
    }

    private fun updateQuery(updatedQuery: Query?) {
        if (updatedQuery != null) {
            _queryList.update { currentList ->
                currentList.map { query ->
                    if (query.id == updatedQuery.id) {
                        updatedQuery
                    } else {
                        query
                    }
                }
            }
        }
    }

    suspend fun likeOrRemoveLikeOfQuestion(id: String) {
        val updatedQuery = queryRepo.likeOrRemoveLikeOfQuestion(id)
        updateQuery(updatedQuery)
    }

    suspend fun likeOrRemoveLikeOfAnswer(id: String) {


        val updatedQuery = queryRepo.likeOrRemoveLikeOfAnswer(id)
        updateQuery(updatedQuery)
    }

    suspend fun likeOrRemoveDislikeOfAnswer(id: String) {

        val updatedQuery = queryRepo.likeOrRemoveDislikeOfAnswer(id)
        updateQuery(updatedQuery)
    }

    fun updateAnswerToQuery(answer: String) {
        _answerToQuery.value = answer
    }

    suspend fun postAnswer(query: Query) {
//        val postAnswer = queryRepo.postAnswer(
//            _answerToQuery.value,
//            query?.id,
//            androidUserVM.userState.value?.name
//        )
//        updateQuery(postAnswer)
        val addAnswer = queryRepository.addAnswer(
            query.id, answerToQuery.value, androidUserVM.userState.value?.uid ?: "",
            Utils.formatClientMillisToISO(System.currentTimeMillis())
        )
        


    }

    fun clearAnswerToQuestion() {
        _answerToQuery.value = ""
    }

    private fun List<QueryDomain>.toQueryListUiModel(): List<Query> {
        return with(this) {
            map {
                it.toQueryUiModel()
            }
        }
    }

    private fun QueryDomain.toQueryUiModel(): Query {
        return with(this) {

            val isUserLiked =
                likes.find { it -> it == androidUserVM.userState.value?.uid }.isNullOrBlank().not()
            Query(
                id,
                title,
                description,
                categoryId,
                answers.isNotEmpty(),
                Utils.parseISODateToMillis(postedOn),
                "", "",
                likes.size,
                answers.size,
                answers.toAnswersUiModel(),
                isUserLiked
            )
        }
    }

    private fun List<AnswerDomain>.toAnswersUiModel(): List<Answer> {
        return with(this) {
            map {
                it.toAnswerUiModel()
            }
        }
    }

    private fun AnswerDomain.toAnswerUiModel(): Answer {
        return with(this) {
            Answer(
                id,
                "",
                "",
                Utils.parseISODateToMillis(answeredOn),
                likes.size,
                dislikes.size,
                answer
            )
        }
    }
}