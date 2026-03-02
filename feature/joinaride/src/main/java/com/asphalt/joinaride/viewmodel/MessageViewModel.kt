package com.asphalt.joinaride.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.joinaride.message.MessageDriverEvent
import com.asphalt.joinaride.message.MessageDriverUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MessageDriverUiState())
    val uiState: StateFlow<MessageDriverUiState> = _uiState.asStateFlow()

    fun onEvent(event: MessageDriverEvent) {
        when (event) {

            is MessageDriverEvent.OnQuickMessageClick -> {
                _uiState.value = _uiState.value.copy(
                    customMessage = event.message
                )
            }

            is MessageDriverEvent.OnCustomMessageChange -> {
                _uiState.value = _uiState.value.copy(
                    customMessage = event.message
                )
            }

            MessageDriverEvent.OnSendClick -> {
                sendMessage()
            }

            MessageDriverEvent.OnCancelClick -> {
                _uiState.value = _uiState.value.copy(
                    customMessage = ""
                )
            }
        }
    }

    private fun sendMessage() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSending = true)

            // Simulate API call
            delay(1500)

            _uiState.value = _uiState.value.copy(
                isSending = false,
                customMessage = ""
            )
        }
    }
}
