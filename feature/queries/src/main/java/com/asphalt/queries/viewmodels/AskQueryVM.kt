package com.asphalt.queries.viewmodels

import androidx.lifecycle.ViewModel
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.CurrentUser
import com.asphalt.android.repository.queries.QueryRepository
import com.asphalt.commonui.utils.Utils
import com.asphalt.queries.sealedclasses.QueryCategories
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AskQueryVM(val queryRepository: QueryRepository) : ViewModel() {
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

    suspend fun submitQuestion(submitInvoke: () -> Unit) {

        val addQueryResult = queryRepository.addQuery(
            queryTitle = _askQuestion.value,
            queryDescription = _description.value,
            categoryId = _selectedCategory.value!!.id,
            postedOn = Utils.formatClientMillisToISO(System.currentTimeMillis()),
            postedBy = user?.uid ?: "",
        )
        when (addQueryResult) {
            is APIResult.Error -> {
                addQueryResult.exception.toString()
            }

            is APIResult.Success -> {
                submitInvoke.invoke()
                clearAll()
            }
        }
    }

    fun setUser(user: CurrentUser?) {
        this.user = user
    }

    fun clearAll() {
        _askQuestion.value = ""
        _description.value = ""
        _questionError.value = false
        _categoryError.value = false
        _descriptionError.value = false
        _selectedCategory.value = null
    }


}


