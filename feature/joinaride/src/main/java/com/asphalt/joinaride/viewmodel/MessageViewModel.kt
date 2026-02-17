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
}
