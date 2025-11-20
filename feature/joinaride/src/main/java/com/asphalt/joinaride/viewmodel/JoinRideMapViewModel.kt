package com.asphalt.joinaride.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.UserDomain
import com.asphalt.android.model.connectedride.ConnectedRideDTO
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class JoinRideMapViewModel(

    private val ridesRepository: RidesRepository) : ViewModel(), KoinComponent {

   // private val rideId: String
    val androidUserVM: AndroidUserVM by inject()

    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId

    private val _ongoingRides = MutableStateFlow<APIResult<List<ConnectedRideDTO>>?>(null)
    val ongoingRides: StateFlow<APIResult<List<ConnectedRideDTO>>?> = _ongoingRides

    init {
       // getOngoingRides(ride = rideId)
    }

    fun setUserId() {
        val currentUser = androidUserVM.getCurrentUserUID()
        val userDomain: UserDomain? = androidUserVM.getUser(userID = currentUser)

        _userId.value = userDomain!!.name
        Log.d("TAG", "setUserId: $currentUser")
        Log.d("TAG", "setUserId: ${_userId.value}")
    }

    fun checkOrganizer() {
        // call api
        // updateOrganizerStatus
        viewModelScope.launch {
           // val result = ridesRepository.updateOrganizerStatus(rideId = rideId, rideStatus = 0)

        }
    }
    fun getOngoingRides(ride: String) {
        viewModelScope.launch {

            val apiResult = APIHelperUI.runWithLoader {
                ridesRepository.getOngoingRides(rideId = ride)
            }
        }
    }

    fun endRide(rideId: String,rideJoinedId: String) {
        viewModelScope.launch {
            val result = ridesRepository.endRide(rideId = rideId,rideJoinedId = rideJoinedId)
        }
    }
}