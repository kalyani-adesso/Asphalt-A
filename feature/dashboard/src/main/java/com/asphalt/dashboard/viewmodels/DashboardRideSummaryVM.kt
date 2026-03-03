package com.asphalt.dashboard.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.constants.APIConstants
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.model.dashboard.DashboardDomain
import com.asphalt.android.model.rides.CreateRideRoot
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardRideSummaryVM(
    val ridesRepository: RidesRepository,
    val androidUserVM: AndroidUserVM
) : ViewModel() {
    private val userUID: String
        get() = androidUserVM.getCurrentUserUID()
    private val _dashboardSummary = MutableStateFlow<List<DashboardDomain>>(emptyList())
    private val _onGoingRide = MutableStateFlow<RidesData?>(null)
    val onGoingRide = _onGoingRide.asStateFlow()
    val dashboardSummary = _dashboardSummary.asStateFlow()


    fun getRidesData() {
        viewModelScope.launch {
            APIHelperUI.handleApiResult(
                APIHelperUI.runWithLoader {
                    ridesRepository.getRideSummary(userUID)
//                    ridesRepository.getRideSummary("NUIAgBiffTUBtoxtgRGcn8wR9bR2")

                }, viewModelScope
            ) {
                _dashboardSummary.value = it
            }

        }
    }
    fun getOngoingRide(newRides: List<RidesData>) {
        val rideData: RidesData? =
            newRides.firstOrNull { ride ->
                (ride.createdBy == userUID && ride.rideStatus == APIConstants.RIDE_JOINED)
                        ||
                        ride.participants.any {
                            it.userId == userUID &&
                                    it.inviteStatus == APIConstants.RIDE_JOINED
                        }
            }
        _onGoingRide.value = rideData
    }
    fun getRideList(data: Map<String, CreateRideRoot>?){

        val ridesDataList = ridesRepository.getRidesDataList(data)
        getOngoingRide(ridesDataList)

    }
    fun getRideRootFromSnapshot(rideId: String, data: Any?):CreateRideRoot?{
        return ridesRepository.mapSnapshotToRide(rideId,data)
    }




}