package com.asphalt.joinaride.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {

    private val _customMessage = MutableStateFlow("")
    val customMessage = _customMessage.asStateFlow()

    // To trigger Snackbar message event
    private val _sentMessageEvent = MutableSharedFlow<String>()
    val sentMessageEvent = _sentMessageEvent.asSharedFlow()

    // Update the custom input message
    fun onCustomMessageChange(newMsg: String) {
        _customMessage.value = newMsg
    }

    // Called when user taps send or quick message button
    fun sendMessage(message: String) {
        if (message.isNotBlank()) {
            // In real app, send message to server here
            viewModelScope.launch {
                _sentMessageEvent.emit(message)
            }
            _customMessage.value = "" // reset input
        }
    }

    // Called on cancel, just clear input
    fun cancel() {
        _customMessage.value = ""
    }

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
