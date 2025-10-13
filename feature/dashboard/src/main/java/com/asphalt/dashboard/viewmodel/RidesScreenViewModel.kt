package com.asphalt.dashboard.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.asphalt.dashboard.constants.RideStatConstants
import com.asphalt.dashboard.data.YourRideDataModel
import com.asphalt.dashboard.data.YourRideRoot

class RidesScreenViewModel : ViewModel() {
    private val _tabSelectionMutableFlow: MutableState<Int> = mutableStateOf(
        RideStatConstants.UPCOMING_RIDE
    )
    val tabSelectFlow: State<Int> = _tabSelectionMutableFlow

    private val _ridesListMutableState: MutableState<YourRideRoot> = mutableStateOf(
        YourRideRoot()
    )
    val ridesListState: State<YourRideRoot> = _ridesListMutableState

    fun updateTab(tab: Int) {
        _tabSelectionMutableFlow.value = tab
    }

    fun getRides() {
        var upcoming = YourRideDataModel(
            title = "Weekend Coast Ride",
            place = "Kochi - Kanyakumari",
            rideStatus = "Upcoming".uppercase(),
            date = "Sun, Oct 21 - 09:00 AM", "3"
        )

        var hirtory = YourRideDataModel(
            title = "Weekend Coast Ride",
            place = "Kochi - Kanyakumari",
            rideStatus = "Completed".uppercase(),
            date = "Sun, Sep 21", "3"
        )

        var invites = YourRideDataModel(
            title = "Invite from Sooraj",
            place = "Kochi - Kanyakumari",
            rideStatus = "",
            date = "Tomorrow - 08:00 AM", "3"
        )

        var upcomiList = ArrayList<YourRideDataModel>()
        upcomiList.add(upcoming)
        upcomiList.add(upcoming)
        upcomiList.add(upcoming)
        upcomiList.add(upcoming)
        upcomiList.add(upcoming)
        upcomiList.add(upcoming)
        upcomiList.add(upcoming)
        upcomiList.add(upcoming)
        upcomiList.add(upcoming)
        upcomiList.add(upcoming)

        var hirtoryList = ArrayList<YourRideDataModel>()
        hirtoryList.add(hirtory)
        hirtoryList.add(hirtory)
        hirtoryList.add(hirtory)
        hirtoryList.add(hirtory)
        hirtoryList.add(hirtory)
        hirtoryList.add(hirtory)
        hirtoryList.add(hirtory)
        hirtoryList.add(hirtory)

        var inviteList = ArrayList<YourRideDataModel>()
        inviteList.add(invites)
        inviteList.add(invites)
        inviteList.add(invites)
        inviteList.add(invites)
        inviteList.add(invites)
        inviteList.add(invites)
        inviteList.add(invites)
        inviteList.add(invites)
        inviteList.add(invites)

        var ridesList =
            YourRideRoot(upcoming = upcomiList, history = hirtoryList, invite = inviteList)
        _ridesListMutableState.value = ridesList
    }
}