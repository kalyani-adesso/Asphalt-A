package com.asphalt.createride.fake

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.createride.model.CreateRideModel
import com.asphalt.createride.model.RideType
import com.asphalt.createride.model.RidersList
import com.asphalt.createride.viewmodel.CreateRideScreenViewModel

class FakeCreateRideViewModel : CreateRideScreenViewModel() {
    override val tabSelectState = mutableStateOf(Constants.TAB_DETAILS)
    override val _showRideTypeError = mutableStateOf(false)
    private val _rideDetailsMutableState = mutableStateOf(CreateRideModel())
    override val rideDetailsState: State<CreateRideModel> = _rideDetailsMutableState

    private val _ridersListMutable: MutableState<ArrayList<RidersList>> =
        mutableStateOf(arrayListOf())
    override val ridersList: State<ArrayList<RidersList>> = _ridersListMutable

    override fun getRideType(context: Context): ArrayList<RideType> {
        var type =
            arrayListOf(
                RideType(Constants.SOLO_RIDE, context.getString(R.string.solo_ride)),
                RideType(Constants.GROUP_RIDE, context.getString(R.string.group_ride)),
            )
        return type
    }

    init {
        _rideDetailsMutableState.value = _rideDetailsMutableState.value.copy(
            rideTitle = "Sunday Morning Ride",
            rideType = "Open",
            startLocation = "Downtown",
            endLocation = "Beach",
            dateString = "12 Oct 2025",
            displayTime = "06:00 AM"
        )

        _ridersListMutable.value = arrayListOf(
            RidersList(isSelect = true),
            RidersList(isSelect = true),
            RidersList(isSelect = false)
        )
    }

}