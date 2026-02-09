package com.asphalt.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.repository.chat.ChatRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.chat.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatScreenViewModel(val androidUserVM: AndroidUserVM, val chatRepository: ChatRepository) :
    ViewModel() {
    private val currentUid: String?
        get() = androidUserVM.getCurrentUserUID()
    private val _chatMessage = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessage: StateFlow<List<ChatMessage>> = _chatMessage

    fun updateChatMessage(message: ChatMessage) {
        // _chatMessage.value = _chatMessage.value + message
        //_chatMessage.value = listOf(message) + _chatMessage.value
    }

    fun clearChat() {
        _chatMessage.value = emptyList()
    }
    fun getName(id:String) : String{
        return androidUserVM.getUser(id)?.name ?: ""
    }
    fun initialise1V1Chat(receiverID: String) {
        chatRepository.createOrGet1v1Chat(currentUid ?: "", receiverID)
        viewModelScope.launch {
            chatRepository.getMessages(
                chatRepository.getCanonicalChatId(
                    currentUid ?: "",
                    receiverID
                )
            ).collect { it ->
                _chatMessage.value = it.map {
                    ChatMessage(
                        it.text,
                        androidUserVM.getUser(it.senderId)?.name ?: "",
                        it.timestamp,
                        it.senderId == currentUid
                    )
                }.reversed()
            }
        }

    }

    fun send1V1Chat(receiverID: String, msg: String) {
        chatRepository.sendMessage(
            chatRepository.getCanonicalChatId(
                currentUid ?: "",
                receiverID
            ), currentUid ?: "",receiverID, msg
        )
    }


}