package com.asphalt.resetpassword.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class VerifyCodeViewModel : ViewModel() {
    private val _verificationCodeMutable = mutableStateOf("")
    val verificationCode: State<String> = _verificationCodeMutable

    val isShowError = mutableStateOf(false)

    fun updateCode(code: String) {
        isShowError.value = false
        _verificationCodeMutable.value = code

    }

    fun verifyCode(): Boolean {
        if (validation()) {
            return true
        }
        return false
    }

    fun validation(): Boolean {
        if (_verificationCodeMutable.value.isNullOrEmpty()) {
            isShowError.value = true
            return false;
        }
        isShowError.value = false
        return true
    }
}