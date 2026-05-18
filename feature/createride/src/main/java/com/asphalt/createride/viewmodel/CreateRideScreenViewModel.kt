package com.asphalt.createride.viewmodel

import android.content.Context
import android.icu.util.Calendar
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.UserDomain
import com.asphalt.android.model.rides.CreateRideRoot
import com.asphalt.android.model.rides.Ratings
import com.asphalt.android.model.rides.UserInvites
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.android.repository.user.UserRepository
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.utils.Utils
import com.asphalt.createride.model.CreateRideModel
import com.asphalt.createride.model.RideType
import com.asphalt.createride.model.RidersList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class CreateRideScreenViewModel : ViewModel(), KoinComponent {
    val userRepo: UserRepository by inject()
    val userRepoImpl: UserRepoImpl by inject()
    val ridesRepo: RidesRepository by inject()


    private val _tabSelectMutableState = MutableStateFlow(Constants.TAB_DETAILS)
    val tabSelectState: StateFlow<Int> = _tabSelectMutableState

    private val _showDatePicker = MutableStateFlow(false)
    val showDatePicker: StateFlow<Boolean> = _showDatePicker

    private val _showEndDatePicker = MutableStateFlow(false)
    val showEndDatePicker: StateFlow<Boolean> = _showEndDatePicker

    private val _showTimePicker = MutableStateFlow(false)
    val showTimePicker: StateFlow<Boolean> = _showTimePicker

    private val _showEndTimePicker = MutableStateFlow(false)
    val showEndTimePicker: StateFlow<Boolean> = _showEndTimePicker

    private val _rideDetailsMutableState = MutableStateFlow(CreateRideModel())
    val rideDetailsState: StateFlow<CreateRideModel> = _rideDetailsMutableState

    private val _selectedUserCount = MutableStateFlow(0)
    val selectedUserCount: StateFlow<Int> = _selectedUserCount

    private val _showRideTypeError = MutableStateFlow(false)
    val showRideTypeError: StateFlow<Boolean> = _showRideTypeError

    private val _showRideTitleError = MutableStateFlow(false)
    val showRideTitleError: StateFlow<Boolean> = _showRideTitleError

    private val _showRideDateError = MutableStateFlow(false)
    val showRideDateError: StateFlow<Boolean> = _showRideDateError

    private val _showRideEndDateError = MutableStateFlow(false)
    val showRideEndDateError: StateFlow<Boolean> = _showRideEndDateError

    private val _showRideTimeError = MutableStateFlow(false)
    val showRideTimeError: StateFlow<Boolean> = _showRideTimeError

    private val _showRideEndTimeError = MutableStateFlow(false)
    val showRideEndTimeError: StateFlow<Boolean> = _showRideEndTimeError

    private val _showRideStartLocError = MutableStateFlow(false)
    val showRideStartLocError: StateFlow<Boolean> = _showRideStartLocError

    private val _showRideEndLocError = MutableStateFlow(false)
    val showRideEndLocError: StateFlow<Boolean> = _showRideEndLocError

    private val _showRideAssemblyLocError = MutableStateFlow(false)
    val showRideAssemblyLocError: StateFlow<Boolean> = _showRideAssemblyLocError

    private val _showParticipantTab = MutableStateFlow(true)
    val showParticipantTab: StateFlow<Boolean> = _showParticipantTab

    private val _assemblyPointCheck = MutableStateFlow(false)
    val assemblyPointCheck: StateFlow<Boolean> = _assemblyPointCheck

    private val _fullList = MutableStateFlow(ArrayList<RidersList>())
    private val _ridersListMutable = MutableStateFlow(ArrayList<RidersList>())
    val ridersList: StateFlow<ArrayList<RidersList>> = _ridersListMutable

    /* init {
         _fullList.value = getUsers()
         _ridersListMutable.value = _fullList.value
     }*/

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun isSoloRide(): Boolean {
        if (_rideDetailsMutableState.value.rideType == "Solo Ride") {
            return true
        } else {
            return false
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query

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
        onSearchQueryChanged(_searchQuery.value)
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

    fun setShowDatePicker(value: Boolean) { _showDatePicker.value = value }
    fun setShowEndDatePicker(value: Boolean) { _showEndDatePicker.value = value }
    fun setShowTimePicker(value: Boolean) { _showTimePicker.value = value }
    fun setShowEndTimePicker(value: Boolean) { _showEndTimePicker.value = value }
    fun setShowRideTypeError(value: Boolean) { _showRideTypeError.value = value }
    fun setShowRideTitleError(value: Boolean) { _showRideTitleError.value = value }
    fun setShowRideDateError(value: Boolean) { _showRideDateError.value = value }
    fun setShowRideEndDateError(value: Boolean) { _showRideEndDateError.value = value }
    fun setShowRideTimeError(value: Boolean) { _showRideTimeError.value = value }
    fun setShowRideEndTimeError(value: Boolean) { _showRideEndTimeError.value = value }
    fun setShowRideStartLocError(value: Boolean) { _showRideStartLocError.value = value }
    fun setShowRideEndLocError(value: Boolean) { _showRideEndLocError.value = value }
    fun setShowRideAssemblyLocError(value: Boolean) { _showRideAssemblyLocError.value = value }

    fun routeFieldValidation(): Boolean {
        if (_rideDetailsMutableState.value.startLocation.isNullOrEmpty()) {
            _showRideStartLocError.value = true
            return false
        }
        if (_rideDetailsMutableState.value.endLocation.isNullOrEmpty()) {
            _showRideEndLocError.value = true
            return false
        }
        if (!_assemblyPointCheck.value) {
            if (_rideDetailsMutableState.value.assemblyLocation.isNullOrEmpty()) {
                _showRideAssemblyLocError.value = true
                return false
            }
        }
        return true
    }

    fun getUserCount() {
        _selectedUserCount.value = _ridersListMutable.value.count { it.isSelect }
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
            _rideDetailsMutableState.value.copy(
                endDateMils = dateInMills,
                endDateString = dateString
            )
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

    fun updateAssembleLocation(loc: String) {
        _rideDetailsMutableState.value = _rideDetailsMutableState.value.copy(assemblyLocation = loc)
    }

    fun updateAssembleLocation(lat: Double, lon: Double) {
        _rideDetailsMutableState.value =
            _rideDetailsMutableState.value.copy(assemblyLat = lat, assemblyLon = lon)
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
        _showDatePicker.value = isShow
    }

    fun showTimePicker(isShow: Boolean) {
        _showTimePicker.value = isShow
    }

    fun showEndDatePicker(isShow: Boolean) {
        _showEndDatePicker.value = isShow
    }

    fun showEndTimePicker(isShow: Boolean) {
        _showEndTimePicker.value = isShow
    }

    fun updateParticipantTab(showTab: Boolean) {
        _showParticipantTab.value = showTab
    }

    fun setAssemblyPointCheck(value: Boolean) {
        _assemblyPointCheck.value = value
    }


    open fun getRideType(context: Context): ArrayList<RideType> {
        var type =
            arrayListOf(
                RideType(Constants.SOLO_RIDE, context.getString(R.string.solo_ride)),
                RideType(Constants.GROUP_RIDE, context.getString(R.string.group_ride)),
//                RideType(Constants.OPEN_EVENT, context.getString(R.string.open_event))
            )
        return type
    }

    suspend fun createRide() {
        val totalDistance = FloatArray(2)
        Location.distanceBetween(
            _rideDetailsMutableState.value.startLat ?: 0.0,
            _rideDetailsMutableState.value.startLon ?: 0.0,
            _rideDetailsMutableState.value.endLat ?: 0.0,
            _rideDetailsMutableState.value.endLon ?: 0.0, totalDistance
        )
        val distanceKm: Double = (totalDistance[0] / 1000).toDouble()
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

        var ratings: Map<String, Ratings> =
            _ridersListMutable.value.filter { it.isSelect == true }
                .associate { rider ->
                    rider.id.orEmpty() to Ratings(
                        stars = 0
                    )
                } ?: emptyMap()
        ratings = ratings.plus(
            (userDetails?.uid ?: "") to Ratings(
                stars = 0
            )
        )

        var hasAssemblyPoint: Boolean = false
        var assemblyPoint: String? = null
        var assemblyLat: Double = 0.0
        var assemblyLon: Double = 0.0

        if (_assemblyPointCheck.value) {
            hasAssemblyPoint = false
            assemblyPoint = _rideDetailsMutableState.value.startLocation
            assemblyLat = _rideDetailsMutableState.value.startLat ?: 0.0
            assemblyLon = _rideDetailsMutableState.value.startLon ?: 0.0
        } else {
            hasAssemblyPoint = true
            assemblyPoint = _rideDetailsMutableState.value.assemblyLocation
            assemblyLat = _rideDetailsMutableState.value.assemblyLat ?: 0.0
            assemblyLon = _rideDetailsMutableState.value.assemblyLon ?: 0.0
        }

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
            ),
            hasAssemblyPoint = hasAssemblyPoint,
            assemblyPoint = assemblyPoint,
            assemblyLat = assemblyLat,
            assemblyLon = assemblyLon,
            ratings = ratings

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
                                            imgUrl = it.profilePic
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

    }

    fun startDateValidation(context: Context): Boolean {
        val currentDate = Calendar.getInstance().timeInMillis


        var starDate = Utils.getDate(
            _rideDetailsMutableState.value.dateMils ?: 0,
            _rideDetailsMutableState.value.hour ?: 0,
            _rideDetailsMutableState.value.mins ?: 0,
            _rideDetailsMutableState.value.isAm
        )

        var endDate = Utils.getDate(
            _rideDetailsMutableState.value.endDateMils ?: 0,
            _rideDetailsMutableState.value.endHour ?: 0,
            _rideDetailsMutableState.value.endMins ?: 0,
            _rideDetailsMutableState.value.isEndAm
        )

        if (starDate < currentDate) {
            Toast.makeText(context, context.getString(R.string.start_cannot_past), Toast.LENGTH_SHORT).show()
            return false
            //

        } else if (starDate > endDate) {
            Toast.makeText(context, context.getString(R.string.start_cannot_later), Toast.LENGTH_SHORT).show()
            return false
            //
        }

        return true
    }

    fun endDateValidation(context: Context): Boolean {
        val currentDate = Calendar.getInstance().timeInMillis


        var starDate = Utils.getDate(
            _rideDetailsMutableState.value.dateMils ?: 0,
            _rideDetailsMutableState.value.hour ?: 0,
            _rideDetailsMutableState.value.mins ?: 0,
            _rideDetailsMutableState.value.isAm
        )

        var endDate = Utils.getDate(
            _rideDetailsMutableState.value.endDateMils ?: 0,
            _rideDetailsMutableState.value.endHour ?: 0,
            _rideDetailsMutableState.value.endMins ?: 0,
            _rideDetailsMutableState.value.isEndAm
        )

        if (endDate < currentDate) {
            Toast.makeText(context, context.getString(R.string.end_cannot_past), Toast.LENGTH_SHORT).show()
            return false
            //
        } else if (endDate < starDate) {
            Toast.makeText(context, context.getString(R.string.end_cannot_before_start), Toast.LENGTH_SHORT).show()
            return false
            //
        }

        return true
    }
}