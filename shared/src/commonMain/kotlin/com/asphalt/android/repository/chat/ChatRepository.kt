package com.asphalt.android.repository.chat

import com.asphalt.android.DataSnapshot
import com.asphalt.android.FirebaseServerValue
import com.asphalt.android.PlatformDatabase
import com.asphalt.android.TransactionResult
import com.asphalt.android.model.chat.ChatRoom
import com.asphalt.android.model.chat.Message
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
        val existingData = currentSnapshot.getValue() as? Map<*, *>
        if (existingData.isNullOrEmpty())  {
            val newChatData = mapOf(
                "type" to "private",
                "members" to mapOf(userAId to true, userBId to true)
            )
            return@runTransaction TransactionResult.success(newChatData)
        } else {
            val existingData = currentSnapshot.getValue() as? Map<String, Any> ?: emptyMap()
            return@runTransaction TransactionResult.success(existingData)
        }
    }
}
    fun createOrGetGroupChat(memberList:List<String>,rideID: String,rideTitle:String) {
        val chatRef = database.getReference("chats").child(rideID)
        val membersMap = memberList.associateWith { true }
        chatRef.runTransaction { currentSnapshot ->
            val existingData = currentSnapshot.getValue() as? Map<*, *>
            if (existingData.isNullOrEmpty()){
                val newChatData = mapOf(
                    "name" to rideTitle,
                    "type" to "group",
                    "members" to membersMap
                )
                return@runTransaction TransactionResult.success(newChatData)
            } else {
                val existingData = currentSnapshot.getValue() as? Map<String, Any> ?: emptyMap()
                return@runTransaction TransactionResult.success(existingData)
            }
        }
    }


    fun sendMessage(chatRoomId: String, senderId: String,recipientId:String, text: String) {
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
            "chats/$chatRoomId/lastTimestamp" to timestamp,
            "chats/$chatRoomId/unreadCounts/$recipientId" to FirebaseServerValue.increment(1)
        )

        database.getReference(null).updateChildren(updates)
    }
    fun sendGroupMessage(rideId: String, senderId: String, text: String, allMemberIds: List<String>) {
        val messageId = database.getReference("chats/$rideId/messages").push().key ?: return
        val timestamp = FirebaseServerValue.TIMESTAMP

        val updates = mutableMapOf<String, Any?>(
            "chats/$rideId/messages/$messageId" to mapOf(
                "senderId" to senderId,
                "text" to text,
                "timestamp" to timestamp
            ),
            "chats/$rideId/lastMessage" to text,
            "chats/$rideId/lastTimestamp" to timestamp
        )

        allMemberIds.filter { it != senderId }.forEach { memberId ->
            updates["chats/$rideId/unreadCounts/$memberId"] = FirebaseServerValue.increment(1)
        }

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

    fun getRecentChats(myUserId: String): Flow<List<ChatRoom>> {
        return database.getReference("chats")
            .observeValue().map { snapshot ->
                snapshot.children.mapNotNull { child ->
                    val map = child.getValue() as? Map<String, Any?> ?: return@mapNotNull null
                    val members = map["members"] as? Map<String, Boolean> ?: emptyMap()

                    if (members.containsKey(myUserId)) {
                        ChatRoom(
                            id = child.key ?: "",
                            name = map["name"]as? String ?: "" ,
                            type = map["type"]as? String ?: "" ,
                            lastMessage = map["lastMessage"] as? String ?: "",
                            lastTimestamp = map["lastTimestamp"] as? Long ?: 0L,
                            unreadCounts = map["unreadCounts"] as? Map<String, Long> ?: emptyMap(),
                            members=members
                        )
                    } else null
                }.sortedByDescending { it.lastTimestamp }
            }
    }

    fun markAsRead(chatRoomId: String, myUserId: String) {
        val updates = mapOf(
            "chats/$chatRoomId/unreadCounts/$myUserId" to 0
        )

        database.getReference(null).updateChildren(updates)
    }
}