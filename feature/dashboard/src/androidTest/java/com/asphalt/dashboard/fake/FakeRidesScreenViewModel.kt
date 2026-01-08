package com.asphalt.dashboard.fake

import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.dashboard.constants.RideStatConstants
import com.asphalt.dashboard.data.YourRideDataModel
import com.asphalt.dashboard.viewmodels.RidesScreenViewModel
import org.mockito.Mockito.mock

class FakeRidesScreenViewModel {
    fun fakeUpcomingRide() = YourRideDataModel(
        ridesId = "ride_123",
        title = "Sunday Morning Ride",
        place = "Central Park - Central Tower",
        rideStatus = RideStatConstants.UPCOMING,
        date = "Sat, Jan 31",
        startTime = "09:42 AM",
        endDateDisplay = "Sun, Feb 01",
        endTime = "09:42 AM",
        riders = 4
    )
}