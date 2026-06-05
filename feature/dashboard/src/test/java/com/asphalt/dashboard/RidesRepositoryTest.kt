package com.asphalt.dashboard

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.rides.CreateRideRoot
import com.asphalt.android.network.rides.RidesApIService
import com.asphalt.android.repository.rides.RidesRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test

@RunWith(MockitoJUnitRunner::class)
class RidesRepositoryTest {
    @Mock
    lateinit var apiService: RidesApIService
    private lateinit var repository: RidesRepository

    @Before
    fun setup() {
        repository = RidesRepository(apiService)
    }

    @Test
    fun getAllRide_returnsRideList_whenApiSuccess() = runTest {

        // Arrange
        val response = mapOf(
            "ride1" to CreateRideRoot().apply {
                userID = "user1"
                rideTitle = "Munnar Ride"
                startDate = 1710000000L
                startLocation = "Kochi"
                endLocation = "Munnar"
            }
        )

        whenever(apiService.getAllRide())
            .thenReturn(APIResult.Success(response))

        // Act
        val result = repository.getAllRide()

        // Assert
        assertTrue(result is APIResult.Success)

        val rides = (result as APIResult.Success).data

        assertEquals(1, rides.size)
        assertEquals("ride1", rides[0].ridesID)
        assertEquals("Munnar Ride", rides[0].rideTitle)

        verify(apiService).getAllRide()
    }

    @Test
    fun getAllRide_returnsError_whenApiFails() = runTest {

        val exception = Exception("Network Error")

        whenever(apiService.getAllRide())
            .thenReturn(APIResult.Error(exception))

        val result = repository.getAllRide()

        assertTrue(result is APIResult.Error)

        verify(apiService).getAllRide()
    }
}