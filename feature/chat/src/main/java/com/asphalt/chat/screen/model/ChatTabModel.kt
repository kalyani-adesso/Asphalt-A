package com.asphalt.chat.screen.model

import com.asphalt.chat.ChatConstants

data class ChatTabModel(var name: String, var id: Int) {
    companion object {
        fun getChatTabs(): ArrayList<ChatTabModel> {
            return arrayListOf(
                ChatTabModel("All", ChatConstants.TAB_ALL),
                ChatTabModel("Unread", ChatConstants.TAB_UNREAD),
                ChatTabModel("Groups", ChatConstants.TAB_GROUPS),
                ChatTabModel("Favorites", ChatConstants.TAB_FAVORITIES),
            )
        }
    }
}


