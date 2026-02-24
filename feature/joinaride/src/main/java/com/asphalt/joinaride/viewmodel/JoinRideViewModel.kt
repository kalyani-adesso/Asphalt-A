package com.asphalt.joinaride.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.constants.APIConstants.RIDE_ACCEPTED
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.connectedride.ConnectedRideDTO
import com.asphalt.android.model.connectedride.ConnectedRideRoot
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.joinaride.repository.IdRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
import kotlinx.coroutines.isActive

class JoinRideViewModel(
    private val idRepository: IdRepository,
) : ViewModel(), KoinComponent {

    //Dependencies
    val ridesRepo: RidesRepository by inject()
    val androidUserVM: AndroidUserVM by inject()

    // stateflows
    private val _rides = MutableStateFlow<List<RidesData>>(emptyList())
    val rides = _rides.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    private val _rideId = MutableStateFlow("")
    val rideId = _rideId.asStateFlow()
    private val _joinedUsers = MutableStateFlow<List<ConnectedRideDTO>>(emptyList())
    val joinedUsers: StateFlow<List<ConnectedRideDTO>> = _joinedUsers
    private val _rideUsers = MutableStateFlow<List<RidesData>>(emptyList())
    val rideUsers: StateFlow<List<RidesData>> = _rideUsers

    private val currentUid = androidUserVM.userState.value?.uid

    // Accepted rides with search filter
    val acceptedRides: StateFlow<List<RidesData>> =
        combine(flow = rides, flow2 = _searchQuery) { ridesList, query ->
            val q = query.trim().lowercase()
            ridesList
                .filter { ride ->
                    ride.createdBy == currentUid ||
                            ride.participants.any { it.inviteStatus == RIDE_ACCEPTED }
                }
                .let { accepted ->
                    if (q.isEmpty()) accepted
                    else accepted.filter { it.rideTitle?.lowercase()?.contains(q) == true }
                }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        getAllRiders()
    }
    fun setRideId(selectedId: String) {
        _rideId.value = selectedId
        idRepository.id = selectedId
    }
    fun getRideId() : String? = idRepository.id
    // Called from UI
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
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
    fun setCreatedBy(ride: RidesData): String {
        return if (androidUserVM.getCurrentUserUID() == ride.createdBy) "Me"
        else androidUserVM.getUser(ride.createdBy.toString())?.name.orEmpty()
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
                currentUid?.let {
                    val rideResult = ridesRepo.changeRideInviteStatus(
                        rideID = rideId,
                        currentUid = it,
                        inviteStatus = status
                    )
                    Log.d("TAG", "changeRideInviteStatus: $rideResult")
                }
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
    fun getOnGoingRides(rideId:String) {

        viewModelScope.launch {

            val rideDetails = ridesRepo.getOngoingRides(rideId)
            APIHelperUI.handleApiResult(rideDetails, viewModelScope) { response ->
                //    val sortedArray = response.sortedBy{ it.startDate }}
                // Filter out current user from “other users
                val otherUsers = response.filter { it.userID != currentUid }
              //  val joinedList = otherUsers
                _joinedUsers.value = otherUsers
                Log.d("TAG", "getOnGoingRides otherUsers: ${otherUsers.size}")

                val rideUsersList = otherUsers.mapNotNull { ride ->
                    androidUserVM.getUser(ride.userID)?.let { user ->
                        RidesData(ridesID = user.uid, createdBy = user.name)
                    }
                }
                _rideUsers.value = rideUsersList

                val user = ridesRepo.getSingeRide(rideId)
                Log.d("TAG", "getOnGoingRides: user $user ")

                val joinRidersList = buildList {
                    addAll(otherUsers.filter { it.userID != currentUid })
                }
                //Update basic ride list (optional, if you need)
                _joinedUsers.value = joinRidersList

//                val rideUsers = joinRidersList.mapNotNull { ride ->
//
//                    val userDomain = androidUserVM.getUser(ride.userID)
//
//                    userDomain?.let { user ->
//                        RidesData(
//                            ridesID = user.uid,
//                            createdBy = user.name
//                        )
//                    }
//                }
//                _rideUsers.value = rideUsers
//                Log.d("TAG", "getJoinRides joined finalList: $joinRidersList")
//                Log.d("TAG", "All rides: $response")
//                Log.d("TAG", "Ride users for UI: ${rideUsers.size}")
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
        _rides.value = joinRide.filter { it.rideStatus != 4 }
    }
    //Firebase
    private var rideListener: ValueEventListener? = null
    private var rideRef: DatabaseReference? = null
    private var startedAt: Long? = null
    private val database = FirebaseDatabase.getInstance()
    // Observe all riders in real-time
    fun observeRideLocations(rideId: String) {
        // Remove old listener safely
        rideListener?.let { listener -> rideRef?.removeEventListener(listener) }

        val ref = database.getReference("rides")
            .child(rideId)
            .child("ongoing_ride")

        rideListener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (!snapshot.exists()) {
                    _joinedUsers.value = emptyList()
                    return
                }
                startedAt = snapshot.child("dateTime").getValue(Double::class.java)?.toLong()

                val users = snapshot.children
                    .filter { it.key != "dateTime" }
                    .mapNotNull { it.getValue(ConnectedRideDTO::class.java) }
                // Always emit NEW list instance
                _joinedUsers.value = users
            }
            override fun onCancelled(error: DatabaseError) {
                rideListener?.let {
                    rideListener?.let { rideRef?.removeEventListener(it) }
                }
            }
        }
        ref.addValueEventListener(rideListener!!)
    }

    private val _isRideStarted = MutableStateFlow(false)
    val isRideStarted = _isRideStarted.asStateFlow()

    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime = _elapsedTime.asStateFlow()
    private var timerJob: Job? = null
    private var rideStartTime: Long? = null

    private val _finalDuration = MutableStateFlow(0L)
    val finalDuration = _finalDuration.asStateFlow()


    fun startRideTimer() {
        if (timerJob != null) return

        rideStartTime = System.currentTimeMillis() // save start time
        Log.d("TIMER", "Ride started at $rideStartTime")

        timerJob = viewModelScope.launch {
            while (isActive) {
                val start = rideStartTime ?: break
                _elapsedTime.value = (System.currentTimeMillis() - start) / 1000
                rideRef?.child("dateTime")?.setValue(_elapsedTime.value)
                delay(1000)
            }
        }
    }

    fun stopRide() : Long {
        _isRideStarted.value = false
        timerJob?.cancel()
        timerJob = null

        val start = rideStartTime ?: return 0L  // if null, exit
        val finalSeconds = (System.currentTimeMillis() - start) / 1000
        Log.d("TIMER", "Ride stopped. Duration = $finalSeconds")

        _finalDuration.value = finalSeconds
        rideStartTime = null
        return finalSeconds
    }
    fun setEndTime(finalTime: Long) {
        _finalDuration.value = finalTime
    }
    override fun onCleared() {
        rideListener?.let { rideRef?.removeEventListener(it) }
        timerJob?.cancel()
        super.onCleared()
    }
}