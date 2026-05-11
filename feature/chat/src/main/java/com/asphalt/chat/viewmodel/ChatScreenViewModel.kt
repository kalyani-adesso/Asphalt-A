package com.asphalt.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.repository.chat.ChatRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.chat.model.ChatMessage
import com.asphalt.chat.model.ChatParamsModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatScreenViewModel(val androidUserVM: AndroidUserVM, val chatRepository: ChatRepository) :
    ViewModel() {
    private val currentUid: String?
        get() = androidUserVM.getCurrentUserUID()
    private val _chatMessage = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessage: StateFlow<List<ChatMessage>> = _chatMessage
    private var chatCollectionJob: Job? = null

    fun updateChatMessage(message: ChatMessage) {
        // _chatMessage.value = _chatMessage.value + message
        //_chatMessage.value = listOf(message) + _chatMessage.value
    }

    fun clearChat() {
        _chatMessage.value = emptyList()
    }

    fun getName(id: String): String {
        return androidUserVM.getUser(id)?.name ?: ""
    }

    fun getProfileImg(id: String): String {
        return androidUserVM.getUser(id)?.profilePic ?: ""
    }

    fun initialise1V1Chat(receiverID: String) {
        val chatRoomID = chatRepository.getCanonicalChatId(currentUid ?: "", receiverID)
        chatRepository.createOrGet1v1Chat(currentUid ?: "", receiverID)
        chatCollectionJob?.cancel()
        chatCollectionJob = viewModelScope.launch {
            chatRepository.getMessages(chatRoomID).collect { messages ->
                _chatMessage.value = messages.map {
                    ChatMessage(
                        it.text,
                        androidUserVM.getUser(it.senderId)?.name ?: "",
                        it.timestamp,
                        it.senderId == currentUid
                    )
                }.reversed()
                chatRepository.markAsRead(chatRoomID, currentUid ?: "")
            }
        }
    }

    fun send1V1Chat(receiverID: String, msg: String) {
        chatRepository.sendMessage(
            chatRepository.getCanonicalChatId(
                currentUid ?: "",
                receiverID
            ), currentUid ?: "", receiverID, msg
        )
    }

    fun initializeGroupChat(chatParams: ChatParamsModel) {
        chatRepository.createOrGetGroupChat(chatParams.members, chatParams.rideId, chatParams.title)
        chatCollectionJob?.cancel()
        chatCollectionJob = viewModelScope.launch {
            chatRepository.getMessages(chatParams.rideId).collect { messages ->
                _chatMessage.value = messages.map {
                    ChatMessage(
                        it.text,
                        androidUserVM.getUser(it.senderId)?.name ?: "",
                        it.timestamp,
                        it.senderId == currentUid
                    )
                }.reversed()
                chatRepository.markAsRead(chatParams.rideId, currentUid ?: "")
            }
        }
    }

    fun sendGroupChatMessage(chatParams: ChatParamsModel, message: String) {

        chatRepository.sendGroupMessage(
            chatParams.rideId,
            currentUid ?: "",
            message,
            chatParams.members
        )
    }

}