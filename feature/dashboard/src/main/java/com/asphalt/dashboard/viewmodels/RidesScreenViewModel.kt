package com.asphalt.dashboard.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.dashboard.constants.RideStatConstants
import com.asphalt.dashboard.data.YourRideDataModel
import com.asphalt.dashboard.data.YourRideRoot
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class RidesScreenViewModel : ViewModel(), KoinComponent {
    val ridesRepo: RidesRepository by inject()
    val userRepoImpl: UserRepoImpl by inject()
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

    fun getRides1() {

        viewModelScope.launch {
            var user = userRepoImpl.getUserDetails()
            val apiResult = APIHelperUI.runWithLoader {
                ridesRepo.getAllRide()
            }
            APIHelperUI.handleApiResult(apiResult, viewModelScope) { response ->

                var resp = mapToYourRideDataModel(response, user?.uid ?: "")
                var upcomiList = ArrayList<YourRideDataModel>()
                upcomiList.addAll(resp)
                var ridesList =
                    YourRideRoot(upcoming = upcomiList, )//history = hirtoryList, invite = inviteList
                _ridesListMutableState.value = ridesList
            }
            //response.mapApiResult { it }
        }

    }


    fun mapToYourRideDataModel(
        allRides: List<RidesData>,
        userId: String
    ): List<YourRideDataModel> {
        // val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        return allRides.mapNotNull { ride ->

            val rideStatus: String? = when {
                ride.userID == userId -> "Queue"

                else -> {
                    val participant = ride.participants.find { it.userId == userId }
                    participant?.let {
                        when (it.inviteStatus) {
                            1 -> "Upcoming"
                            else -> ""
                        }
                    }
                }
            }


            if (rideStatus == null) return@mapNotNull null

            YourRideDataModel(
                title = ride.rideTitle ?: "",
                place = ride.startLocation ?: "",
                rideStatus = rideStatus,
                //date = ride.startDate?.let { dateFormatter.format(Date(it)) } ?: "",
                date = " ${ride.startDate}",
                riders = ride.participants.size.toString()
            )
        }
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