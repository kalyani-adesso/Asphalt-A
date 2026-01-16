package com.asphalt.android.repository.chat

import com.asphalt.android.DataSnapshot
import com.asphalt.android.FirebaseServerValue
import com.asphalt.android.Logger
import com.asphalt.android.PlatformDatabase
import com.asphalt.android.TransactionResult
import com.asphalt.android.model.chat.Message
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepository {
    private val database = PlatformDatabase()

    fun getCanonicalChatId(uid1: String, uid2: String): String {
        return if (uid1 < uid2) "${uid1}_${uid2}" else "${uid2}_${uid1}"
    }

    fun createOrGet1v1Chat(userAId: String, userBId: String) {
        val chatRoomId = getCanonicalChatId(userAId, userBId)
        val chatRef = database.getReference("chats").child(chatRoomId)

        chatRef.runTransaction { currentSnapshot ->
            if (currentSnapshot.getValue() == null) {
                val newChatData = mapOf(
                    "type" to "private",
                    "members" to mapOf(userAId to true, userBId to true)
                )
                Logger.d("IF_Success","Success" + currentSnapshot.getValue())
                return@runTransaction TransactionResult.success(newChatData)
            } else {
                Logger.d("IF_Fail","Fail"+currentSnapshot.getValue())
                val existingData = currentSnapshot.getValue() as? Map<String, Any> ?: emptyMap()
                return@runTransaction TransactionResult.success(existingData)
            }
        }
    }

    fun sendMessage(chatRoomId: String, senderId: String, text: String) {
        val messagesRef = database.getReference("chats/$chatRoomId/messages").push()
        val messageId = messagesRef.key ?: return

        val timestamp = FirebaseServerValue.TIMESTAMP


        val messageData = mapOf(
            "senderId" to senderId,
            "text" to text,
            "timestamp" to timestamp
        )


        val updates = mapOf(
            "chats/$chatRoomId/messages/$messageId" to messageData,
            "chats/$chatRoomId/lastMessage" to text,
            "chats/$chatRoomId/lastTimestamp" to timestamp
        )

        database.getReference(null).updateChildren(updates)
    }
    private fun mapSnapshotToMessages(snapshot: DataSnapshot): List<Message> {
        return snapshot.children.mapNotNull { child ->
            val map = child.getValue() as? Map<String, Any?> ?: return@mapNotNull null

            Message(
                id = child.key ?: "",
                senderId = map["senderId"] as? String ?: "",
                text = map["text"] as? String ?: "",
                timestamp = (map["timestamp"] as? Long) ?: 0L,
                isRead = map["isRead"] as? Boolean ?: false
            )
        }.sortedBy { it.timestamp }
    }

    fun getMessages(chatRoomId: String): Flow<List<Message>> {
        return database.getReference("chats/$chatRoomId/messages")
            .observeValue().map {
                snapshot ->
                mapSnapshotToMessages(snapshot)

            }
    }
}