package com.asphalt.dashboard.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.model.CurrentUser
import com.asphalt.dashboard.repository.DashBoardRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(val dashBoardRepo: DashBoardRepo) : ViewModel() {
    init {
        fetchUserDetails()
    }

    private val _user = MutableStateFlow<CurrentUser?>(null)
    val user: StateFlow<CurrentUser?> = _user
    fun fetchUserDetails() {
        viewModelScope.launch {
            val userDetails = dashBoardRepo.getUserDetails()
            userDetails.also { _user.value = it }
        }


    }

}