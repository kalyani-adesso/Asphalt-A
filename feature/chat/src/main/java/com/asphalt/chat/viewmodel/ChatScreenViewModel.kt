package com.asphalt.chat.viewmodel

import androidx.lifecycle.ViewModel
import com.asphalt.chat.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatScreenViewModel : ViewModel() {
    private val _chatMessage = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessage: StateFlow<List<ChatMessage>> = _chatMessage

    fun updateChatMessage(message: ChatMessage) {
       // _chatMessage.value = _chatMessage.value + message
        _chatMessage.value = listOf(message) + _chatMessage.value
    }
}