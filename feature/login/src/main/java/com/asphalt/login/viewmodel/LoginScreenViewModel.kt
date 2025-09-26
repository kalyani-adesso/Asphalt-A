package com.asphalt.login.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.asphalt.commonui.util.EmailValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginScreenViewModel : ViewModel() {
    private val _emailTextMutableState = MutableStateFlow("")

    val emailTextState: StateFlow<String> = _emailTextMutableState

    private val _passwordTextMutableState = MutableStateFlow("")
    val passwordTextState = _passwordTextMutableState
    val isEmailVaild = MutableStateFlow(false)


    fun updateEmailState(email: String) {
        _emailTextMutableState.value = email
        isEmailVaild.value = EmailValidator.isValid(email)
    }

    fun updatePassword(password: String) {
        _passwordTextMutableState.value = password

    }


}