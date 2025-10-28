package com.asphalt.createride.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.asphalt.android.datastore.DataStoreManager
import com.asphalt.android.model.createride.CreateRideRoot
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.viewmodel.createridevm.CreateRideVm
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.createride.model.CreateRideModel
import com.asphalt.createride.model.RideType
import com.asphalt.createride.model.RidersList
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class CreateRideScreenViewModel : ViewModel(), KoinComponent {
    val showToast = mutableStateOf(false)
    val showLoader = mutableStateOf(false)
    val createVm: CreateRideVm = CreateRideVm()

    val data: DataStoreManager by inject()
    val userRepoImpl: UserRepoImpl by inject()

    //var fullList = getUsers()
    private val _tabSelectMutableState: MutableState<Int> = mutableStateOf(Constants.TAB_DETAILS)
    val tabSelectState: State<Int> = _tabSelectMutableState
    val show_datePicker = mutableStateOf(false)

    val show_timePicker = mutableStateOf(false)

    private val _rideDetailsMutableState = mutableStateOf(CreateRideModel())
    val rideDetailsState: State<CreateRideModel> = _rideDetailsMutableState

    val selectedUserCount = mutableStateOf(0)

    val _showRideTypeError = mutableStateOf(false)
    val _showRideTitleError = mutableStateOf(false)
    val _showRideDateError = mutableStateOf(false)
    val _showRideTimeError = mutableStateOf(false)
    val _showRideStartLocError = mutableStateOf(false)
    val _showRideEndLocError = mutableStateOf(false)


    private val _fullList = mutableStateOf(ArrayList<RidersList>())
    private val _ridersListMutable: MutableState<ArrayList<RidersList>> =
        mutableStateOf(arrayListOf())
    val ridersList: State<ArrayList<RidersList>> = _ridersListMutable

    init {
        _fullList.value = getUsers()
        _ridersListMutable.value = _fullList.value
    }

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

    fun updateUerList(isSelcted: Boolean, id: Int?) {
        _fullList.value = ArrayList(_fullList.value.map { rider ->
            if (rider.id == id) {
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

    fun updateTime(hrs: Int?, min: Int?, isAm: Boolean, time_text: String) {
        _rideDetailsMutableState.value =
            _rideDetailsMutableState.value.copy(
                hour = hrs,
                mins = min,
                isAm = isAm,
                displayTime = time_text
            )
    }

    fun updateStartLocation(loc: String) {
        _rideDetailsMutableState.value = _rideDetailsMutableState.value.copy(startLocation = loc)
    }

    fun updateEnLocation(loc: String) {
        _rideDetailsMutableState.value = _rideDetailsMutableState.value.copy(endLocation = loc)
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
        val userDetails = userRepoImpl.getUserDetails()
        userDetails?.uid
        var createRide: CreateRideRoot = CreateRideRoot(
            rideType = _rideDetailsMutableState.value.rideType,
            rideTitle = _rideDetailsMutableState.value.rideTitle,
            description = _rideDetailsMutableState.value.description,
            dateMils = _rideDetailsMutableState.value.dateMils,
            dateString = _rideDetailsMutableState.value.dateString,
            hour = _rideDetailsMutableState.value.hour,
            mins = _rideDetailsMutableState.value.mins,
            isAm = _rideDetailsMutableState.value.isAm,
            displayTime = _rideDetailsMutableState.value.displayTime,
            startLocation = _rideDetailsMutableState.value.startLocation,
            endLocation = _rideDetailsMutableState.value.endLocation
        )
        showLoader.value = true
        var response = createVm.createRid(userDetails?.uid ?: "", createRide)
        if (response) {
            showLoader.value = false
            showToast.value = true
        } else {
            showLoader.value = false
            showToast.value = false
        }
    }

    fun getUsers(): ArrayList<RidersList> {
        var list = arrayListOf(

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
        return list;
        //_ridersListMutable.value = list
    }

}