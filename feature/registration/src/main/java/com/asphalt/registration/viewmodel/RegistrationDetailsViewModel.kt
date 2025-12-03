package com.asphalt.registration.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.model.User
import com.asphalt.android.viewmodel.AuthViewModel
import com.asphalt.commonui.util.EmailValidator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception


class RegistrationDetailsViewModel(private val authViewModel: AuthViewModel) : ViewModel(){
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState : StateFlow<SignUpUiState> = _uiState
    private val _eventFlow = MutableSharedFlow<SignUpUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
    val showLoader = MutableStateFlow(false)
    fun updateLoader(boolean: Boolean) {
        showLoader.value = boolean
    }
    fun onEvent(event: SignUpUiEvent) {
        when(event) {
            is SignUpUiEvent.FullNameChagned -> _uiState.update { it.copy(fullName = event.value, fullNameError = null) }
            is SignUpUiEvent.EmailChagned -> _uiState.update { it.copy(email = event.value, emailError = null) }
            is SignUpUiEvent.PasswordChagned -> _uiState.update { it.copy(password = event.value, passwordError = null) }
            is SignUpUiEvent.CofirmPasswordChagned -> _uiState.update { it.copy(confirmPassword = event.value, confirmPasswordError = null) }
            SignUpUiEvent.clearMessage -> _uiState.update { it.copy(errorMessage = null, successMessage = null) }
            SignUpUiEvent.Submit -> AccountCreationClick()
            is SignUpUiEvent.Error -> "Failure"
            is SignUpUiEvent.Success -> "Success"
        }
    }

    fun validateAllFields() : ValidationResult {
        val state = _uiState.value

        val fullNameErr = if (state.fullName.isBlank()) FieldsError("Full Name is Required") else null

        val emailErr = when {
            state.email.isBlank() -> FieldsError("EMail is Required")
            !EmailValidator.isValid(email = state.email) -> FieldsError( "Enter Valid email")
            else -> null
        }
        val passwordErr = when {
            state.password.isBlank() -> FieldsError("Please Enter Password")
            state.password.length < 6 -> FieldsError("Password must be at least 6 characters")
            else -> null
        }
        val confirmPasswordErr = when {
            state.confirmPassword.isBlank() -> FieldsError("Confirm password is required")
            state.password != state.confirmPassword -> FieldsError("Password do not match")
            else -> null
        }
        val result = ValidationResult(
            isValid = listOf(fullNameErr,emailErr,passwordErr,confirmPasswordErr).all { it == null },
            fullNameError = fullNameErr,
            emailError = emailErr,
            passwordError = passwordErr,
            confirmPasswordError = confirmPasswordErr
        )
        // update UI state with fields errors and clear top level messages
        _uiState.update {
            it.copy(
                fullNameError = result.fullNameError,
                emailError = result.emailError,
                passwordError = result.passwordError,
                confirmPasswordError = result.confirmPasswordError,
                errorMessage = null,
                successMessage = null
            )
        }
        return result
    }

    fun AccountCreationClick() {
        updateLoader(true)
        // run validation once via the single function
        val validation = validateAllFields()
        if (!validation.isValid) {updateLoader(false)
            return}

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

            val user = User(
                email = _uiState.value.email.trim(),
                name = _uiState.value.fullName.trim(),
                password = _uiState.value.password,
                confirmPassword = _uiState.value.confirmPassword
            )
            try {

                val response = authViewModel.SignUp(user)
                if (response.isSuccess){
                    _uiState.update { it.copy(isLoading = false, successMessage = "Account Created Successfully") }
                    _eventFlow.emit(SignUpUiEvent.Success("Account Created Successfully"))
                    updateLoader(false)
                }
                else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Registration Failed") }
                    _eventFlow.emit(SignUpUiEvent.clearMessage)
                    _uiState.value.errorMessage = response.isFailure.toString()
                    updateLoader(false)
                }
            }catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _eventFlow.emit(SignUpUiEvent.Error(e.message ?: "Unknown error"))
                updateLoader(false)
            }
        }
    }
}
data class FieldsError(val message:String?)

data class SignUpUiState(
    val fullName : String = "",
    val email : String = "",
    val password : String = "",
    val confirmPassword : String = "",

    val fullNameError : FieldsError? = null,
    val emailError : FieldsError? = null,
    val passwordError : FieldsError? = null,
    val confirmPasswordError : FieldsError? = null,

    val isLoading : Boolean = false,
    val successMessage: String? = null,
    var errorMessage: String? = null
)

data class ValidationResult(
    val isValid : Boolean,
    val fullNameError : FieldsError? = null,
    val emailError : FieldsError? = null,
    val passwordError : FieldsError? = null,
    val confirmPasswordError : FieldsError? = null

)

sealed interface SignUpUiEvent {
    data class FullNameChagned(val value: String) : SignUpUiEvent
    data class EmailChagned(val value: String) : SignUpUiEvent
    data class PasswordChagned(val value: String) : SignUpUiEvent
    data class CofirmPasswordChagned(val value: String) : SignUpUiEvent
    object Submit : SignUpUiEvent
    object clearMessage : SignUpUiEvent
    data class Success(val success: String) : SignUpUiEvent
    data class Error(val message:String) : SignUpUiEvent
}