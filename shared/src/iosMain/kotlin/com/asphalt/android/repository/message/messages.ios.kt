package com.asphalt.android.repository.message

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.message.MessageDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.cinterop.ExperimentalForeignApi

import cocoapods.FirebaseDatabase.FIRDatabase
import cocoapods.FirebaseDatabase.FIRDataSnapshot
import cocoapods.FirebaseDatabase.FIRDataEventType
import com.asphalt.android.model.message.MessageRoot
import platform.Foundation.NSDictionary
import platform.Foundation.allObjects
import kotlinx.coroutines.channels.awaitClose

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import platform.Foundation.NSNumber
import platform.Foundation.allObjects
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose

actual class MessageImpl {

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun receiveMessage(rideId: String): Flow<APIResult<List<MessageDTO>>> = callbackFlow {
        val dbRef = FIRDatabase.database().reference()
            .child("messages")
            .child(rideId)

        val listener = dbRef.observeEventType(
            FIRDataEventType.FIRDataEventTypeValue,
            withBlock = { snapshot ->

                if (snapshot == null || !snapshot.exists()) {
                    trySend(APIResult.Success(emptyList()))
                    return@observeEventType
                }

                val dtoList = mutableListOf< MessageDTO>()

                val childrenArray = snapshot.children.allObjects as? List<FIRDataSnapshot> ?: emptyList()

                for (child in childrenArray) {
                    val joinedId = child.key
                    val dict = child.value as? NSDictionary ?: continue
                    val root = dict.toMessageRoot()

                    val messageId = child.key

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
            },
            withCancelBlock = { error ->
                trySend(APIResult.Error(Exception(error?.localizedDescription ?: "Firebase error")))
            }
        )

        awaitClose {
            dbRef.removeObserverWithHandle(listener)
        }
    }
}

fun NSDictionary.toMessageRoot(): MessageRoot {
    return  MessageRoot(
       senderID = this.objectForKey("senderID") as? String,
       senderName = this.objectForKey("senderName") as? String,
       receiverID = this.objectForKey("receiverID") as? String,
        receiverName = this.objectForKey("receiverName") as? String,
        message = this.objectForKey("message") as? String,
        onGoingRideID = this.objectForKey("onGoingRideID") as? String,
        timeStamp = (this.objectForKey("timeStamp") as? NSNumber)?.longValue,
        isRideOnGoing = this.objectForKey("isRideOnGoing") as? Boolean
    )
}
