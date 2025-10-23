package com.asphalt.dashboard.viewmodels

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
            title = "Kanyakumari Trip",
            place = "Kochi - Kanyakumari",
            rideStatus = "Upcoming".uppercase(),
            date = "Sun, Oct 26 - 02:00 PM", "3 Riders"
        )
        var upcoming1 = YourRideDataModel(
            title = "Trip to Moonnar",
            place = "Kochi - Moonnar",
            rideStatus = "Queue".uppercase(),
            date = "Sun, Oct 26 - 04:00 AM", "3 Riders"
        )
        var upcoming2 = YourRideDataModel(
            title = "Weekend Coast Ride",
            place = "Kochi - Vagamon",
            rideStatus = "Queue".uppercase(),
            date = "Thu, Oct 23 - 05:00 AM", "3 Riders"
        )

        var hirtory = YourRideDataModel(
            title = "Weekend Coast Ride",
            place = "Kochi - Kanyakumari",
            rideStatus = "Completed".uppercase(),
            date = "Sun, Jun 22", "3 Riders"
        )
        var hirtory1 = YourRideDataModel(
            title = "Vagamon trip",
            place = "Kochi - Vagamon",
            rideStatus = "Completed".uppercase(),
            date = "Tue, Jun 24", "3 Riders"
        )

        var invites = YourRideDataModel(
            title = "Invite from Sooraj",
            place = "Kochi - Kanyakumari",
            rideStatus = "",
            date = "Tomorrow - 08:00 AM", "3 Riders"
        )

        var invites1 = YourRideDataModel(
            title = "Invite from Sreedev",
            place = "Kochi - Wayanad",
            rideStatus = "",
            date = "Tomorrow - 08:00 AM", "3 Riders"
        )

        var upcomiList = ArrayList<YourRideDataModel>()
        upcomiList.add(upcoming)
        upcomiList.add(upcoming1)
        upcomiList.add(upcoming2)



        var hirtoryList = ArrayList<YourRideDataModel>()
        hirtoryList.add(hirtory)
        hirtoryList.add(hirtory1)



        var inviteList = ArrayList<YourRideDataModel>()
        inviteList.add(invites)
        inviteList.add(invites1)


        var ridesList =
            YourRideRoot(upcoming = upcomiList, history = hirtoryList, invite = inviteList)
        _ridesListMutableState.value = ridesList
    }
}