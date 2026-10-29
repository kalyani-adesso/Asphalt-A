package com.asphalt.dashboard

import com.asphalt.android.model.APIResult
import com.asphalt.android.network.rides.RidesApIService
import com.asphalt.android.repository.rides.RidesRepository
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class RateRideApiTest {
    @Mock
    lateinit var apiService: RidesApIService

    private lateinit var repository: RidesRepository

    @Before
    fun setup() {
        repository = RidesRepository(apiService)
    }

    @Test
    fun `rateYourRide should return success when api call succeeds`() {
        runTest {
            // Given
            val rideId = "ride123"
            val userId = "user123"
            val stars = 5
            val comments = "Excellent ride"

            val expectedResult: APIResult<Unit> = APIResult.Success(Unit)

            whenever(
                apiService.rateYourRide(
                    rideId,
                    userId,
                    stars,
                    comments
                )
            ).thenReturn(expectedResult)

            // When
            val result = repository.rateYourRide(
                rideId,
                userId,
                stars,
                comments
            )

            // Then
            assertEquals(expectedResult, result)

            verify(apiService).rateYourRide(
                rideId,
                userId,
                stars,
                comments
            )
        }
    }

    @Test
    fun `rateYourRide should return error when api call fails`() = runTest {
        // Given
        val rideId = "ride123"
        val userId = "user123"
        val stars = 1
        val comments = "Bad ride"

        val expectedResult: APIResult<Unit> =
            APIResult.Error(Exception("Network Error"))

        whenever(
            apiService.rateYourRide(
                rideId,
                userId,
                stars,
                comments
            )
        ).thenReturn(expectedResult)

        // When
        val result = repository.rateYourRide(
            rideId,
            userId,
            stars,
            comments
        )

        // Then
        assertTrue(result is APIResult.Error)

        verify(apiService).rateYourRide(
            rideId,
            userId,
            stars,
            comments
        )
    }

}