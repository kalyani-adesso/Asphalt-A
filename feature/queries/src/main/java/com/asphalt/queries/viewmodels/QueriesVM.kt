package com.asphalt.queries.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.queries.constants.QueryConstants.CATEGORY_ALL_ID
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

class QueriesVM(val queryRepo: QueryRepo, val androidUserVM: AndroidUserVM) : ViewModel() {
    fun setFilterCategory(categoryID: Int) {
        filterCategory.value = categoryID
    }

    private val _answerToQuery = MutableStateFlow("")
    val answerToQuery = _answerToQuery.asStateFlow()

    private val _queryList = MutableStateFlow<List<Query>>(emptyList())
    private var filterCategory = MutableStateFlow(CATEGORY_ALL_ID)

    val filteredQueryList: StateFlow<List<Query>> =
        combine(_queryList, filterCategory) { queries, categoryId ->
            if (categoryId == CATEGORY_ALL_ID) queries else queries.filter { it.categoryId == categoryId }
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
        _queryList.value = queryRepo.loadQueryList()
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

    suspend fun likeOrRemoveLikeOfQuestion(id: Int) {
        val updatedQuery = queryRepo.likeOrRemoveLikeOfQuestion(id)
        updateQuery(updatedQuery)
    }

    suspend fun likeOrRemoveLikeOfAnswer(id: Int) {


        val updatedQuery = queryRepo.likeOrRemoveLikeOfAnswer(id)
        updateQuery(updatedQuery)
    }

    suspend fun likeOrRemoveDislikeOfAnswer(id: Int) {

        val updatedQuery = queryRepo.likeOrRemoveDislikeOfAnswer(id)
        updateQuery(updatedQuery)
    }

    fun updateAnswerToQuery(answer: String) {
        _answerToQuery.value = answer
    }

    suspend fun postAnswer(query: Query?) {
        val postAnswer = queryRepo.postAnswer(
            _answerToQuery.value,
            query?.id,
            androidUserVM.userState.value?.name
        )
        updateQuery(postAnswer)


    }

    fun clearAnswerToQuestion() {
        _answerToQuery.value = ""
    }
}