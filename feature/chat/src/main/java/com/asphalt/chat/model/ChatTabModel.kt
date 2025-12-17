package com.asphalt.chat.model

import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import com.asphalt.chat.ChatConstants
import com.asphalt.commonui.R

data class ChatTabModel(@param:StringRes var name: Int, var id: Int) {
    companion object {
        fun getChatTabs(): ArrayList<ChatTabModel> {
            return arrayListOf(
                ChatTabModel(R.string.all, ChatConstants.TAB_ALL),
                ChatTabModel(R.string.unread, ChatConstants.TAB_UNREAD),
                ChatTabModel(R.string.groups, ChatConstants.TAB_GROUPS),
                ChatTabModel(R.string.favorites, ChatConstants.TAB_FAVORITIES),
            )
        }
    }
}


