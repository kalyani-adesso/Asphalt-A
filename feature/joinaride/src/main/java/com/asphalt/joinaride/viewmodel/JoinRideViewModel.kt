package com.asphalt.joinaride.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.constants.APIConstants.RIDE_ACCEPTED
import com.asphalt.android.constants.APIConstants.RIDE_JOINED
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.UserDomain
import com.asphalt.android.model.connectedride.ConnectedRideDTO
import com.asphalt.android.model.connectedride.ConnectedRideRoot
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.repository.joinride.JoinRideRepository
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.joinaride.repository.IdRepository
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
    private val repository: JoinRideRepository,
    private val idRepository: IdRepository
) : ViewModel(), KoinComponent {
    private val _rides = MutableStateFlow<List<RidesData>>(emptyList())
    val ridesRepo: RidesRepository by inject()
    val androidUserVM: AndroidUserVM by inject()
    val rides = _rides.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    val userRepoImpl: UserRepoImpl by inject()

    private val _rideId = MutableStateFlow("")
    val rideId = _rideId.asStateFlow()

    fun setRideId(selectedId: String) {
        _rideId.value = selectedId
        idRepository.id = selectedId
    }

    fun getRideId() : String? = idRepository.id

    // join ride
    private val _joinRideResult = MutableStateFlow<APIResult<ConnectedRideDTO>?>(null)
    val joinRideResult: StateFlow<APIResult<ConnectedRideDTO>?> = _joinRideResult

    private val _rideDetails = MutableStateFlow<ConnectedRideRoot?>(null)
    val rideDetails : StateFlow<ConnectedRideRoot?> = _rideDetails

    private val currentUid = androidUserVM.userState.value?.uid
    private val _createdBy = MutableStateFlow("")
    val createdBy = _createdBy.asStateFlow()

    val acceptedRides: StateFlow<List<RidesData>> =
        combine(rides, _searchQuery) { ridesData, query ->


            val q = query.trim().lowercase()
            // STEP 1 → Filter ACCEPTED rides
            val accepted = ridesData.filter { ride ->

                // current user
                if (ride.createdBy == currentUid) {
                    true
                } else {
                    // participants
                    ride.participants.any { p ->
                        p.inviteStatus == RIDE_ACCEPTED
                    }
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
                ridesRepo.getAllRide()
            }
            APIHelperUI.handleApiResult(apiResult, viewModelScope) { ride ->
                _rides.value = ride
            }
        }
    }

    fun setCreatedBy(ride: RidesData) {
        val userDomain: UserDomain? = androidUserVM.getUser(userID = ride.createdBy.toString())

        val rideStatus = ride.rideStatus
        Log.d("TAG", "setCreatedBy: $rideStatus")
        if (androidUserVM.getCurrentUserUID() == ride.createdBy) {
            _createdBy.value = "Me"
        } else {
            _createdBy.value = userDomain?.name ?: ""
        }
    }
    // Called from UI
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateRideStatus(userId: String, rideId: String, status: Int) {
        viewModelScope.launch {
            // organizer
            if (userId == currentUid) {

                val result =
                    ridesRepo.updateOrganizerStatus(rideId = rideId, rideStatus = status)
                Log.d("TAG", "updateOrganizerStatus:$result")
            } else {
                // participants status
                val rideResult = ridesRepo.changeRideInviteStatus(
                    rideID = rideId,
                    currentUid = currentUid!!, inviteStatus = status
                )
                Log.d("TAG", "changeRideInviteStatus:$rideResult")
            }
        }
    }

    fun joinRide(joinRide: RidesData) {

        viewModelScope.launch {

            val request = ConnectedRideRoot(
                rideID = joinRide.ridesID,
                currentLat = joinRide.startLatitude,
                currentLong = joinRide.startLongitude,
                dateTime = joinRide.startDate,
                isRejoined = false,
                status = "connected",
                userID = currentUid
                // current lat, curret long, datetime
            )

            val result = ridesRepo.joinRide(joinRide = request)
            //_joinRideResult.value = result
            Log.d("TAG", "JoinRideClick: $result")
        }
    }

    fun reJoinRide(joinRide: RidesData) {
        viewModelScope.launch {

            val request = ConnectedRideRoot(
                rideID = joinRide.ridesID,
                currentLat = joinRide.startLatitude,
                currentLong = joinRide.startLongitude,
                dateTime = joinRide.startDate,
                isRejoined = true,
                status = "connected"
                // current lat, curret long, datetime
            )
           // val res = ridesRepo.reJoinRide(rejoinRide = reJoinRide, ongoingRideId = reJoinRide.rideJoinedID ?: "")
          //  Log.d("TAG", "JoinRideClick: $res")

        }
    }

    fun getOnGoingRides(rideId:String) {
        viewModelScope.launch {
            val rideDetails = ridesRepo.getOngoingRides(rideId)
            Log.d("TAG", "getJoinRides: $rideDetails")
        }
    }

    private val _endRideResult = MutableStateFlow<APIResult<Unit>?>(null)
    val endRideResult = _endRideResult

    fun endRide(rideId: String,rideJoinedId: String) {
        viewModelScope.launch {
            val result = ridesRepo.endRide(rideId = rideId,rideJoinedId = rideJoinedId)
            _endRideResult.value = result
            Log.d("TAG", "endRide: $result")
        }
    }
}