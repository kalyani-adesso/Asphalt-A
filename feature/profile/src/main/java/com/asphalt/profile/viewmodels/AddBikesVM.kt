package com.asphalt.profile.viewmodels

import androidx.lifecycle.ViewModel
import com.asphalt.profile.sealedclasses.BikeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddBikesVM :
    ViewModel() {

    private val _make = MutableStateFlow("")
    val make: StateFlow<String> = _make

    private val _bikeType = MutableStateFlow(BikeType.SportsBike.id)
    fun updateMake(input: String) {
        _makeError.value = false
        _make.value = input
    }

    val bikeType = _bikeType.asStateFlow()

    private val _makeError = MutableStateFlow(false)
    val makeError: StateFlow<Boolean> = _makeError
    private val _modelError = MutableStateFlow(false)
    val modelError: StateFlow<Boolean> = _modelError


    private val _model = MutableStateFlow("")
    val model: StateFlow<String> = _model
    fun updateModel(input: String) {
        _modelError.value = false
        _model.value = input
    }

    fun addBike(onValidation: () -> Unit) {
        var isValidFields = true
        if (_make.value.isEmpty()) {
            _makeError.value = true
            isValidFields = false
        }
        if (_model.value.isEmpty()) {
            _modelError.value = true
            isValidFields = false
        }
        if (isValidFields)
            onValidation()
    }

    fun updateBikeType(id: Int) {
        _bikeType.value = id
    }

    fun clearAll() {
        _model.value = ""
        _make.value = ""
        _makeError.value = false
        _modelError.value = false
        _bikeType.value = BikeType.SportsBike.id
    }
}