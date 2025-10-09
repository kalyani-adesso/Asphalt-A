package com.asphalt.registration.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegistrationPasswordViewModel : ViewModel() {

    private var registrationInfo = RegistrationInfo()
    private val _uiState =
        MutableStateFlow<RegistrationPasswordUiState>(
            RegistrationPasswordUiState.Success(
                registrationInfo
            )
        )
    val uiState: StateFlow<RegistrationPasswordUiState> = _uiState.asStateFlow()
    private val _effect = MutableSharedFlow<RegistrationPasswordEffect>()
    val effect: SharedFlow<RegistrationPasswordEffect> = _effect

    suspend fun handleNavigation(
        onBackPressed: () -> Unit = {},
        onNavigationToPostRegistrationDetails: (String) -> Unit = {}
    ) {
        effect.collect { effect ->
            when (effect) {
                is RegistrationPasswordEffect.Back -> onBackPressed()
                is RegistrationPasswordEffect.ToRegistrationDetails
                    -> {
                    onNavigationToPostRegistrationDetails(effect.id)
                }
            }
        }
    }
    fun onContinueClick(verificationCode: String) {
        viewModelScope.launch {
            _effect.emit(
                value = RegistrationPasswordEffect.ToRegistrationDetails(
                    id = verificationCode
                )
            )
        }
    }
}

sealed interface RegistrationPasswordUiState {
    object Loading : RegistrationPasswordUiState
    data class Success(val registrationInfo: RegistrationInfo) : RegistrationPasswordUiState
    data class Error(val errorMessage: String) : RegistrationPasswordUiState
}

data class RegistrationInfo(
    val isSuccessful: Boolean = false,
    val id: String = "",
    val code: String = "",
    val password: String = "",
    val confirmPassword: String = ""
)

sealed class RegistrationPasswordEffect {
    data object Back : RegistrationPasswordEffect()
    data class ToRegistrationDetails(val id: String) : RegistrationPasswordEffect()
}