package com.asphalt.resetpassword.viewmodel


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.asphalt.resetpassword.model.CreatePasswordStateModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CreatePasswordViewModel : ViewModel() {
    private val _createPasswordMutableState = MutableStateFlow(CreatePasswordStateModel())
    val createPasswordState: StateFlow<CreatePasswordStateModel> = _createPasswordMutableState

    fun updateEmail(email: String) {
        _createPasswordMutableState.value = _createPasswordMutableState.value.copy(email = email)

    }

    fun updatePassword(password: String) {
        _createPasswordMutableState.value =
            _createPasswordMutableState.value.copy(password = password)
    }

    fun updateConfirmPassword(password: String) {
        _createPasswordMutableState.value =
            _createPasswordMutableState.value.copy(confirmPassword = password)
    }

}