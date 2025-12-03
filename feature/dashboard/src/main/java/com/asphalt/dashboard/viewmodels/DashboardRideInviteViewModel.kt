package com.asphalt.dashboard.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.dashboard.data.DashboardRideInviteUIModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.asphalt.dashboard.mappers.toDashBoardInvites

class DashboardRideInviteViewModel(
    val ridesRepository: RidesRepository,
    val androidUserVM: AndroidUserVM
) :
    ViewModel() {

    private val currentUid: String?
        get() = androidUserVM.getCurrentUserUID()
    private val _dashboardRideInvites =
        MutableStateFlow<List<DashboardRideInviteUIModel>>(emptyList())
    private val userList
        get() = androidUserVM.userList

    val dashboardRideInviteList: StateFlow<List<DashboardRideInviteUIModel>> = _dashboardRideInvites

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
    fun cancelRide(rideID: String){
        viewModelScope.launch {
            val apiResult = APIHelperUI.runWithLoader {
                ridesRepository.deleteRide(rideID)

            }
            APIHelperUI.handleApiResult(apiResult,viewModelScope){
                removeInviteFromList(rideID)
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