package com.asphalt.resetpassword.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.repository.user.UserRepository
import com.asphalt.android.viewmodel.AuthViewModel
import com.asphalt.commonui.UIState
import com.asphalt.commonui.UIStateHandler
import com.asphalt.commonui.util.EmailValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    val authViewModel: AuthViewModel,
    val userAPIRepository: UserRepository
) : ViewModel() {
    private val _emailMutableState = MutableStateFlow("")
    val emailState: StateFlow<String> = _emailMutableState

    private val _isShowTick = MutableStateFlow(false)
    val isShowTick: StateFlow<Boolean> = _isShowTick

    private val _isShowError = MutableStateFlow(false)
    val isShowError: StateFlow<Boolean> = _isShowError

    private val _showSuccess = MutableStateFlow(false)
    val showSuccess: StateFlow<Boolean> = _showSuccess

    private val _showFailure = MutableStateFlow(false)
    val showFailure: StateFlow<Boolean> = _showFailure

    fun updateEmail(email: String) {
        _isShowError.value = false
        _emailMutableState.value = email
        _isShowTick.value = EmailValidator.isValid(email)
    }

    fun sendCode(): Boolean {
        if (validation()) {
            return true
        }
        return false
    }

    fun callRestPassword() {
        viewModelScope.launch {
            UIStateHandler.sendEvent(UIState.Loading)
            APIHelperUI.handleApiResult(
                userAPIRepository.getAllUsers(),
                viewModelScope
            ) { response ->

                if (response.any {
                        it.email.equals(_emailMutableState.value, ignoreCase = true)
                    }) {
                    val response = authViewModel.resetPassword(_emailMutableState.value)
                    if (response.isSuccess) {
                        _showSuccess.value = true
                        _showFailure.value = false
                    } else {
                        _showSuccess.value = false
                        _showFailure.value = true
                    }
                    UIStateHandler.sendEvent(UIState.DismissLoader)
                } else {
                    _showSuccess.value = false
                    _showFailure.value = true
                    UIStateHandler.sendEvent(UIState.DismissLoader)
                }

            }

        }
    }

    fun validation(): Boolean {
        if (_emailMutableState.value.isNullOrEmpty()) {
            _isShowError.value = true
            return false
        }
        if (!EmailValidator.isValid(_emailMutableState.value)) {
            _isShowError.value = true
            return false
        }
        _isShowError.value = false
        return true
    }

}
