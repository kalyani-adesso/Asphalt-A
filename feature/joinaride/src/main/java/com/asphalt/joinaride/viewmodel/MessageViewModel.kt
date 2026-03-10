package com.asphalt.joinaride.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.model.chat.Message
import com.asphalt.android.model.message.MessageRoot
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.FirebaseDatabase.getInstance
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class MessageViewModel(private val ridesRepository: RidesRepository) : ViewModel(), KoinComponent {

    val androidUserVM: AndroidUserVM by inject()
    private val messageRef = getInstance().getReference("messages")

    val currentUid: String?
        get() = androidUserVM.userState.value?.uid

    val currentUser: String?
        get() = androidUserVM.userState.value?.name

    var customMessage by mutableStateOf("")
        private set

    private val _uiState = MutableStateFlow(MessageRoot())
    val uiState: StateFlow<MessageRoot> = _uiState.asStateFlow()

    private val _messagesList = MutableStateFlow<List<MessageRoot>>(emptyList())
    val messagesList: StateFlow<List<MessageRoot>> = _messagesList

    fun onQuickMessageClick(message: String) {
        customMessage = message
    }
    fun onCustomMessageChange(message: String) {
        customMessage = message
    }

    var receiverName by mutableStateOf("")
        private set

    var recevierId by mutableStateOf("")
    private set
    fun sendMessage(
        senderID: String, // current user id
        senderName: String, // current user name
        receiverID: String, // riderId who joined
        receiverName: String, // ridername
        onGoingRideID: String, // ongoingRideId
        isRideOnGoing: Boolean, // true
        message: String
    ) {

        if (message.isBlank()) return

        viewModelScope.launch {

            //val messageId = messageRef.push().key ?: return@launch

            val messageRoot = MessageRoot(
                senderID = senderID,
                senderName = senderName,
                receiverID = receiverID,
                receiverName = receiverName,
                message = message,
                onGoingRideID = onGoingRideID,
                timeStamp = System.currentTimeMillis(),
                isRideOnGoing = isRideOnGoing,
            )
            //messageRef.setValue(messageRoot)
            // save to firebase
//            messageRef
//                .child(onGoingRideID)
//                .setValue(messageRoot)
//                .await()

            // api called
            ridesRepository.sendMessage(messageRoot)
            // clear input
            customMessage = ""

//            _uiState.update {
//                it.copy(
//                    message = "",
//                    senderID = senderID,
//                )
//            }
        }
    }
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    // LISTEN FOR LIVE MESSAGES
    fun listenForMessages(onGoingRideId: String, recevierId: String) {

        val ref = messageRef.database.getReference("messages/$onGoingRideId")

        Log.d("TAG", "listenForMessages rideId: $onGoingRideId")

        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val list = mutableListOf<MessageRoot>()

                Log.d("TAG", "onDataChange snapshot: $snapshot")

                for (child in snapshot.children) {

                    val msg = child.getValue(MessageRoot::class.java)

                    Log.d("TAG", "Parsed message: $msg")

                    msg?.let {
                        if ((it.receiverID == recevierId && it.senderID == currentUid) ||
                            (it.receiverID == currentUid && it.senderID == recevierId))  {
                            list.add(it)
                        }
                    }
                }
                _messagesList.value = list.sortedBy { it.timeStamp }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "listenForMessages cancelled", error.toException())
            }
        })
    }
}