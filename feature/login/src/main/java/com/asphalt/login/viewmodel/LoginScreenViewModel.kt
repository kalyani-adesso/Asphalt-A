package com.asphalt.login.viewmodel

import androidx.lifecycle.ViewModel
import com.asphalt.commonui.util.EmailValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginScreenViewModel : ViewModel() {
    private val _emailTextMutableState = MutableStateFlow("")
    val _emailTextState: StateFlow<String> = _emailTextMutableState
    val _isEmailVaild = MutableStateFlow(false)


    fun updateEmailState(email: String) {
        _emailTextMutableState.value = email
        _isEmailVaild.value = EmailValidator.isValid(email)
    }

}