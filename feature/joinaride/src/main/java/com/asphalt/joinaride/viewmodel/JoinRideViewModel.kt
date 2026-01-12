package com.asphalt.joinaride.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.constants.APIConstants.RIDE_ACCEPTED
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.UserDomain
import com.asphalt.android.model.connectedride.ConnectedRideDTO
import com.asphalt.android.model.connectedride.ConnectedRideRoot
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.joinaride.repository.IdRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
    private val idRepository: IdRepository
) : ViewModel(), KoinComponent {
    private val _rides = MutableStateFlow<List<RidesData>>(emptyList())
    val ridesRepo: RidesRepository by inject()
    val androidUserVM: AndroidUserVM by inject()
    val rides = _rides.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _rideId = MutableStateFlow("")
    val rideId = _rideId.asStateFlow()

    fun setRideId(selectedId: String) {
        _rideId.value = selectedId
        idRepository.id = selectedId
    }
    fun getRideId() : String? = idRepository.id
    private val currentUid = androidUserVM.userState.value?.uid
    
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
            val apiResult = APIHelperUI.runWithLoader {
                ridesRepo.getAllRide()
            }
            APIHelperUI.handleApiResult(apiResult, viewModelScope) { ride ->
                _rides.value = ride
            }
        }
    }

    fun setCreatedBy(ride: RidesData) : String {
        val userDomain: UserDomain? = androidUserVM.getUser(userID = ride.createdBy.toString())

        val rideStatus = ride.rideStatus
        Log.d("TAG", "setCreatedBy: $rideStatus")
       return if (androidUserVM.getCurrentUserUID() == ride.createdBy) {

            "Me"
        } else {

            userDomain?.name ?: ""

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

    private val _joinedUsers =
        MutableStateFlow<List<ConnectedRideDTO>>(emptyList())

    val joinedUsers: StateFlow<List<ConnectedRideDTO>> = _joinedUsers

    private val _rideUsers = MutableStateFlow<List<RidesData>>(emptyList())
    val rideUsers: StateFlow<List<RidesData>> = _rideUsers
    fun getOnGoingRides(rideId:String) {

        viewModelScope.launch {

            val rideDetails = ridesRepo.getOngoingRides(rideId)

            APIHelperUI.handleApiResult(rideDetails, viewModelScope) { response ->
                //    val sortedArray = response.sortedBy{ it.startDate }}
                // Filter out current user from “other users
                val filteredList = response
                    .filter { it.userID != currentUid }

                Log.d("TAG", "getOnGoingRides otherUsers: ${filteredList.size}")

                // Find current user
                val currentUser = response.find {
                    it.userID == currentUid
                }
                Log.d("TAG", "getJoinRides currentuser: $currentUser")

                val user = ridesRepo.getSingeRide(rideId)
                Log.d("TAG", "getOnGoingRides: user $user ")


                val joinRidersList = buildList {
                    addAll(filteredList.filter { it.userID != currentUid })

                }
                //Update basic ride list (optional, if you need)
                _joinedUsers.value = joinRidersList

                val rideUsers = joinRidersList.mapNotNull { ride ->

                    val userDomain = androidUserVM.getUser(ride.userID)

                    userDomain?.let { user ->
                        RidesData(
                            ridesID = user.uid,
                            createdBy = user.name
                        )
                    }
                }

                _rideUsers.value = rideUsers
                Log.d("TAG", "getJoinRides joined finalList: $joinRidersList")
                Log.d("TAG", "All rides: $response")
                Log.d("TAG", "Ride users for UI: ${rideUsers.size}")
            }
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

    fun removeEndRideList(joinRide: List<RidesData>) {
        viewModelScope.launch {
            // Remove items where status == 4
            _rides.value = joinRide.filter { it.rideStatus != 4 }

        }
    }

    private var rideListener: ValueEventListener? = null
    fun observeRideLocations(rideId: String) {

        val ref = FirebaseDatabase.getInstance()
            .getReference("rides")
            .child(rideId)
            .child("ongoing_ride")

        rideListener?.let { ref.removeEventListener(it) }

        rideListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children.mapNotNull {
                    it.getValue(ConnectedRideDTO::class.java)
                }
                _joinedUsers.value = users
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        ref.addValueEventListener(rideListener!!)
    }
}