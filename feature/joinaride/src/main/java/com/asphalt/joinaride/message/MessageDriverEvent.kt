package com.asphalt.joinaride.message

sealed class MessageDriverEvent {


    data class OnQuickMessageClick(val message: String) : MessageDriverEvent()

    data class OnCustomMessageChange(val message: String) : MessageDriverEvent()

    object OnSendClick : MessageDriverEvent()

    object OnCancelClick : MessageDriverEvent()
}