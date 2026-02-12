package com.asphalt.chat.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.model.chat.ChatRoom
import com.asphalt.android.model.chat.getOtherUserId
import com.asphalt.android.repository.chat.ChatRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.chat.ChatConstants
import com.asphalt.commonui.constants.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatListViewModel(val chatRepository: ChatRepository, val androidUserVM: AndroidUserVM) :
    ViewModel() {
    val tabSelection = mutableStateOf(ChatConstants.TAB_ALL)
    private val currentUid: String
        get() = androidUserVM.getCurrentUserUID()

    private val _chatListModel = MutableStateFlow<List<ChatRoom>>(emptyList())
    private val _chatListModelTemp = MutableStateFlow<List<ChatRoom>>(emptyList())
    val chatModel: StateFlow<List<ChatRoom>> = _chatListModelTemp

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun updateTab(tab: Int) {
        tabSelection.value = tab
        applyTabFilter()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        applyTabFilter()
    }

    fun applyTabFilter() {
        val fullList = _chatListModel.value
        val query = _searchQuery.value.lowercase()

        val tabFiltered = when (tabSelection.value) {

            ChatConstants.TAB_ALL -> {
                fullList
            }

            ChatConstants.TAB_GROUPS -> {
                fullList.filter { it.type == "group" }
            }

            ChatConstants.TAB_UNREAD -> {
                fullList.filter {
                    (it.unreadCounts[currentUid]?.toInt() ?: 0) > 0
                }
            }

            ChatConstants.TAB_FAVORITIES -> {
                // Example: if you mark favorites via members map
                /*fullList.filter {
                    it.members[currentUid] == true
                }*/
                fullList
            }

            else -> fullList
        }

        // Step 2: Apply search filter
        // 🔍 Search filter
        _chatListModelTemp.value =
            if (query.isBlank()) {
                tabFiltered
            } else {
                tabFiltered.filter { chatRoom ->

                    val displayName = if (chatRoom.type == Constants.GROUP_CHAT) {
                        chatRoom.name ?: ""
                    } else {
                        androidUserVM.getUser(
                            chatRoom.getOtherUserId(currentUid) ?: ""
                        )?.name ?: ""
                    }

                    displayName.contains(query, ignoreCase = true) ||
                            chatRoom.lastMessage.contains(query, ignoreCase = true)
                }
            }
    }


    fun getChatList() {
        viewModelScope.launch {
            chatRepository.getRecentChats(currentUid).collect { it ->
                _chatListModel.value = it
                applyTabFilter()
            }
        }

    }

}