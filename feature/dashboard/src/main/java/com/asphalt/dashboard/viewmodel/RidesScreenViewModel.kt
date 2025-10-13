package com.asphalt.dashboard.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.asphalt.dashboard.constants.RideStatConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RidesScreenViewModel : ViewModel() {
    private val _tabSelectionMutableFlow: MutableState<Int> = mutableStateOf(
        RideStatConstants.UPCOMING_RIDE
    )
    val tabSelectFlow: State<Int> = _tabSelectionMutableFlow

    fun updateTab(tab: Int) {
        _tabSelectionMutableFlow.value = tab
    }
}