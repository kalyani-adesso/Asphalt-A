package com.asphalt.createride

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.rides.CreateRideRoot
import com.asphalt.android.network.rides.RidesApIService
import com.asphalt.android.repository.rides.RidesRepository
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateRideTest {
    @Mock
    private lateinit var apiService: RidesApIService

    private lateinit var repository: RidesRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = RidesRepository(apiService)
    }


    @Test
    fun createRide_returnsSuccess_whenApiCallSucceeds() = runTest {
        // Arrange
        val request = CreateRideRoot(
            userID = "user123",
            rideTitle = "Sunday Ride",
            description = "Test Ride"
        )

        val expectedResponse = GenericResponse(
            name = "ride123"
        )

        whenever(apiService.createRide(request))
            .thenReturn(APIResult.Success(expectedResponse))

        // Act
        val result = repository.createRide(request)

        // Assert
        assertTrue(result is APIResult.Success)

        val successResult = result as APIResult.Success
        assertEquals(expectedResponse, successResult.data)

        verify(apiService).createRide(request)
    }

    @Test
    fun `createRide returns error when api call fails`() = runTest {
        // Arrange
        val request = CreateRideRoot(
            userID = "user123",
            rideTitle = "Sunday Ride"
        )

        val error = APIResult.Error(
            Exception("Network Error")
        )

        whenever(apiService.createRide(request))
            .thenReturn(error)

        // Act
        val result = repository.createRide(request)

        // Assert
        assertTrue(result is APIResult.Error)

        val errorResult = result as APIResult.Error
        assertEquals("Network Error", errorResult.exception.message)

        verify(apiService).createRide(request)
    }

    @Test
    fun `createRide sends complete ride data to api`() = runTest {
        val request = CreateRideRoot(
            userID = "user123",
            rideType = "Group",
            rideTitle = "Mountain Ride",
            description = "Weekend Ride",
            startDate = 123456789L,
            startLocation = "Kochi",
            endLocation = "Munnar",
            distance = 120.5
        )

        whenever(apiService.createRide(request))
            .thenReturn(APIResult.Success(GenericResponse("ride001")))

        repository.createRide(request)

        verify(apiService).createRide(request)
    }
}
