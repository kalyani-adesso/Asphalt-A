package com.asphalt.joinaride.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.model.chat.Message
import com.asphalt.android.model.message.MessageRoot
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase.getInstance
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class MessageViewModel(private val ridesRepository: RidesRepository) : ViewModel(), KoinComponent {

    val androidUserVM: AndroidUserVM by inject()
    private val rootMessageRef = getInstance().getReference("messages")
    val currentUid: String?
        get() = androidUserVM.userState.value?.uid
    val currentUser: String?
        get() = androidUserVM.userState.value?.name

    private var messageRef: DatabaseReference? = null

    private val _customMessage = MutableStateFlow("")
    val customMessage: StateFlow<String> = _customMessage
    private val _messagesList = MutableStateFlow<List<MessageRoot>>(emptyList())
    val messagesList: StateFlow<List<MessageRoot>> = _messagesList
    fun onQuickMessageClick(message: String) {
        _customMessage.value = message
    }
    fun onCustomMessageChange(message: String) {
        _customMessage.value = message
    }
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
            // api called
            ridesRepository.sendMessage(messageRoot)
            // clear input
            _customMessage.value = ""
        }
    }
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private var messageListener: ValueEventListener? = null

    // LISTEN FOR LIVE MESSAGES
    fun listenForMessages(rideId: String, recevierId: String) {
        messageListener?.let { messageRef?.removeEventListener(it) }

        val ref = rootMessageRef.database.getReference("messages/$rideId")
        val newListener = object : ValueEventListener {

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
        }
        messageRef = ref
        messageListener = newListener
        ref.addValueEventListener(newListener)
    }

    override fun onCleared() {
        val listener = messageListener
        val ref = messageRef
        listener?.let {
            ref?.removeEventListener(it)
        }
        super.onCleared()
    }
}