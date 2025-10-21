package com.asphalt.createride.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.createride.model.CreateRideModel

import com.asphalt.createride.model.RideType
import com.asphalt.createride.model.RidersList

class CreateRideScreenViewModel : ViewModel() {
    private val _tabSelectMutableState: MutableState<Int> = mutableStateOf(Constants.TAB_DETAILS)
    val tabSelectState: State<Int> = _tabSelectMutableState
    val show_datePicker = mutableStateOf(false)

    val show_timePicker = mutableStateOf(false)

    private val _rideDetailsMutableState = mutableStateOf(CreateRideModel())
    val rideDetailsState: State<CreateRideModel> = _rideDetailsMutableState

    private val _ridersListMutable: MutableState<ArrayList<RidersList>> =
        mutableStateOf(getUsers())
    val ridersList: State<ArrayList<RidersList>> = _ridersListMutable

    val selectedUserCount = mutableStateOf(0)

    val _showRideTypeError = mutableStateOf(false)
    val _showRideTitleError = mutableStateOf(false)

    fun fieldValidation(): Boolean {
        if (_rideDetailsMutableState.value.rideType.isNullOrEmpty()) {
            _showRideTypeError.value = true
            return false
        }
        if (_rideDetailsMutableState.value.rideTitle.isNullOrEmpty()) {
            _showRideTitleError.value = true
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

    fun updateUerList(isSelcted: Boolean, id: Int?) {
        _ridersListMutable.value = ArrayList(_ridersListMutable.value.map { ride ->
            if (ride.id == id) {
                ride.copy(isSelect = isSelcted)
            } else {
                ride
            }
        })
        // _ridersListMutable.value = _rideDetailsMutableState.value.map { ride -> if (ride) }


    }


    fun getUsers(): ArrayList<RidersList> {
        var list = arrayListOf(

            RidersList(
                id = 1,
                name = "Harikumar S",
                job = "Software Engineer",
                bike = "Unicorn",
                isSelect = false,
                imgUrl = "https://picsum.photos/id/1/200/300"
            ),
            RidersList(
                id = 2,
                name = "Sreedev",
                job = "Software Engineer",
                bike = "Unicorn",
                isSelect = false,
                imgUrl = "https://picsum.photos/id/1/200/300"

            ),
            RidersList(
                id = 3,
                name = "Vyshak ",
                job = "Software Engineer",
                bike = "Unicorn",
                isSelect = false,
                imgUrl = "https://picsum.photos/id/1/200/300"
            ),
            RidersList(
                id = 4,
                name = "Jerin John",
                job = "Software Engineer",
                bike = "Unicorn",
                isSelect = false,
                imgUrl = "https://picsum.photos/id/1/200/300"
            ),
            RidersList(
                id = 5,
                name = "Vipin Raj",
                job = "Software Engineer",
                bike = "Unicorn",
                isSelect = false,
                imgUrl = "https://picsum.photos/id/1/200/300"
            ),
            RidersList(
                id = 6,
                name = "Pramod Selvaraj",
                job = "Software Engineer",
                bike = "Unicorn",
                isSelect = false,
                imgUrl = "https://picsum.photos/id/1/200/300"
            ),
            RidersList(
                id = 7,
                name = "Vinu V John",
                job = "Software Engineer",
                bike = "Unicorn",
                isSelect = false,
                imgUrl = "https://picsum.photos/id/1/200/300"
            )
        )
        return list;
        //_ridersListMutable.value = list
    }

}