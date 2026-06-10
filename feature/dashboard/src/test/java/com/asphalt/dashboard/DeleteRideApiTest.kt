package com.asphalt.dashboard

import com.asphalt.android.model.APIResult
import com.asphalt.android.network.rides.RidesApIService
import com.asphalt.android.repository.rides.RidesRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class DeleteRideApiTest {

    @Mock
    lateinit var apiService: RidesApIService

    private lateinit var repository: RidesRepository

    @Before
    fun setup() {
        repository = RidesRepository(apiService)
    }

    @Test
    fun deleteRide_returnsSuccess() {
        runBlocking {
            // Given
            val rideId = "ride123"
            val expectedResult = APIResult.Success(Unit)

            whenever(apiService.deleteRide(rideId))
                .thenReturn(expectedResult)

            // When
            val result = repository.deleteRide(rideId)

            // Then
            assertEquals(expectedResult, result)
            verify(apiService).deleteRide(rideId)
        }
    }

    @Test
    fun deleteRide_returnsError() {
        runBlocking {
            // Given
            val rideId = "ride123"

            val expectedResult = APIResult.Error(
                exception = Exception("Delete failed"),
                code = 500
            )

            whenever(apiService.deleteRide(rideId))
                .thenReturn(expectedResult)

            // When
            val result = repository.deleteRide(rideId)

            // Then
            assertTrue(result is APIResult.Error)
            assertEquals(500, (result as APIResult.Error).code)

            verify(apiService).deleteRide(rideId)
        }
    }
}