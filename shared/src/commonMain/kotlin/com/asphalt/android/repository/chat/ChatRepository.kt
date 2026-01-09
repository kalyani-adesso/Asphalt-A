package com.asphalt.android.repository.chat

import com.asphalt.android.PlatformDatabase
import com.asphalt.android.TransactionResult

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
                return@runTransaction TransactionResult.success(newChatData)
            } else {
                return@runTransaction TransactionResult.success(currentSnapshot)
            }
        }
    }
}