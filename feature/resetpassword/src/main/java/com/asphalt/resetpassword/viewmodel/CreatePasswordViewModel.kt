package com.asphalt.resetpassword.viewmodel


import androidx.lifecycle.ViewModel
import com.asphalt.commonui.R
import com.asphalt.resetpassword.model.CreatePasswordStateModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CreatePasswordViewModel : ViewModel() {
    private val _createPasswordMutableState = MutableStateFlow(CreatePasswordStateModel())
    val createPasswordState: StateFlow<CreatePasswordStateModel> = _createPasswordMutableState

    fun updateEmail(email: String) {
        _createPasswordMutableState.value =
            _createPasswordMutableState.value.copy(isShowEmailError = false)
        _createPasswordMutableState.value = _createPasswordMutableState.value.copy(email = email)

    }

    fun updatePassword(password: String) {
        _createPasswordMutableState.value =
            _createPasswordMutableState.value.copy(isShowPasswordError = false)
        _createPasswordMutableState.value =
            _createPasswordMutableState.value.copy(password = password)
    }

    fun updateConfirmPassword(password: String) {
        _createPasswordMutableState.value =
            _createPasswordMutableState.value.copy(confirmPassword = password)
    }

    fun createPassword(): Boolean {
        if (validation()) {
            return true
        }
        return false

    }

    fun validation(): Boolean {
        if (_createPasswordMutableState.value.email.isNullOrEmpty()) {
            _createPasswordMutableState.value =
                _createPasswordMutableState.value.copy(isShowEmailError = true)
            return false
        }
        if (_createPasswordMutableState.value.password.isNullOrEmpty()) {
            _createPasswordMutableState.value =
                _createPasswordMutableState.value.copy(isShowPasswordError = true)
            return false
        }
        if (_createPasswordMutableState.value.confirmPassword.isNullOrEmpty()) {
            _createPasswordMutableState.value =
                _createPasswordMutableState.value.copy(
                    isShowConfirmPasswordError = true,
                    confirmPasswordError = R.string.enter_valid_password
                )
            return false
        }
        if (!_createPasswordMutableState.value.confirmPassword.equals(_createPasswordMutableState.value.password)) {
            _createPasswordMutableState.value = _createPasswordMutableState.value.copy(
                isShowConfirmPasswordError = true,
                confirmPasswordError = R.string.password_confirm_password_match
            )
            return false
        }
        _createPasswordMutableState.value = _createPasswordMutableState.value.copy(
            isShowConfirmPasswordError = false,
            isShowEmailError = false,
            isShowPasswordError = false
        )
        return true
    }

}