package com.asphalt.resetpassword.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.asphalt.commonui.util.EmailValidator

class ForgotPasswordViewModel : ViewModel() {
    private val _emailMutableState: MutableState<String> = mutableStateOf("")
    val emailState: State<String> = _emailMutableState
    val isShowTick = mutableStateOf(false)
    val isShowError = mutableStateOf(false)

    fun updateEmail(email: String) {
        isShowError.value = false
        _emailMutableState.value = email
        isShowTick.value = EmailValidator.isValid(email)
    }

    fun sendCode() : Boolean {
        if (validation()) {
            return true
        }
        return false
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