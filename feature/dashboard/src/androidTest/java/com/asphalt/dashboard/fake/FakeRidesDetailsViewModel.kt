package com.asphalt.dashboard.fake

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.asphalt.android.constants.APIConstants
import com.asphalt.android.model.RidersList
import com.asphalt.android.model.rides.ParticipantData
import com.asphalt.android.model.rides.RidesData
import com.asphalt.commonui.R
import com.asphalt.dashboard.viewmodels.RidesDetailsViewModel

class FakeRidesDetailsViewModel : RidesDetailsViewModel() {
    val ridesDetails = mutableStateOf<RidesData?>(null)
    override val ridesData: androidx.compose.runtime.State<RidesData?> = ridesDetails

    private val _fakeRiders = mutableStateOf<List<RidersList>>(emptyList())
    override val ridersList: State<List<RidersList>> = _fakeRiders

    fun setRide(data: RidesData=fakeRideData()) {
        ridesDetails.value = data
    }

    fun setRiders(list: List<RidersList> = fakeRiders()) {
        _fakeRiders.value = list
    }

    fun setDeleteVisible(visible: Boolean) {
        showDeleteButton.value = visible
    }

    private fun fakeRideData(): RidesData {
        return RidesData(
            ridesID = "ride_1",
            createdBy = "user_1",
            rideTitle = "Morning Ride",
            startLocation = "New York",
            endLocation = "Boston",
            startDate = 1700000000000,
            endDate = 1700003600000,
            participants = listOf(
                ParticipantData("user_2", APIConstants.RIDE_ACCEPTED),
                ParticipantData("user_3", APIConstants.RIDE_INVITED),
                ParticipantData("user_4", APIConstants.RIDE_DECLINED)
            )
        )
    }

    private fun fakeRiders(): List<RidersList> {
        return listOf(
            RidersList(
                uid = "user_1",
                name = "You",
                profilePic = "",
                isOrganizer = true,
                inviteStatus = APIConstants.RIDE_ACCEPTED,
                displayStatusString = R.string.ride_creator
            ),
            RidersList(
                uid = "user_2",
                name = "Alex",
                profilePic = "",
                isOrganizer = false,
                inviteStatus = APIConstants.RIDE_ACCEPTED,
                displayStatusString = R.string.confirmed
            ),
            RidersList(
                uid = "user_3",
                name = "Sam",
                profilePic = "",
                isOrganizer = false,
                inviteStatus = APIConstants.RIDE_INVITED,
                displayStatusString = R.string.waiting_response
            )
        )
    }

}