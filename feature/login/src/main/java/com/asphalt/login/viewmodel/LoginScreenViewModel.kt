package com.asphalt.login.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.datastore.DataStoreManager
import com.asphalt.android.model.CurrentUser
import com.asphalt.android.viewmodel.AuthViewModel
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.PreferenceKeys
import com.asphalt.commonui.util.EmailValidator
import com.asphalt.login.model.LoginValidationModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class LoginScreenViewModel(val authViewModel: AuthViewModel, val datastore: DataStoreManager,val androidUserVM: AndroidUserVM) :
    ViewModel() {

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
    val isrememberMe = mutableStateOf(false)


    fun updateMessage(boolean: Boolean) {
        showFailureMessage.value = boolean
    }

    fun updateLoader(boolean: Boolean) {
        showLoader.value = boolean
    }

    fun resetLogin() {
        isLoginSuccess.value = false
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
                var loginresponse = authViewModel.signIn(
                    _emailTextMutableState.value,
                    _passwordTextMutableState.value
                )
                if (loginresponse.isSuccess) {
                    var user = with(loginresponse) {
                        CurrentUser(isSuccess, errorMessage, name, email, uid)
                    }
                   androidUserVM.updateUserData(user)
                    datastore.saveValue(PreferenceKeys.REMEMBER_ME, isrememberMe.value)
                    //println("isRemberMe: ${isrememberMe.value}")
                    _emailTextMutableState.value = ""
                    _passwordTextMutableState.value = ""
                    isEmailVaild.value = false
                    updateMessage(false)
                    updateLoader(false)
                    isLoginSuccess.value = true
                } else {
                    isLoginSuccess.value = false
                    updateLoader(false)
                    updateMessage(true)
                }

                Log.d(
                    "Login",
                    "${loginresponse.isSuccess}  ${datastore.getValue(PreferenceKeys.USER_DETAILS)}"
                )
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