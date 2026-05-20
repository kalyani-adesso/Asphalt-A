package com.asphalt.android.repository.message

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.message.MessageDTO
import com.asphalt.android.model.message.MessageRoot
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

actual class MessageImpl {
    // Flow allows streaming updates - every time a new message is added in Firebase, your UI can automatically receive it.
    // fetches messages for a specific ride from Firebase Realtime Database & returns a Flow<APIResult<List<MessageDTO>>>
    actual suspend fun receiveMessage(rideId: String): Flow<APIResult<List<MessageDTO>>> = callbackFlow{

        // Connects to Firebase Realtime Database.  //Navigates to the node: messages/{rideId}.
        val dbRef = FirebaseDatabase.getInstance().reference
            .child("messages")
            .child(rideId)

        //listens to all changes under /messages/{rideId}
        val listener = object : ValueEventListener { // ValueEventListener - continuous updates
            // onDataChange - Immediately with current data.  Every time data changes (new message added, edited, or deleted)
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    trySend(APIResult.Success(emptyList()))
                    return
                }
                val dtoList = mutableListOf<MessageDTO>()

                for (child in snapshot.children) {
                    val dict = child.value as? Map<*, *> ?: continue

                    val messageId = child.key ?: continue

                    val root = dict.toMessageRoot()

                    dtoList.add(
                        MessageDTO(
                            id = messageId,
                            senderID = root.senderID ?: "",
                            senderName = root.senderName ?: "",
                            receiverID = root.receiverID ?: "",
                            receiverName = root.receiverName ?: "",
                            message = root.message ?: "",
                            onGoingRideID = root.onGoingRideID ?: "",
                            timeStamp = root.timeStamp ?: 0L,
                            isRideOnGoing = root.isRideOnGoing ?: false
                        )
                    )
                }
                trySend(APIResult.Success(dtoList))
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        dbRef.addValueEventListener(listener)

        awaitClose {
            dbRef.removeEventListener(listener)
        }
    }
}
// Extension function to convert Map<String, Any?> to MessageRoot
fun Map<*, *>.toMessageRoot(): MessageRoot {
    return MessageRoot(
        senderID = this["senderID"] as? String,
        senderName = this["senderName"] as? String,
        receiverID = this["receiverID"] as? String,
        receiverName = this["receiverName"] as? String,
        message = this["message"] as? String,
        onGoingRideID = this["onGoingRideID"] as? String,
        timeStamp = (this["timeStamp"] as? Long) ?: 0L,
        isRideOnGoing = this["isRideOnGoing"] as? Boolean ?: false
    )
}