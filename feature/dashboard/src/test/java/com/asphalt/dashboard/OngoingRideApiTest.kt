package com.asphalt.dashboard

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.connectedride.ConnectedRideRoot
import com.asphalt.android.network.rides.RidesApIService
import com.asphalt.android.repository.rides.RidesRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class OngoingRideApiTest {
    private lateinit var repository: RidesRepository

    @Mock
    lateinit var apiService: RidesApIService

    @Before
    fun initialSetup() {
        repository = RidesRepository(apiService)
    }
    @Test
    fun `getOngoingRides should return mapped ConnectedRideDTO list`() = runTest {

        val rideId = "ride123"

        val apiResponse = mapOf(
            "join1" to ConnectedRideRoot(
                rideID = rideId,
                userID = "user1",
                currentLat = 10.2,
                currentLong = 76.3,
                speedInKph = 55.0,
                status = "ONGOING",
                dateTime = 123456789L,
                isRejoined = false
            )
        )

        whenever(apiService.getJoinedRides(rideId))
            .thenReturn(APIResult.Success(apiResponse))

        val result = repository.getOngoingRides(rideId)

        assertTrue(result is APIResult.Success)

        val data = (result as APIResult.Success).data

        assertEquals(1, data.size)

        assertEquals("join1", data[0].rideJoinedID)
        assertEquals("user1", data[0].userID)
        assertEquals(10.2, data[0].currentLat, 0.0)
        assertEquals(76.3, data[0].currentLong, 0.0)
        assertEquals(55.0, data[0].speedInKph, 0.0)
        assertEquals("ONGOING", data[0].status)
        assertFalse(data[0].isRejoined)
    }
    @Test
    fun `getOngoingRides should return empty list`() = runTest {

        val rideId = "ride123"

        whenever(apiService.getJoinedRides(rideId))
            .thenReturn(APIResult.Success(emptyMap()))

        val result = repository.getOngoingRides(rideId)

        assertTrue(result is APIResult.Success)

        val data = (result as APIResult.Success).data

        assertTrue(data.isEmpty())
    }
    @Test
    fun `getOngoingRides should return failure when api fails`() = runTest {

        val rideId = "ride123"

        val error = Exception("Network Error")

        whenever(apiService.getJoinedRides(rideId))
            .thenReturn(APIResult.Error(error))

        val result = repository.getOngoingRides(rideId)

        assertTrue(result is APIResult.Error)
    }
    @Test
    fun `getOngoingRides should map null values to defaults`() = runTest {

        val rideId = "ride123"

        val apiResponse = mapOf(
            "join1" to ConnectedRideRoot(
                rideID = null,
                userID = null,
                currentLat = null,
                currentLong = null,
                speedInKph = null,
                status = null,
                dateTime = null,
                isRejoined = null
            )
        )

        whenever(apiService.getJoinedRides(rideId))
            .thenReturn(APIResult.Success(apiResponse))

        val result = repository.getOngoingRides(rideId)

        val data = (result as APIResult.Success).data.first()

        assertEquals("", data.rideID)
        assertEquals("", data.userID)
        assertEquals(0.0, data.currentLat, 0.0)
        assertEquals(0.0, data.currentLong, 0.0)
        assertEquals(0.0, data.speedInKph, 0.0)
        assertEquals("", data.status)
        assertEquals(0L, data.dateTime)
        assertFalse(data.isRejoined)
    }


}