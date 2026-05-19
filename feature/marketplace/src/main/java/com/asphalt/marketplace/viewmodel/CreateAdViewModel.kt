package com.asphalt.marketplace.viewmodel

import androidx.lifecycle.ViewModel
import com.asphalt.marketplace.model.CreateAdUIModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CreateAdViewModel : ViewModel() {
    private val _createAd_model = MutableStateFlow(CreateAdUIModel())
    val createAd_model: StateFlow<CreateAdUIModel> = _createAd_model

    fun validations(): Boolean {
        if (_createAd_model.value.tile.isEmpty()) {
            _createAd_model.value = _createAd_model.value.copy(isShowTitleError = true)
            return false
        } else {
            _createAd_model.value = _createAd_model.value.copy(isShowTitleError = false)
        }
        if (_createAd_model.value.color.isEmpty()) {
            _createAd_model.value = _createAd_model.value.copy(isShowColorError = true)
            return false
        } else {
            _createAd_model.value = _createAd_model.value.copy(isShowColorError = false)
        }
        if (_createAd_model.value.fuel.isEmpty()) {
            _createAd_model.value = _createAd_model.value.copy(isShowFuelError = true)
            return false
        } else {
            _createAd_model.value = _createAd_model.value.copy(isShowFuelError = false)
        }
        return true
    }

    fun setTitle(value: String) {
        _createAd_model.value = _createAd_model.value.copy(tile = value)
    }

    fun setColor(value: String) {
        _createAd_model.value = _createAd_model.value.copy(color = value)
    }

    fun setFuelType(value: String) {
        _createAd_model.value = _createAd_model.value.copy(fuel = value)
    }

    fun setPrice(input: String) {
        val parsed = input.toDoubleOrNull() ?: return
        _createAd_model.value = _createAd_model.value.copy(price = parsed)
    }

}