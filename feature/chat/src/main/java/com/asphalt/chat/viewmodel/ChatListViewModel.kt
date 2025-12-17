package com.asphalt.chat.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.asphalt.chat.ChatConstants

class ChatListViewModel : ViewModel() {
    val tabSelection = mutableStateOf(ChatConstants.TAB_ALL)

    fun updateTab(tab: Int) {
        tabSelection.value = tab
    }
}