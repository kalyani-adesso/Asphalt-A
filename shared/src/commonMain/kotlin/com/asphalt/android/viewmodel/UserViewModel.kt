package com.asphalt.android.viewmodel

import com.asphalt.android.model.CurrentUser
import com.asphalt.android.repository.UserRepoImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(val userRepoImpl: UserRepoImpl, val coroutineScope: CoroutineScope) {
    private val _user = MutableStateFlow<CurrentUser?>(null)
    val user: StateFlow<CurrentUser?> = _user

    fun fetchDetails() {
        coroutineScope.launch {
            val userDetails = userRepoImpl.getUserDetails()
            _user.value = userDetails
        }

    }
}