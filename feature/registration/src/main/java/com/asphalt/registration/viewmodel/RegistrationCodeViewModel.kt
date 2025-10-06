package com.asphalt.registration.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegistrationCodeViewModel : ViewModel() {
    private val _uiState =
        MutableStateFlow<RegistrationCodeUiState>(RegistrationCodeUiState.Success(""))
    val uiState: StateFlow<RegistrationCodeUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<RegistrationCodeEffect>()
    private val effect: SharedFlow<RegistrationCodeEffect> = _effect

    fun onBackPressed() {
        viewModelScope.launch {
            _effect.emit(RegistrationCodeEffect.Back)
        }
    }

    suspend fun handleNavigation(
        onBackPressed: () -> Unit,
        onNavigateToRegistrationPassword: (String) -> Unit
    ) {
        effect.collect { effect ->
            when (effect) {
                is RegistrationCodeEffect.Back -> onBackPressed()
                is RegistrationCodeEffect.ToRegistrationPassword
                    -> onNavigateToRegistrationPassword(effect.id)
            }
        }
    }

    fun onContinueClick(registrationId: String) {
        viewModelScope.launch {
            _effect.emit(
                RegistrationCodeEffect.ToRegistrationPassword(
                    registrationId
                )
            )
        }
    }
}

sealed interface RegistrationCodeUiState {
    object Loading : RegistrationCodeUiState
    data class Success(val message: String) : RegistrationCodeUiState
    data class Error(val errorMessage: String) : RegistrationCodeUiState
}

sealed class RegistrationCodeEffect {
    data object Back : RegistrationCodeEffect()
    data class ToRegistrationPassword(val id: String) : RegistrationCodeEffect()
}