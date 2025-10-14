package com.asphalt.dashboard.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.dashboard.data.DashboardRideInvite
import com.asphalt.dashboard.repository.DashboardRideInviteRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardRideInviteViewModel(val dashboardRideInviteRepo: DashboardRideInviteRepo) :
    ViewModel() {


    private val _dashboardRideInvites = MutableStateFlow<List<DashboardRideInvite>>(emptyList())
    val dashboardRideInviteList: StateFlow<List<DashboardRideInvite>> = _dashboardRideInvites

    init {
        viewModelScope.launch {
            getDashboardRideInvites()
        }
    }

    suspend fun getDashboardRideInvites() {
        _dashboardRideInvites.value = dashboardRideInviteRepo.getDashboardRideInvites()
    }

}