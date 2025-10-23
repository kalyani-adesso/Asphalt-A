package com.asphalt.registration.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.datastore.DataStoreManager
import com.asphalt.android.model.User
import com.asphalt.android.viewmodel.AuthViewModel
import com.asphalt.commonui.util.EmailValidator
import com.asphalt.commonui.util.ValidationResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationDetailsViewModel(
    val authViewModel: AuthViewModel,
    val datastore: DataStoreManager) : ViewModel() {
  //  private var signUpInfo = SignUpInfo()
    private val _effect = MutableSharedFlow<RegistrationDetailsEffect>()
    private val effect: SharedFlow<RegistrationDetailsEffect> = _effect
    private val _signupState = MutableStateFlow(SignUpInfo())
    val signupState: StateFlow<SignUpInfo> = _signupState

    fun onFieldChange(
        user: User
    ){
        _signupState.value = _signupState.value.copy(
            fullName = user.name,
            email = user.email,
            password = user.password,
            confirmPassword = user.confirmPassword,
            errorMessage = null
        )
    }
    fun onSignupClick(user: User) {

        val validationResult = ValidateInputs(
            user = user
        )
        viewModelScope.launch {
            _signupState.value = _signupState.value.copy(isLoading = true)

            if (!validationResult.successful) {
                _signupState.value = _signupState.value.copy(errorMessage = validationResult.errorMessage)
                return@launch
            }

            _signupState.value = _signupState.value.copy(
                fullName = user.name, email = user.email,
                password = user.password, confirmPassword = user.confirmPassword
            )


//            _signupState.value = SignUpInfo.
//
//            val data = authViewModel.SignUp(user)
//            if (data.isSuccess) {
//                _signupState.value = SignUpUiState.Success(data = _signupState.value)
//                Log.d("TAG", "onSignupClick: data user ${data.isSuccess}")
//            }
//            else if (data.isFailure){
//                val errorMessage = data.isFailure
//                _signupState.value = SignUpUiState.Error(
//                    _signupState.value.copy(errorMessage = true, errorTitle = errorMessage.toString())
//                )
//
//            }

        }
    }


    fun onBackPressed() {
        viewModelScope.launch {
            _effect.emit(RegistrationDetailsEffect.Back)
        }
    }

    fun onContinueClick(registrationId: String, registrationCode: String) {
        viewModelScope.launch {
            _effect.emit(
                RegistrationDetailsEffect.ToSignBack
            )
        }
    }

    suspend fun handleNavigation(
        onBackPressed: () -> Unit,
        // onNavigateToRegistrationPassword: (String, String) -> Unit
        onNavigateToLogin: () -> Unit
    ) {
        effect.collect { effect ->
            when (effect) {
                RegistrationDetailsEffect.Back -> onBackPressed()
                is RegistrationDetailsEffect.ToSignBack
                    -> onNavigateToLogin()
            }
        }
    }
    fun ValidateInputs(
        user: User) : ValidationResult {

        if (user.name.isBlank() || user.email.isBlank()
            || user.password.isBlank() || user.confirmPassword.isBlank()) {
            return ValidationResult(false,"All fields are required")
        }
        if (!EmailValidator.isValid(email = user.email)) {
            return ValidationResult(false,"Please Enter valid email address")
        }
        if (user.password.length < 6) {
            return ValidationResult(false,"Password must be at least 6 characters long")
        }
        if (user.password != user.confirmPassword) {
            return ValidationResult(false,"Password do not match")
        }
        return ValidationResult(true)
    }
}

data class SignUpInfo(
    val fullName : String = "",
    val email : String = "",
    val  password : String = "",
    val confirmPassword : String = "",

    val isLoading : Boolean = false,
    val isSuccess : Boolean = false,
    val errorMessage: String? = null,
)

//sealed interface SignUpUiState {
//
//    object Idle : SignUpUiState
//    object Loading : SignUpUiState
//    data class Success(val data: SignUpInfo) : SignUpUiState
//    data class Error(val errorMessage: SignUpInfo) : SignUpUiState
//}

sealed class RegistrationDetailsEffect {
    data object Back : RegistrationDetailsEffect()
    data object ToSignBack : RegistrationDetailsEffect()
}