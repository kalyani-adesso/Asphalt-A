package com.asphalt.createride.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.asphalt.commonui.constants.Constants

class CreateRideScreenViewModel : ViewModel() {
    private val _tabSelectMutableState: MutableState<Int> = mutableStateOf(Constants.TAB_DETAILS)
    val tabSelectState: State<Int> = _tabSelectMutableState


    fun updateTab(tab: Int) {

        _tabSelectMutableState.value += tab
    }
}