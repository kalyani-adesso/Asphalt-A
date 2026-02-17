package com.asphalt.marketplace.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.asphalt.marketplace.model.CreateAdUIModel

class CreateAdViewModel : ViewModel() {
    private val _createAd_model = mutableStateOf(CreateAdUIModel())
    val createAd_model: State<CreateAdUIModel> = _createAd_model

    fun validations(): Boolean {
        if (_createAd_model.value.tile.isEmpty()) {
            _createAd_model.value = _createAd_model.value.copy(isShowTitleError = true)

            return false
        } else {
            _createAd_model.value = _createAd_model.value.copy(isShowTitleError = false)
        }
        if (_createAd_model.value.color.isEmpty()) {
            _createAd_model.value.isShowColorError = true
            return false
        } else {
            _createAd_model.value.isShowColorError = false
        }
        return true
    }

    fun setTitle(value: String) {
        _createAd_model.value = _createAd_model.value.copy(tile = value)
    }

}