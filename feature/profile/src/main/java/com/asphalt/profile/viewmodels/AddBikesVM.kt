package com.asphalt.profile.viewmodels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.asphalt.profile.data.VehicleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddBikesVM : ViewModel() {
    private val _make = MutableStateFlow("")
    val make: StateFlow<String> = _make
    fun updateMake(input: String) {
        _makeError.value = false
        _make.value = input
    }

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

    fun addBike(): VehicleData? {
        var isValidFields = true
        if (_make.value.isEmpty()){
            _makeError.value = true
            isValidFields = false
        }
        if (_model.value.isEmpty()){
            _modelError.value = true
            isValidFields = false
        }
        return if (isValidFields)
            VehicleData(_make.value, _model.value)
        else null
    }

    fun clearAll() {
        _model.value = ""
        _make.value = ""
        _makeError.value = false
        _modelError.value = false
    }
}