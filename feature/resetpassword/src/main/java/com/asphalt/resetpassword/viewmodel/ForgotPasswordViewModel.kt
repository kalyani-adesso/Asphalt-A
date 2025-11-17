package com.asphalt.resetpassword.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.repository.user.UserRepository
import com.asphalt.android.viewmodel.AuthViewModel
import com.asphalt.commonui.UIState
import com.asphalt.commonui.UIStateHandler
import com.asphalt.commonui.util.EmailValidator
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    val authViewModel: AuthViewModel,
    val userAPIRepository: UserRepository
) : ViewModel() {
    private val _emailMutableState: MutableState<String> = mutableStateOf("")
    val emailState: State<String> = _emailMutableState
    val isShowTick = mutableStateOf(false)
    val isShowError = mutableStateOf(false)

    val showSuccess = mutableStateOf(false)
    val showFailure = mutableStateOf(false)
    val showLoader = mutableStateOf(false)

    fun updateEmail(email: String) {
        isShowError.value = false
        _emailMutableState.value = email
        isShowTick.value = EmailValidator.isValid(email)
    }

    fun sendCode(): Boolean {
        if (validation()) {
            return true
        }
        return false
    }

    fun callRestPassword() {
        viewModelScope.launch {
           // showLoader.value = true
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
                        showSuccess.value = true
                        showFailure.value = false
                    } else {
                        showSuccess.value = false
                        showFailure.value = true
                    }
                    UIStateHandler.sendEvent(UIState.DismissLoader)
                } else {
                    showSuccess.value = false
                    showFailure.value = true
                    UIStateHandler.sendEvent(UIState.DismissLoader)
                }

            }

        }


    }

    fun validation(): Boolean {
        if (_emailMutableState.value.isNullOrEmpty()) {
            isShowError.value = true
            return false
        }
        if (!EmailValidator.isValid(_emailMutableState.value)) {
            isShowError.value = true
            return false
        }
        isShowError.value = false
        return true
    }

}