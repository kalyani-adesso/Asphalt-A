package com.asphalt.queries.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.helpers.UserDataHelper
import com.asphalt.android.model.UserDomain
import com.asphalt.android.model.queries.AnswerDomain
import com.asphalt.android.model.queries.QueryDomain
import com.asphalt.android.repository.queries.QueryRepository
import com.asphalt.android.repository.user.UserRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.UIState
import com.asphalt.commonui.UIStateHandler
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
    androidUserVM: AndroidUserVM,
    val queryRepository: QueryRepository, val userRepository: UserRepository
) : ViewModel() {
    fun setFilterCategory(categoryID: Int) {
        filterCategory.value = categoryID
    }

    private val currentUid = androidUserVM.userState.value?.uid
    private val searchText = MutableStateFlow("")


    private val _userList = MutableStateFlow<List<UserDomain>>(emptyList())
    fun getCurrentUserData(): UserDomain? {

        return UserDataHelper.getUserDataFromCurrentList(_userList.value, currentUid ?: "")
    }


    private val _answerToQuery = MutableStateFlow("")
    val answerToQuery = _answerToQuery.asStateFlow()

    private val _queryList = MutableStateFlow<List<Query>>(emptyList())
    private var filterCategory = MutableStateFlow(CATEGORY_ALL_ID)

    val filteredQueryList: StateFlow<List<Query>> =
        combine(_queryList, filterCategory, searchText) { queries, categoryId, searchQuery ->
            val searchFilteredQueries = if (searchQuery.isBlank()) {
                queries
            } else {
                queries.filter {
                    it.title.contains(searchQuery, ignoreCase = true)
                }
            }

            val filteredQuery =
                if (categoryId == CATEGORY_ALL_ID) searchFilteredQueries else searchFilteredQueries.filter { it.categoryId == categoryId }
            filteredQuery.sortedByDescending { it.postedOn }

        }.stateIn(
            viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )


    init {
        viewModelScope.launch {
            loadUserList()
            loadQueries()
        }
    }

    suspend fun loadUserList() {
        APIHelperUI.handleApiResult(
            APIHelperUI.runWithLoader { userRepository.getAllUsers() },
            viewModelScope
        ) {
            _userList.value = it
        }
    }

    suspend fun loadQueries() {
        val result = APIHelperUI.runWithLoader { queryRepository.getQueries() }
        APIHelperUI.handleApiResult(result, viewModelScope) {
            val uiList = it.toQueryListUiModel()
            _queryList.value = uiList
        }
    }

    private fun updateQuery(updatedQuery: Query?) {
        if (updatedQuery == null) return

        _queryList.update { currentList ->
            val existingQuery = currentList.find { it.id == updatedQuery.id }

            if (existingQuery != null) {
                currentList.map { query ->
                    if (query.id == updatedQuery.id) {
                        updatedQuery
                    } else {
                        query
                    }
                }
            } else {
                listOf(updatedQuery) + currentList
            }
        }
    }

    private fun updateAnswer(queryId: String, updatedAnswer: Answer?) {
        if (updatedAnswer == null) return

        _queryList.update { currentList ->
            currentList.map { query ->
                if (query.id == queryId) {
                    val existingAnswer = query.answers.find { it.id == updatedAnswer.id }

                    val updatedAnswers = if (existingAnswer != null) {
                        query.answers.map { answer ->
                            if (answer.id == updatedAnswer.id) updatedAnswer else answer
                        }
                    } else {
                        query.answers + updatedAnswer
                    }

                    query.copy(answers = updatedAnswers)
                } else {
                    query
                }
            }
        }
    }

    suspend fun likeOrRemoveLikeOfQuestion(id: String, currentLikeStatus: Boolean) {
        currentUid?.let {
            val likeQueryResult = APIHelperUI.runWithLoader {
                if (currentLikeStatus) queryRepository.removeLikeQuery(
                    it,
                    id
                ) else queryRepository.likeQuery(it, id)
            }
            APIHelperUI.handleApiResult(likeQueryResult, viewModelScope) {
                getQueryById(id)
            }
        }
    }

    suspend fun addOrRemoveLikeOfAnswer(
        answerId: String,
        currentLikeStatus: Boolean,
        queryId: String
    ) {
        if (currentLikeStatus)
            currentUid?.let {
                val result = APIHelperUI.runWithLoader {
                    queryRepository.removeLikeOrDislikeAnswer(it, queryId, answerId)
                }
                APIHelperUI.handleApiResult(result, viewModelScope) {
                    getAnswerById(
                        queryId,
                        answerId
                    )
                }
            }
        else currentUid?.let {
            val result =
                APIHelperUI.runWithLoader {
                    queryRepository.likeOrDislikeAnswer(
                        currentUid,
                        queryId,
                        answerId,
                        true
                    )
                }
            APIHelperUI.handleApiResult(result, viewModelScope) {
                getAnswerById(queryId, answerId)
            }
        }
    }

    suspend fun addOrRemoveDislikeOfAnswer(
        answerId: String,
        currentDislikeStatus: Boolean,
        queryId: String
    ) {
        if (currentDislikeStatus)
            currentUid?.let {
                val result =
                    APIHelperUI.runWithLoader {
                        queryRepository.removeLikeOrDislikeAnswer(
                            it,
                            queryId,
                            answerId
                        )
                    }
                APIHelperUI.handleApiResult(result, viewModelScope) {
                    getAnswerById(queryId, answerId)
                }
            }
        else currentUid?.let {
            val result =
                APIHelperUI.runWithLoader {
                    queryRepository.likeOrDislikeAnswer(
                        currentUid,
                        queryId,
                        answerId,
                        false
                    )
                }
            APIHelperUI.handleApiResult(result, viewModelScope) {
                getAnswerById(queryId, answerId)
            }
        }
    }

    fun updateAnswerToQuery(answer: String) {
        _answerToQuery.value = answer
    }

    fun postAnswer(query: Query) {
        viewModelScope.launch {
            val answer = answerToQuery.value
            val addAnswer = APIHelperUI.runWithLoader {
                queryRepository.addAnswer(
                    query.id, answer, currentUid ?: "",
                    Utils.formatClientMillisToISO(System.currentTimeMillis())
                )
            }
            APIHelperUI.handleApiResult(addAnswer, viewModelScope) {
                UIStateHandler.sendEvent(UIState.SUCCESS("Added answer successfully!"))
                getAnswerById(query.id, it.name)
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
            val userDataFromCurrentList =
                UserDataHelper.getUserDataFromCurrentList(_userList.value, postedBy)
            val isUserLiked =
                likes.find { it -> it == currentUid }.isNullOrBlank().not()
            Query(
                id,
                title,
                description,
                categoryId,
                answers.isNotEmpty(),
                Utils.parseISODateToMillis(postedOn),
                userDataFromCurrentList?.name ?: "",
                userDataFromCurrentList?.profilePic ?: "",
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

    fun addQuestion(queryId: String) {
        viewModelScope.launch {
            getQueryById(queryId)
        }
    }

    suspend fun getQueryById(queryId: String) {
        val result = APIHelperUI.runWithLoader { queryRepository.getQueryByID(queryId) }
        APIHelperUI.handleApiResult(result, viewModelScope) {
            updateQuery(it.toQueryUiModel())
        }
    }

    suspend fun getAnswerById(queryId: String, answerId: String) {
        val result = APIHelperUI.runWithLoader { queryRepository.getAnswerByID(queryId, answerId) }
        APIHelperUI.handleApiResult(result, viewModelScope) {
            updateAnswer(queryId, it.toAnswerUiModel())
        }
    }

    private fun AnswerDomain.toAnswerUiModel(): Answer {
        val userDataFromCurrentList =
            UserDataHelper.getUserDataFromCurrentList(_userList.value, answeredBy)
        return with(this) {
            val isUserLiked =
                likes.find { it -> it == currentUid }.isNullOrBlank().not()
            val isUserDisLiked =
                dislikes.find { it -> it == currentUid }.isNullOrBlank()
                    .not()
            Answer(
                id,
                userDataFromCurrentList?.name ?: "",
                userDataFromCurrentList?.profilePic ?: "",
                Utils.parseISODateToMillis(answeredOn),
                likes.size,
                dislikes.size,
                answer,
                isUserLiked = isUserLiked, isUserDisliked = isUserDisLiked
            )
        }
    }

    fun search(searchQuery: String) {
        searchText.value = searchQuery
    }
}