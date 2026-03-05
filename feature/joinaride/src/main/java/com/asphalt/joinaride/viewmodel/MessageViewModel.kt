package com.asphalt.joinaride.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.model.message.MessageRoot
import com.asphalt.android.repository.rides.RidesRepository
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.FirebaseDatabase.getInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MessageViewModel(private val ridesRepository: RidesRepository) : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val messageRef = getInstance().getReference("messages")


    var customMessage by mutableStateOf("")
        private set

    var isSending by mutableStateOf(false)
        private set



    fun onMessageChange(message: String) {
        customMessage = message
    }

    fun clearMessage() {
        customMessage = ""
    }

    private val _uiState = MutableStateFlow(MessageRoot())
    val uiState: StateFlow<MessageRoot> = _uiState.asStateFlow()

    fun onQuickMessageClick(message: String) {
        _uiState.update {
            it.copy(message = message)
        }
    }

    fun onCustomMessageChange(message: String) {
        _uiState.update {
            it.copy(message = message)
        }
    }
    fun sendMessage(
        senderID: String,
        senderName: String,
        receiverID: String,
        receiverName: String,
        onGoingRideID: String,
        isRideOnGoing: Boolean
    ) {

        val text = _uiState.value.message
        if (text!!.isBlank()) return

        viewModelScope.launch {

            //_uiState.update { it.copy(isLoading = true) }

            val messageId = messageRef.push().key ?: return@launch

            val messageRoot = MessageRoot(
                senderID = senderID,
                senderName = senderName,
                receiverID = receiverID,
                receiverName = receiverName,
                message = text,
                onGoingRideID = onGoingRideID,
                timeStamp = System.currentTimeMillis(),
                isRideOnGoing = true
            )
            messageRef
                .child(messageId)
                .setValue(messageRoot)
                .await()

            customMessage = ""

            try {
                // API call
                ridesRepository.sendMessage(messageRoot)
                // Update header message
                _uiState.update {
                    it.copy(
                        message = text,
                        senderID = "",
                    )
                }

            } catch (e: Exception) {
                Log.d("TAGGGGG", "sendMessage: ${e.localizedMessage}")
            }
        }
    }
}