package com.asphalt.queries.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.queries.AnswerDomain
import com.asphalt.android.model.queries.QueryDomain
import com.asphalt.android.repository.queries.QueryRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.utils.Utils
import com.asphalt.queries.constants.QueryConstants.CATEGORY_ALL_ID
import com.asphalt.queries.data.Answer
import com.asphalt.queries.data.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QueriesVM(
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

        when (val result = queryRepository.getQueries()) {
            is APIResult.Success -> {
                val uiList = result.data.toQueryListUiModel()
                _queryList.value = uiList
            }

            is APIResult.Error -> {
                val msg = result.exception.message ?: "Something went wrong"
            }
        }
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
    private fun updateAnswer(queryId: String, updatedAnswer: Answer?) {
        if (updatedAnswer == null) return

        _queryList.update { currentList ->
            currentList.map { query ->
                if (query.id == queryId) {
                    val updatedAnswers = query.answers.map { answer ->
                        if (answer.id == updatedAnswer.id) {
                            updatedAnswer
                        } else {
                            answer
                        }
                    }
                    query.copy(answers = updatedAnswers)
                } else {
                    query
                }
            }
        }
    }

    suspend fun likeOrRemoveLikeOfQuestion(id: String, currentLikeStatus: Boolean) {
        androidUserVM.userState.value?.uid?.let {
            val likeQueryResult = if (currentLikeStatus) queryRepository.removeLikeQuery(
                it,
                id
            ) else queryRepository.likeQuery(it, id)
            when (likeQueryResult) {
                is APIResult.Error -> {
                }

                is APIResult.Success -> getQueryById(id)
            }
        }
    }

    suspend fun addOrRemoveLikeOfAnswer(
        answerId: String,
        currentLikeStatus: Boolean,
        queryId: String
    ) {
        val uid = androidUserVM.userState.value?.uid
        if (currentLikeStatus)
            uid?.let {
                val result =
                    queryRepository.removeLikeOrDislikeAnswer(it, queryId, answerId)
                when (result) {
                    is APIResult.Error -> {}
                    is APIResult.Success -> {
                        getAnswerById(queryId,answerId)
                    }
                }
            }
        else uid?.let {
            val result =
                queryRepository.likeOrDislikeAnswer(uid, queryId, answerId, true)
            when (result) {
                is APIResult.Error -> {}
                is APIResult.Success -> {
                    getAnswerById(queryId,answerId)

                }
            }
        }
    }

    suspend fun addOrRemoveDislikeOfAnswer(
        answerId: String,
        currentDislikeStatus: Boolean,
        queryId: String
    ) {
        val uid = androidUserVM.userState.value?.uid
        if (currentDislikeStatus)
            uid?.let {
                val result =
                    queryRepository.removeLikeOrDislikeAnswer(it, queryId, answerId)
                when (result) {
                    is APIResult.Error -> {}
                    is APIResult.Success -> {
                        getAnswerById(queryId,answerId)

                    }
                }
            }
        else uid?.let {
            val result =
                queryRepository.likeOrDislikeAnswer(uid, queryId, answerId, false)
            when (result) {
                is APIResult.Error -> {}
                is APIResult.Success -> {
                    getAnswerById(queryId,answerId)

                }
            }
        }
    }

    fun updateAnswerToQuery(answer: String) {
        _answerToQuery.value = answer
    }

    suspend fun postAnswer(query: Query) {
        val addAnswer = queryRepository.addAnswer(
            query.id, answerToQuery.value, androidUserVM.userState.value?.uid ?: "",
            Utils.formatClientMillisToISO(System.currentTimeMillis())
        )
        when (addAnswer) {
            is APIResult.Error -> {
            }

            is APIResult.Success -> {
                getQueryById(query.id)
            }
        }


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

    suspend fun getQueryById(queryId: String) {
        val result = queryRepository.getQueryByID(queryId)
        when (result) {
            is APIResult.Error -> {}
            is APIResult.Success -> updateQuery(result.data.toQueryUiModel())
            null -> {}
        }

    }
    suspend fun getAnswerById(queryId: String,answerId: String) {
        val result = queryRepository.getAnswerByID(queryId,answerId)
        when (result) {
            is APIResult.Error -> {}
            is APIResult.Success -> updateAnswer(queryId,result.data.toAnswerUiModel())
            null -> {}
        }

    }

    private fun AnswerDomain.toAnswerUiModel(): Answer {
        return with(this) {
            val isUserLiked =
                likes.find { it -> it == androidUserVM.userState.value?.uid }.isNullOrBlank().not()
            val isUserDisLiked =
                dislikes.find { it -> it == androidUserVM.userState.value?.uid }.isNullOrBlank()
                    .not()
            Answer(
                id,
                "",
                "",
                Utils.parseISODateToMillis(answeredOn),
                likes.size,
                dislikes.size,
                answer,
                isUserLiked = isUserLiked, isUserDisliked = isUserDisLiked
            )
        }
    }
}