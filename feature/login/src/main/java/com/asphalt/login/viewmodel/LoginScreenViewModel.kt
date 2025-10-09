package com.asphalt.login.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.viewmodel.AuthViewModel
import com.asphalt.commonui.R
import com.asphalt.commonui.util.EmailValidator
import com.asphalt.login.model.LoginValidationModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

class LoginScreenViewModel (val authViewModel: AuthViewModel) : ViewModel() {

    private val _emailTextMutableState = MutableStateFlow("")

    val emailTextState: StateFlow<String> = _emailTextMutableState

    private val _passwordTextMutableState = MutableStateFlow("")
    val passwordTextState: StateFlow<String> = _passwordTextMutableState

    private val _validationMutableState = MutableStateFlow(LoginValidationModel())
    val validateState: StateFlow<LoginValidationModel> = _validationMutableState
    val isEmailVaild = MutableStateFlow(false)

    val isLoginSuccess = MutableStateFlow(false)
    val showFailureMessage = MutableStateFlow(false)
    val showLoader = MutableStateFlow(false)

    fun updateMessage(boolean: Boolean){
        showFailureMessage.value=boolean
    }

    fun updateLoader(boolean: Boolean){
        showLoader.value=boolean
    }

    fun resetLogin(){
        isLoginSuccess.value =false
    }
    fun updateEmailState(email: String) {
        _emailTextMutableState.value = email
        isEmailVaild.value = EmailValidator.isValid(email)
        if (EmailValidator.isValid(email)) {
            clearValidation()
        }
    }

    fun updatePassword(password: String) {
        _passwordTextMutableState.value = password

    }

    fun callLogin() {
        if (fieldValidation()) {
            updateLoader(true)
            viewModelScope.launch {
               var loginresponse =authViewModel.signIn(_emailTextMutableState.value,_passwordTextMutableState.value)
                if(loginresponse.isSuccess){
                    updateMessage(false)
                    updateLoader(false)
                    isLoginSuccess.value = true
                }else{
                    isLoginSuccess.value = false
                    updateLoader(false)
                    updateMessage(true)
                }
                Log.d("Login","${loginresponse.isSuccess}")
            }

        }

    }

    fun fieldValidation(): Boolean {
        if (_emailTextMutableState.value.isEmpty()) {
            _validationMutableState.value = _validationMutableState.value.copy(
                isShowEmailError = true,
                emailError = R.string.enter_valid_email
            )
            return false
        }
        if (!EmailValidator.isValid(_emailTextMutableState.value)) {
            _validationMutableState.value = _validationMutableState.value.copy(
                isShowEmailError = true,
                emailError = R.string.enter_valid_email
            )
            return false
        }
        if (_passwordTextMutableState.value.isEmpty()) {
            _validationMutableState.value = _validationMutableState.value.copy(
                isShowPasswordError = true,
                passwordError = R.string.enter_valid_password
            )
            return false
        }
        clearValidation()
        return true
    }

    fun clearValidation() {
        _validationMutableState.value = _validationMutableState.value.copy(
            isShowEmailError = false,
            isShowPasswordError = false,
        )
    }


}