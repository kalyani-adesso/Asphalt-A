package com.asphalt.chat.model

data class ChatMessage(val text: String,
                       val name:String,
                       val timeStamp: Long,
                       val isSender: Boolean,)
