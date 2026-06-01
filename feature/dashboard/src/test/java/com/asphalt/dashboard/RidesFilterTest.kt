package com.asphalt.dashboard.utils

import com.asphalt.android.constants.APIConstants
import com.asphalt.android.model.UserDomain
import com.asphalt.android.model.rides.ParticipantData
import com.asphalt.android.model.rides.RatingsData
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.dashboard.constants.RideStatConstants.HISTORY
import com.asphalt.dashboard.constants.RideStatConstants.UPCOMING
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`


class RidesFilterTest {

    private lateinit var androidUserVM: AndroidUserVM

    private val userId = "user1"

    @Before
    fun setup() {
        androidUserVM = mock(AndroidUserVM::class.java)
    }

    @Test
    fun `getUComingRides returns creator ride with no participants`() {

        val ride = RidesData(
            ridesID = "ride1",
            createdBy = userId,
            rideTitle = "Morning Ride",
            participants = emptyList(),
            rideStatus = 0
        )

        val result = RidesFilter.getUComingRides(
            listOf(ride),
            userId,
            androidUserVM
        )

        assertEquals(1, result.size)
        assertEquals(UPCOMING, result[0].rideStatus)
        assertEquals("ride1", result[0].ridesId)
    }

    @Test
    fun `getUComingRides excludes ended ride created by user`() {

        val ride = RidesData(
            ridesID = "ride1",
            createdBy = userId,
            rideStatus = APIConstants.END_RIDE
        )

        val result = RidesFilter.getUComingRides(
            listOf(ride),
            userId,
            androidUserVM
        )

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getUComingRides includes accepted participant ride`() {

        val participant = ParticipantData(
            userId = userId,
            inviteStatus = APIConstants.RIDE_ACCEPTED
        )

        val ride = RidesData(
            ridesID = "ride1",
            createdBy = "otherUser",
            participants = listOf(participant)
        )

        val result = RidesFilter.getUComingRides(
            listOf(ride),
            userId,
            androidUserVM
        )

        assertEquals(1, result.size)
        assertEquals(UPCOMING, result[0].rideStatus)
    }

    @Test
    fun `getUComingRides excludes invited participant ride`() {

        val participant = ParticipantData(
            userId = userId,
            inviteStatus = APIConstants.RIDE_INVITED
        )

        val ride = RidesData(
            ridesID = "ride1",
            createdBy = "otherUser",
            participants = listOf(participant)
        )

        val result = RidesFilter.getUComingRides(
            listOf(ride),
            userId,
            androidUserVM
        )

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getInvites returns invited ride`() {

        val participant = ParticipantData(
            userId = userId,
            inviteStatus = APIConstants.RIDE_INVITED
        )

        val ride = RidesData(
            ridesID = "ride1",
            createdBy = "owner1",
            participants = listOf(participant)
        )

        `when`(androidUserVM.getUser("owner1"))
            .thenReturn(
                UserDomain(
                    uid = "owner1",
                    name = "John Doe",
                    profilePic = "profile.jpg", email = "hari@test.com", isMechanic = false,
                    primaryBike = "", contactNumber = "", accountCreationDate = 0L
                )
            )

        val result = RidesFilter.getInvites(
            listOf(ride),
            userId,
            androidUserVM
        )

        assertEquals(1, result.size)
        assertEquals("John Doe", result[0].createdUSerName)
        assertEquals("profile.jpg", result[0].profileImageUrl)
    }

    @Test
    fun `getInvites excludes owner rides`() {

        val ride = RidesData(
            ridesID = "ride1",
            createdBy = userId
        )

        val result = RidesFilter.getInvites(
            listOf(ride),
            userId,
            androidUserVM
        )

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getHistoryRide returns ended ride created by user`() {

        val ride = RidesData(
            ridesID = "ride1",
            createdBy = userId,
            rideStatus = APIConstants.END_RIDE
        )

        val result = RidesFilter.getHistoryRide(
            listOf(ride),
            userId
        )

        assertEquals(1, result.size)
        assertEquals(HISTORY, result[0].rideStatus)
    }

    @Test
    fun `getHistoryRide returns participant ended ride`() {

        val participant = ParticipantData(
            userId = userId,
            inviteStatus = APIConstants.END_RIDE
        )

        val ride = RidesData(
            ridesID = "ride1",
            createdBy = "owner1",
            participants = listOf(participant)
        )

        val result = RidesFilter.getHistoryRide(
            listOf(ride),
            userId
        )

        assertEquals(1, result.size)
        assertEquals(HISTORY, result[0].rideStatus)
    }

    @Test
    fun `getHistoryRide populates rating information`() {

        val rating = RatingsData(
            userId = userId,
            stars = 5
        )

        val ride = RidesData(
            ridesID = "ride1",
            createdBy = userId,
            rideStatus = APIConstants.END_RIDE,
            ratings = listOf(rating),
            imageCount = 3
        )

        val result = RidesFilter.getHistoryRide(
            listOf(ride),
            userId
        )

        assertEquals(1, result.size)
        assertEquals(5, result[0].starsCount)
        assertEquals(3, result[0].imageCount)
    }

    @Test
    fun `getHistoryRide returns empty when no matching rides`() {

        val ride = RidesData(
            ridesID = "ride1",
            createdBy = "otherUser",
            rideStatus = 0
        )

        val result = RidesFilter.getHistoryRide(
            listOf(ride),
            userId
        )

        assertTrue(result.isEmpty())
    }
}