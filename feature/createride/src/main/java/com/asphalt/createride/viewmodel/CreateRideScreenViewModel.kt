package com.asphalt.createride.viewmodel

import android.content.Context
import android.icu.util.Calendar
import android.location.Location
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.UserDomain
import com.asphalt.android.model.rides.CreateRideRoot
import com.asphalt.android.model.rides.UserInvites
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.android.repository.user.UserRepository
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.util.LocationUtils
import com.asphalt.commonui.utils.Utils
import com.asphalt.createride.model.CreateRideModel
import com.asphalt.createride.model.RideType
import com.asphalt.createride.model.RidersList
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CreateRideScreenViewModel : ViewModel(), KoinComponent {
    val userRepo: UserRepository by inject()
    val userRepoImpl: UserRepoImpl by inject()
    val ridesRepo: RidesRepository by inject()


    private val _tabSelectMutableState: MutableState<Int> = mutableStateOf(Constants.TAB_DETAILS)
    val tabSelectState: State<Int> = _tabSelectMutableState
    val show_datePicker = mutableStateOf(false)
    val show_EndDatePicker = mutableStateOf(false)

    val show_timePicker = mutableStateOf(false)
    val show_EndTimePicker = mutableStateOf(false)

    private val _rideDetailsMutableState = mutableStateOf(CreateRideModel())
    val rideDetailsState: State<CreateRideModel> = _rideDetailsMutableState

    val selectedUserCount = mutableStateOf(0)

    val _showRideTypeError = mutableStateOf(false)
    val _showRideTitleError = mutableStateOf(false)
    val _showRideDateError = mutableStateOf(false)
    val _showRideEndDateError = mutableStateOf(false)
    val _showRideTimeError = mutableStateOf(false)
    val _showRideEndTimeError = mutableStateOf(false)
    val _showRideStartLocError = mutableStateOf(false)
    val _showRideEndLocError = mutableStateOf(false)
    val show_participant_Tab = mutableStateOf(true)


    private val _fullList = mutableStateOf(ArrayList<RidersList>())
    private val _ridersListMutable: MutableState<ArrayList<RidersList>> =
        mutableStateOf(arrayListOf())
    val ridersList: State<ArrayList<RidersList>> = _ridersListMutable

    /* init {
         _fullList.value = getUsers()
         _ridersListMutable.value = _fullList.value
     }*/

    val searchQuery = mutableStateOf("")

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query

        val q = query.trim().lowercase()

        _ridersListMutable.value = ArrayList(
            if (q.isEmpty()) {
                _fullList.value
            } else {
                _fullList.value.filter { person ->
                    (person.name ?: "").lowercase().contains(q) ||
                            (person.job ?: "").lowercase().contains(q) ||
                            (person.bike ?: "").lowercase().contains(q)
                }
            })
        getUserCount()
    }

    fun updateUerList(isSelcted: Boolean, id: String?) {
        _fullList.value = ArrayList(_fullList.value.map { rider ->
            if (rider.id.equals(id)) {
                rider.copy(isSelect = isSelcted)
            } else {
                rider
            }
        })

        // Re-filter with current query so filtered list reflects changes
        onSearchQueryChanged(searchQuery.value)
        getUserCount()


    }

    fun detailsFieldValidation(): Boolean {
        if (_rideDetailsMutableState.value.rideType.isNullOrEmpty()) {
            _showRideTypeError.value = true
            return false
        }
        if (_rideDetailsMutableState.value.rideTitle.isNullOrEmpty()) {
            _showRideTitleError.value = true
            return false
        }
        if (_rideDetailsMutableState.value.dateString == null) {
            _showRideDateError.value = true
            return false
        }
        if (_rideDetailsMutableState.value.hour == null) {
            _showRideTimeError.value = true
            return false
        }
        if (_rideDetailsMutableState.value.endDateString == null) {
            _showRideEndDateError.value = true
            return false
        }
        if (_rideDetailsMutableState.value.endHour == null) {
            _showRideEndTimeError.value = true
            return false
        }
        return true
    }

    fun routeFieldValidation(): Boolean {
        if (_rideDetailsMutableState.value.startLocation.isNullOrEmpty()) {
            _showRideStartLocError.value = true
            return false
        }
        if (_rideDetailsMutableState.value.endLocation.isNullOrEmpty()) {
            _showRideEndLocError.value = true
            return false
        }
        return true
    }

    fun getUserCount() {
        selectedUserCount.value = _ridersListMutable.value.count { it.isSelect }
    }

    fun updateRiderType(type: String) {
        _rideDetailsMutableState.value = _rideDetailsMutableState.value.copy(rideType = type)
    }

    fun updateRiderTitle(title: String) {
        _rideDetailsMutableState.value = _rideDetailsMutableState.value.copy(rideTitle = title)
    }

    fun updateRiderDesc(decs: String) {
        _rideDetailsMutableState.value = _rideDetailsMutableState.value.copy(description = decs)
    }

    fun updateDate(dateInMills: Long?, dateString: String) {

        _rideDetailsMutableState.value =
            _rideDetailsMutableState.value.copy(dateMils = dateInMills, dateString = dateString)
    }

    fun updateEndDate(dateInMills: Long?, dateString: String) {

        _rideDetailsMutableState.value =
            _rideDetailsMutableState.value.copy(endDateMils = dateInMills, endDateString = dateString)
    }

    fun updateTime(hrs: Int?, min: Int?, isAm: Boolean, time_text: String) {
        _rideDetailsMutableState.value =
            _rideDetailsMutableState.value.copy(
                hour = hrs,
                mins = min,
                isAm = isAm,
                displayTime = time_text
            )
    }

    fun updateEndTime(hrs: Int?, min: Int?, isAm: Boolean, time_text: String) {
        _rideDetailsMutableState.value =
            _rideDetailsMutableState.value.copy(
                endHour = hrs,
                endMins = min,
                isEndAm = isAm,
                endDisplayTime = time_text
            )
    }

    fun updateStartLocation(loc: String) {
        _rideDetailsMutableState.value = _rideDetailsMutableState.value.copy(startLocation = loc)
    }

    fun updateEnLocation(loc: String) {
        _rideDetailsMutableState.value = _rideDetailsMutableState.value.copy(endLocation = loc)
    }

    fun updateStartLocation(lat: Double, lon: Double) {
        _rideDetailsMutableState.value =
            _rideDetailsMutableState.value.copy(startLat = lat, startLon = lon)
    }

    fun updateEndLocation(lat: Double, lon: Double) {
        _rideDetailsMutableState.value =
            _rideDetailsMutableState.value.copy(endLat = lat, endLon = lon)
    }

    fun updateTab(tab: Int) {

        _tabSelectMutableState.value += tab
    }

    fun showDatePicker(isShow: Boolean) {
        show_datePicker.value = isShow
    }

    fun showTimePicker(isShow: Boolean) {
        show_timePicker.value = isShow
    }

    fun showEndDatePicker(isShow: Boolean) {
        show_EndDatePicker.value = isShow
    }

    fun showEndTimePicker(isShow: Boolean) {
        show_EndTimePicker.value = isShow
    }

    fun updateParticipantTab(showTab: Boolean) {
        show_participant_Tab.value = showTab
    }


    fun getRideType(context: Context): ArrayList<RideType> {
        var type =
            arrayListOf(
                RideType(Constants.SOLO_RIDE, context.getString(R.string.solo_ride)),
                RideType(Constants.GROUP_RIDE, context.getString(R.string.group_ride)),
                RideType(Constants.OPEN_EVENT, context.getString(R.string.open_event))
            )
        return type
    }

    suspend fun createRide() {
        val totalDistance = FloatArray(2)
        Location.distanceBetween(_rideDetailsMutableState.value.startLat ?: 0.0,
            _rideDetailsMutableState.value.startLon ?: 0.0,
            _rideDetailsMutableState.value.endLat ?: 0.0,
            _rideDetailsMutableState.value.endLon ?: 0.0,totalDistance)
        val distanceKm : Double = (totalDistance[0]/1000).toDouble()
        Log.d("TAG", "MapWithCurrentLocation: distance $distanceKm")
        val cal = Calendar.getInstance()
        val userDetails = userRepoImpl.getUserDetails()
        val map: Map<String, UserInvites> =
            _ridersListMutable.value.filter { it.isSelect == true }
                .associate { rider ->
                    rider.id.orEmpty() to UserInvites(
                        acceptInvite = 0
                    )
                } ?: emptyMap()
        var createRide: CreateRideRoot = CreateRideRoot(
            userID = userDetails?.uid,
            rideType = _rideDetailsMutableState.value.rideType,
            rideTitle = _rideDetailsMutableState.value.rideTitle,
            description = _rideDetailsMutableState.value.description,
            startDate = Utils.getDate(
                _rideDetailsMutableState.value.dateMils ?: 0,
                _rideDetailsMutableState.value.hour ?: 0,
                _rideDetailsMutableState.value.mins ?: 0,
                _rideDetailsMutableState.value.isAm
            ),
            startLocation = _rideDetailsMutableState.value.startLocation,
            endLocation = _rideDetailsMutableState.value.endLocation,
            createdDate = cal.timeInMillis,
            participants = map,
            startLatitude = _rideDetailsMutableState.value.startLat ?: 0.0,
            startLongitude = _rideDetailsMutableState.value.startLon ?: 0.0,
            endLatitude = _rideDetailsMutableState.value.endLat ?: 0.0,
            endLongitude = _rideDetailsMutableState.value.endLon ?: 0.0,
            distance = distanceKm,
            endDate = Utils.getDate(
                _rideDetailsMutableState.value.endDateMils ?: 0,
                _rideDetailsMutableState.value.endHour ?: 0,
                _rideDetailsMutableState.value.endMins ?: 0,
                _rideDetailsMutableState.value.isEndAm
            )
        )



        val apiResult = APIHelperUI.runWithLoader {
            ridesRepo.createRide(createRide)
        }
        APIHelperUI.handleApiResult(apiResult, viewModelScope) {
            updateTab(1)
            //UIStateHandler.sendEvent(UIState.SUCCESS("Added query successfully!"))
        }
    }


    fun getUsers() {
        viewModelScope.launch {
            val user = userRepoImpl.getUserDetails()
            var response: APIResult<List<UserDomain>> = userRepo.getAllUsers()
            when (response) {
                is APIResult.Success -> {
                    if (response.data.size > 0) {
                        _fullList.value =
                            ArrayList(
                                response.data
                                    .filter { it.uid != user?.uid }
                                    .map {
                                        RidersList(
                                            name = it.name,
                                            id = it.uid,
                                            bike = it.primaryBike,
                                            job = if (it.isMechanic)
                                                "Mechanic" else "",
                                            imgUrl=it.profilePic
                                        )
                                    })
                        _ridersListMutable.value = _fullList.value
                    }

                }

                is APIResult.Error -> {
                    //val msg = result.exception.message ?: "Something went wrong"

                }
            }

        }


        /* var list = arrayListOf(

             RidersList(
                 id = 1,
                 name = "Harikumar S",
                 job = "Mechanic",
                 bike = "Unicorn",
                 isSelect = false,
                 imgUrl = "https://picsum.photos/id/1/200/300"
             ),
             RidersList(
                 id = 2,
                 name = "Sreedev",
                 job = "",
                 bike = "Unicorn",
                 isSelect = false,
                 imgUrl = "https://picsum.photos/id/1/200/300"

             ),
             RidersList(
                 id = 3,
                 name = "Vyshak ",
                 job = "",
                 bike = "Unicorn",
                 isSelect = false,
                 imgUrl = "https://picsum.photos/id/1/200/300"
             ),
             RidersList(
                 id = 4,
                 name = "Jerin John",
                 job = "",
                 bike = "Unicorn",
                 isSelect = false,
                 imgUrl = "https://picsum.photos/id/1/200/300"
             ),
             RidersList(
                 id = 5,
                 name = "Vipin Raj",
                 job = "Mechanic",
                 bike = "Unicorn",
                 isSelect = false,
                 imgUrl = "https://picsum.photos/id/1/200/300"
             ),
             RidersList(
                 id = 6,
                 name = "Pramod Selvaraj",
                 job = "",
                 bike = "Unicorn",
                 isSelect = false,
                 imgUrl = "https://picsum.photos/id/1/200/300"
             ),
             RidersList(
                 id = 7,
                 name = "Vinu V John",
                 job = "",
                 bike = "Unicorn",
                 isSelect = false,
                 imgUrl = "https://picsum.photos/id/1/200/300"
             )
         )
         return list;*/

    }

}