package com.asphalt.dashboard.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.model.UserDomain
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.android.repository.user.UserRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.dashboard.data.DashboardRideInviteUIModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mappers.toDashBoardInvites

class DashboardRideInviteViewModel(
    val ridesRepository: RidesRepository,
    val userRepository: UserRepository,
    androidUserVM: AndroidUserVM
) :
    ViewModel() {

    private val currentUid = androidUserVM.userState.value?.uid
    private val _dashboardRideInvites =
        MutableStateFlow<List<DashboardRideInviteUIModel>>(emptyList())
    private val userList = MutableStateFlow<List<UserDomain>>(emptyList())

    val dashboardRideInviteList: StateFlow<List<DashboardRideInviteUIModel>> = _dashboardRideInvites

    init {
        viewModelScope.launch {
            loadUserList()
            getDashboardRideInvites()
        }
    }

    suspend fun loadUserList() {
        APIHelperUI.handleApiResult(
            APIHelperUI.runWithLoader { userRepository.getAllUsers() },
            viewModelScope
        ) {
            userList.value = it
        }
    }

    private fun removeInviteFromList(rideID: String) {
        _dashboardRideInvites.update { currentList ->
            currentList.filterNot { it.rideID == rideID }
        }
    }

    suspend fun getDashboardRideInvites() {
        currentUid?.let { uid ->
            APIHelperUI.handleApiResult(APIHelperUI.runWithLoader {
                ridesRepository.getRideInvites(
                    uid
                )
            }, viewModelScope) {
                _dashboardRideInvites.value = it.toDashBoardInvites(userList.value)
            }
        }

    }

    fun updateRideInviteStatus(rideID: String, inviteStatus: Int) {
        viewModelScope.launch {
            currentUid?.let {
                APIHelperUI.handleApiResult(
                    APIHelperUI.runWithLoader {
                        ridesRepository.changeRideInviteStatus(
                            rideID,
                            it,
                            inviteStatus
                        )
                    }, viewModelScope
                ) {
                    removeInviteFromList(rideID)
                }
            }
        }
    }

}