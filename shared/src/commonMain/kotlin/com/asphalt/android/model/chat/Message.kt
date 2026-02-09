package com.asphalt.android.model.chat

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String = "",
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0L,
    val isRead: Boolean = false,

)


@Serializable
data class ChatRoom(
    val id: String = "",
    val name: String? = null,
    val lastMessage: String = "",
    val lastTimestamp: Long = 0L,
    val type: String = "private",
    val members: Map<String, Boolean> = emptyMap(),
    val unreadCounts: Map<String, Int> = emptyMap()
)

fun ChatRoom.getOtherUserId(currentUserId: String): String? {
    return members.keys.firstOrNull { it != currentUserId }
}