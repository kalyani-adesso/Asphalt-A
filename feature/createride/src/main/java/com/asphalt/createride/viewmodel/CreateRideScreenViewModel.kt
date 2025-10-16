package com.asphalt.createride.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.createride.model.RideType

class CreateRideScreenViewModel : ViewModel() {
    private val _tabSelectMutableState: MutableState<Int> = mutableStateOf(Constants.TAB_DETAILS)
    val tabSelectState: State<Int> = _tabSelectMutableState


    fun updateTab(tab: Int) {

        _tabSelectMutableState.value += tab
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
}