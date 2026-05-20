package com.asphalt.joinaride.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.constants.APIConstants
import com.asphalt.android.constants.APIConstants.END_RIDE
import com.asphalt.android.constants.APIConstants.RIDE_ACCEPTED
import com.asphalt.android.constants.APIConstants.RIDE_JOINED
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.connectedride.ConnectedRideDTO
import com.asphalt.android.model.connectedride.ConnectedRideRoot
import com.asphalt.android.model.dashboard.DashboardDTO
import com.asphalt.android.model.places.OSRMResponse
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.repository.places.PlacesRepository
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.utils.Utils
import com.asphalt.joinaride.repository.IdRepository
import com.google.android.gms.maps.model.LatLng
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
import java.util.concurrent.atomic.AtomicInteger
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
    val plcesRepo: PlacesRepository by inject()

    // stateflows
    private val _rides = MutableStateFlow<List<RidesData>>(emptyList())
    val rides = _rides.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    private val _rideId = MutableStateFlow("")
    val rideId = _rideId.asStateFlow()
    private val _joinedUsers = MutableStateFlow<List<ConnectedRideDTO>>(emptyList())
    val joinedUsers: StateFlow<List<ConnectedRideDTO>> = _joinedUsers

    private val _currentUserConnectedRideData = MutableStateFlow<ConnectedRideDTO?>(null)
    val currentUserConnectedRideData = _currentUserConnectedRideData.asStateFlow()
//    private val _rideUsers = MutableStateFlow<List<RidesData>>(emptyList())
//    val rideUsers: StateFlow<List<RidesData>> = _rideUsers

    private val _completedRideId = MutableStateFlow<String?>(null)
    val completedRideId: StateFlow<String?> = _completedRideId

    private val _polyLine = MutableStateFlow<List<LatLng>>(emptyList())
    val polyLine: StateFlow<List<LatLng>> = _polyLine
    val currentUid = androidUserVM.userState.value?.uid

    // Accepted rides with search filter
    val acceptedRides: StateFlow<List<RidesData>> =
        combine(flow = rides, flow2 = _searchQuery) { ridesList, query ->
            val q = query.trim().lowercase()
            ridesList
                .filter { ride ->
                    if (ride.createdBy == currentUid) {
                        ride.rideStatus != END_RIDE
                    } else {
                        ride.participants.any {
                            it.userId == currentUid &&
                                    it.inviteStatus in listOf(
                                RIDE_ACCEPTED,
                                RIDE_JOINED
                            )
                        }
                    }
                }
                .let { accepted ->
                    if (q.isEmpty()) accepted
                    else accepted.filter { it.rideTitle?.lowercase()?.contains(q) == true }
                }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        getAllRiders()
    }


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
                    val now = Utils.currentDateWithoutTime()

                    _rides.value = ride.orEmpty().filter { rideItem ->

                        //  1. Check upcoming ride
                        val isUpcoming = rideItem.startDate?.let { it >= now } == true

                        //  2. Check user condition
                        val isAllowed = if (rideItem.createdBy == currentUid) {
                            // Creator case
                            rideItem.rideStatus == APIConstants.RIDE_JOINED
                        } else {
                            // Participant case
                            rideItem.participants.any { participant ->
                                participant.userId == currentUid &&
                                        participant.inviteStatus == APIConstants.RIDE_JOINED
                            }
                        }

                        //  Final condition
                        isUpcoming || isAllowed
                    }
                    //_rides.value = ride
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
                userID = currentUid,
                rideStartedTime = System.currentTimeMillis()
                // current lat, curret long, datetime
            )
            val result = ridesRepo.joinRide(joinRide = request)
            //_joinRideResult.value = result
            Log.d("TAG", "JoinRideClick: $result")
        }
    }

    fun getOnGoingRides(rideId: String) {

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
//                _rideUsers.value = rideUsersList

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
    fun endRide(rideId: String, rideJoinedId: String) {
        viewModelScope.launch {
            val result = ridesRepo.endRide(rideId = rideId, rideJoinedId = rideJoinedId)
            _endRideResult.value = result
            Log.d("TAG", "endRide: $result")
        }
    }

    fun sendEndRideSummary(
        ridesID: String?,
        rideDistance: Double,
        isGroupRide: Boolean,
        startLocation: String?,
        endLocation: String?,
        isOrganiser: Boolean,
        isParticipant: Boolean,
        currentTimeMillis: Long
    ) {
        viewModelScope.launch {
            ridesRepo.endRideSummary(
                androidUserVM.getCurrentUserUID(), DashboardDTO(
                    ridesID,
                    rideDistance,
                    isGroupRide,
                    startLocation,
                    endLocation,
                    isOrganiser,
                    isParticipant, currentTimeMillis
                )
            )

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
    var endRideID: String? = null
    private var _ongoingRideUpdate = MutableStateFlow("")
    var ongoingRideUpdate = _ongoingRideUpdate.asStateFlow()
    private val observeRetryCount = AtomicInteger(0)
    private val MAX_RETRIES = 3

    // Observe all riders in real-time
    fun observeRideLocations(rideId: String) {
        // Remove old listener safely
        rideListener?.let { listener -> rideRef?.removeEventListener(listener) }
        _currentUserConnectedRideData.value = null
        _joinedUsers.value = emptyList()

        rideRef = database.getReference("ongoing_ride")
            .child(rideId)

        rideListener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                observeRetryCount.set(0)
                if (!snapshot.exists()) {
                    _joinedUsers.value = emptyList()
                    return
                }
                startedAt = snapshot.child("dateTime").getValue(Double::class.java)?.toLong()
                val connectedRides = snapshot.children.mapNotNull { child ->
                    val data = child.value as? Map<*, *> ?: return@mapNotNull null

                    val connectedRideDTO =
                        ConnectedRideDTO(
                            rideJoinedID = child.key ?: "", // Usually the push key
                            rideID = data["rideID"] as? String ?: "",
                            userID = data["userID"] as? String ?: "",
                            currentLat = (data["currentLat"] as? Number)?.toDouble() ?: 0.0,
                            currentLong = (data["currentLong"] as? Number)?.toDouble() ?: 0.0,
                            speedInKph = (data["speedInKph"] as? Number)?.toDouble() ?: 0.0,
                            status = data["status"] as? String ?: "Connected",
                            dateTime = (data["dateTime"] as? Number)?.toLong() ?: 0L,
                            isRejoined = data["isRejoined"] as? Boolean ?: false,
                            distanceTravelled = (data["totalDistance"] as? Number)?.toDouble() ?: 0.0,
                            rideStartedTime = (data["rideStartedTime"] as? Number)?.toLong() ?: 0,
                            canTrack = data["canTrack"] as? Boolean ?: true,
                            bearing = (data["bearing"] as? Number)?.toFloat() ?: 0.0f,
                        )
                    if (data["userID"] == androidUserVM.getCurrentUserUID()) {
                        endRideID = child.key.orEmpty()
                        _ongoingRideUpdate.value = child.key.orEmpty()
                        _currentUserConnectedRideData.value = connectedRideDTO
                    }
                    connectedRideDTO

                }


                // Always emit NEW list instance
                _joinedUsers.value = connectedRides
                Log.d("connectedRides=", joinedUsers.value.size.toString())

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Listener cancelled: ${error.message}")

                rideListener?.let { rideRef?.removeEventListener(it) }
                rideListener = null

                if (observeRetryCount.get() < MAX_RETRIES) {
                    observeRetryCount.incrementAndGet()
                    val delayMillis = (1000L * (1 shl observeRetryCount.get()))
                    viewModelScope.launch {
                        Log.d(
                            "Firebase",
                            "Retrying listener in ${delayMillis}ms (Attempt $observeRetryCount)"
                        )
                        delay(delayMillis)
                        observeRideLocations(rideId)
                    }
                } else {
                    Log.e("Firebase", "Max retries reached. Cannot connect to ride.")
                }
            }
        }
        if (rideRef != null && rideListener != null) {
            rideRef!!.addValueEventListener(rideListener!!)
        }
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

    fun stopRide(): Long {
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

    fun completedRideID(newRides: List<RidesData>) {
        _completedRideId.value =
            newRides.firstOrNull { ride ->
                (ride.createdBy == currentUid && ride.rideStatus == APIConstants.RIDE_JOINED)
                        ||
                        ride.participants.any {
                            it.userId == currentUid &&
                                    it.inviteStatus == APIConstants.RIDE_JOINED
                        }
            }?.ridesID
    }

    fun getPolyLines(startLat: Double, startLon: Double, endLat: Double, endLon: Double) {
        var list: List<LatLng> = emptyList()
        viewModelScope.launch {
            val response = plcesRepo.getPolyLine(startLat, startLon, endLat, endLon)
            when (response) {
                is APIResult.Error -> {

                }

                is APIResult.Success -> {
                    /* val coordinates = response.routes.firstOrNull()?.geometry?.coordinates
                     coordinates?.map { LatLng(it[1], it[0]) } ?: emptyList()*/
                    try {
                        val coordinates = response.data.routes.firstOrNull()?.geometry?.coordinates
                        val routePoints = coordinates?.map { LatLng(it[1], it[0]) } ?: emptyList()
                        _polyLine.value = routePoints
                        //_polyLine.value = listOf(LatLng(startLat, startLon))+routePoints+listOf(LatLng(endLat, endLon))
                    } catch (e: Exception) {
                        Log.e("JoinRide", "Failed to parse route coordinates", e)
                    }

                    //response.data.coordinates?.map { LatLng(it[1], it[0]) } ?: emptyList()
                }
            }

        }

    }

    fun updateOngoingRideDatabase(data: Map<String, Any>, rideId: String) {
        val path = "ongoing_ride/${rideId}/$endRideID"
        database.getReference(path).updateChildren(data)
    }

    fun getParticipantCounts(ridesData: RidesData): Pair<Int, Int> {
        // Safely handle null participants
        val list = ridesData.participants ?: emptyList()

        // Count participants with inviteStatus 3 or 4
        val participantCountWithStatus = list.count {
            it.inviteStatus == 3 || it.inviteStatus == 4
        }

        // Add +1 to total only if rideStatus is 3 or 4
        val total =
            participantCountWithStatus + if (ridesData.rideStatus == 3 || ridesData.rideStatus == 4) 1 else 0

        // Return: first = size + 1, second = total as calculated
        return Pair(list.size + 1, total)
    }
}