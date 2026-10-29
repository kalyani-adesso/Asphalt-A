package com.asphalt.dashboard

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.rides.CreateRideRoot
import com.asphalt.android.model.rides.UserInvites
import com.asphalt.android.network.rides.RidesApIService
import com.asphalt.android.repository.rides.RidesRepository
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class SinlgeRideApiTest {

    @Mock
    lateinit var apiService: RidesApIService

    private lateinit var repository: RidesRepository

    @Before
    fun setup() {
        repository = RidesRepository(apiService)
    }

    @Test
    fun getSingleRide_success() = runTest {
        val rideId = "ride123"

        val createRideRoot = CreateRideRoot().apply {
            userID = "user1"
            rideTitle = "Sunday Ride"
            description = "Morning Ride"
            rideType = "GROUP"
        }

        `when`(apiService.getSingleRide(rideId))
            .thenReturn(APIResult.Success(createRideRoot))

        val result = repository.getSingeRide(rideId)

        assertTrue(result is APIResult.Success)

        val ride = (result as APIResult.Success).data

        assertEquals(rideId, ride.ridesID)
        assertEquals("user1", ride.createdBy)
        assertEquals("Sunday Ride", ride.rideTitle)
        assertEquals("Morning Ride", ride.description)
        assertEquals("GROUP", ride.rideType)

        verify(apiService).getSingleRide(rideId)
    }

    @Test
    fun getSingleRide_error() = runTest {
        val rideId = "ride123"

        val error = APIResult.Error(Exception("Network error"))

        `when`(apiService.getSingleRide(rideId))
            .thenReturn(error)

        val result = repository.getSingeRide(rideId)

        assertTrue(result is APIResult.Error)

        verify(apiService).getSingleRide(rideId)
    }
}