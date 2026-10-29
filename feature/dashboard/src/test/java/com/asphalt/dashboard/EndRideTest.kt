package com.asphalt.dashboard

import com.asphalt.android.model.APIResult
import com.asphalt.android.network.rides.RidesApIService
import com.asphalt.android.repository.rides.RidesRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class EndRideTest {
    @Mock
    lateinit var apiService: RidesApIService

    private lateinit var repository: RidesRepository

    @Before
    fun setup() {
        repository = RidesRepository(apiService)
    }

    @Test
    fun endRide_returnsSuccessResult() {
        runBlocking {
            // Arrange
            val rideId = "ride123"
            val rideJoinedId = "joined123"

            val expectedResult: APIResult<Unit> = APIResult.Success(Unit)

            Mockito.`when`(
                apiService.endRide(rideId, rideJoinedId)
            ).thenReturn(expectedResult)

            // Act
            val result = repository.endRide(rideId, rideJoinedId)

            // Assert
            assertEquals(expectedResult, result)

            Mockito.verify(apiService).endRide(
                rideId,
                rideJoinedId
            )
        }
    }

    @Test
    fun endRide_returnsErrorResult() {
        runBlocking {
            // Arrange
            val rideId = "ride123"
            val rideJoinedId = "joined123"

            val expectedResult: APIResult<Unit> =
                APIResult.Error(Exception("API failure"))

            Mockito.`when`(
                apiService.endRide(rideId, rideJoinedId)
            ).thenReturn(expectedResult)

            // Act
            val result = repository.endRide(rideId, rideJoinedId)

            // Assert
            assertEquals(expectedResult, result)

            Mockito.verify(apiService).endRide(
                rideId,
                rideJoinedId
            )
        }
    }

}