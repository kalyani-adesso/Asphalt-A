package com.asphalt.registration.viewmodel

import androidx.lifecycle.ViewModel

class RegistrationDetailsViewModel : ViewModel() {




}

sealed interface RegistrationDetailsUiState {
    object Loading : RegistrationDetailsUiState
    data class Success(val message: String) : RegistrationDetailsUiState
    data class Error(val errorMessage: String) : RegistrationDetailsUiState
}

sealed class RegistrationDetailsEffect {
    data object Back : RegistrationDetailsEffect()
    data class ToRegistrationPassword(val id: String) : RegistrationDetailsEffect()
}