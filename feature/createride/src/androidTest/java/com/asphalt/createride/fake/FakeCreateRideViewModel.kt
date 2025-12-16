package com.asphalt.createride.fake

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.createride.model.RideType
import com.asphalt.createride.viewmodel.CreateRideScreenViewModel

class FakeCreateRideViewModel : CreateRideScreenViewModel() {
    override val tabSelectState = mutableStateOf(Constants.TAB_DETAILS)
    override val _showRideTypeError = mutableStateOf(false)
    //val selectedRideType = mutableStateOf("")
    override fun getRideType(context: Context): ArrayList<RideType> {
        var type =
            arrayListOf(
                RideType(Constants.SOLO_RIDE, context.getString(R.string.solo_ride)),
                RideType(Constants.GROUP_RIDE, context.getString(R.string.group_ride)),
            )
        return type
    }
}