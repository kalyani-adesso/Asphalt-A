package com.asphalt.queries.viewmodels

import androidx.lifecycle.ViewModel
import com.asphalt.android.model.CurrentUser
import com.asphalt.commonui.utils.Utils
import com.asphalt.queries.data.Query
import com.asphalt.queries.repositories.QueryRepo
import com.asphalt.queries.sealedclasses.QueryCategories
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AskQueryVM(val queryRepo: QueryRepo) : ViewModel() {
    private val _askQuestion = MutableStateFlow("")
    private val _questionError = MutableStateFlow(false)
    val questionError: StateFlow<Boolean> = _questionError.asStateFlow()

    private val _categoryError = MutableStateFlow(false)
    val categoryError: StateFlow<Boolean> = _categoryError.asStateFlow()
    val askQuestion: StateFlow<String> = _askQuestion.asStateFlow()
    private var user: CurrentUser? = null
    fun updateQuestion(query: String) {
        _questionError.value = false
        _askQuestion.value = query
    }

    private val _description = MutableStateFlow("")
    private val _descriptionError = MutableStateFlow(false)
    val descriptionError: StateFlow<Boolean> = _descriptionError.asStateFlow()
    val description: StateFlow<String> = _description.asStateFlow()
    fun updateDescription(description: String) {
        _descriptionError.value = false
        _description.value = description
    }

    private val _selectedCategory = MutableStateFlow<QueryCategories?>(null)
    val selectedCategory: StateFlow<QueryCategories?> = _selectedCategory.asStateFlow()
    fun updateSelectedCategory(category: QueryCategories) {
        _categoryError.value = false
        _selectedCategory.value = category
    }

    fun validateFields(): Boolean {
        var isValid = true
        if (_description.value.isEmpty()) {
            _descriptionError.value = true
            isValid = false
        }
        if (_askQuestion.value.isEmpty()) {
            _questionError.value = true
            isValid = false
        }
        if (_selectedCategory.value == null) {
            _categoryError.value = true
            isValid = false
        }
        return isValid

    }

    suspend fun submitQuestion() {
        queryRepo.addQuery(
            Query(
                title = _askQuestion.value,
                description = _description.value,
                isAnswered = false,
                categoryId = _selectedCategory.value?.id ?: 0,
                postedOn = Utils.formatClientMillisToISO(System.currentTimeMillis()),
                postedByName = user?.name?:"",
                postedByUrl = "",
                likeCount = 0,
                answerCount = 0,
                answers = emptyList()
            )
        )
    }

    fun setUser(user: CurrentUser?) {
        this.user = user
    }
}