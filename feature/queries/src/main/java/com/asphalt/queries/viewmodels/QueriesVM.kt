package com.asphalt.queries.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.queries.QueryConstants.CATEGORY_ALL_ID
import com.asphalt.queries.data.Query
import com.asphalt.queries.repositories.QueryRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QueriesVM(val queryRepo: QueryRepo) : ViewModel() {
    fun setFilterCategory(categoryID: Int) {
        filterCategory.value = categoryID
    }


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
}