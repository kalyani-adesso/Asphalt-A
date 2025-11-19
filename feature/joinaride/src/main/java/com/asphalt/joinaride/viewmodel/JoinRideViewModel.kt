package com.asphalt.joinaride.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.constants.APIConstants.RIDE_ACCEPTED
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.UserDomain
import com.asphalt.android.model.connectedride.ConnectedRideRoot
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.repository.joinride.JoinRideRepository
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.collections.emptyList
import kotlin.getValue
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class JoinRideViewModel(
    private val repository: JoinRideRepository) : ViewModel(), KoinComponent {
    private val _rides = MutableStateFlow<List<RidesData>>(emptyList())
    val ridesRepo: RidesRepository by inject()
    val androidUserVM: AndroidUserVM by inject()
   val rides = _rides.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    val userRepoImpl: UserRepoImpl by inject()

    // join ride
    private val _joinRideResult = MutableStateFlow<APIResult<ConnectedRideRoot>?>(null)
    val joinRideResult : StateFlow<APIResult<ConnectedRideRoot>?> = _joinRideResult

    private val _createdBy =  MutableStateFlow("")
    val createdBy = _createdBy.asStateFlow()


    val acceptedRides: StateFlow<List<RidesData>> =
        combine(rides, _searchQuery) { ridesData, query ->


            val q = query.trim().lowercase()
            // STEP 1 → Filter ACCEPTED rides
            val accepted = ridesData.filter { ride ->

                ride.participants.any { p ->
                    p.inviteStatus == RIDE_ACCEPTED
                }
            }
            // STEP 2 → Apply SEARCH on those accepted rides
            val finalList =
                if (q.isEmpty()) {
                    accepted
                } else {
                    accepted.filter { ride ->
                        val titleMatch =
                            ride.rideTitle?.lowercase()?.contains(q) == true
                        titleMatch
                    }
                }
            println("Accepted Count after search = ${finalList.size}")
            println("user name = ${_createdBy.value}}")

            finalList

        }.stateIn(
            viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )
    init {
        getAllRiders()
    }
    fun getAllRiders() {
        viewModelScope.launch {
            var user = userRepoImpl.getUserDetails()

            val apiResult = APIHelperUI.runWithLoader {
                repository.getAllRidesList()
            }
            APIHelperUI.handleApiResult(apiResult, viewModelScope) { ride ->
                _rides.value = ride
            }
        }
    }

    fun setCreatedBy(ride: RidesData) {
        val userDomain: UserDomain? = androidUserVM.getUser(userID = ride.createdBy.toString())

        if (androidUserVM.getCurrentUserUID() == ride.createdBy) {
            _createdBy.value = "Me"
        }
        else {
            _createdBy.value = userDomain!!.name
        }
    }
    // Called from UI
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun joinRideClick(joinRide:ConnectedRideRoot) {
        viewModelScope.launch {
            val result = ridesRepo.joinRideClick(joinRide = joinRide)
            _joinRideResult.value = result
            Log.d("TAG", "JoinRideClick: $result")
        }
    }
}

sealed class RideUiState {
    object Loading : RideUiState()
    data class Success(val data: ConnectedRideRoot) : RideUiState()
    data class Error(val message: String) : RideUiState()
}