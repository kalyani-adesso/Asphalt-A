package com.asphalt.dashboard

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.dashboard.DashboardDTO
import com.asphalt.android.network.rides.RidesApIService
import com.asphalt.android.repository.rides.RidesRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertNull

@RunWith(MockitoJUnitRunner::class)
class RideSummaryTest {

    @Mock
    lateinit var apiService: RidesApIService

    private lateinit var repository: RidesRepository

    @Before
    fun setup() {
        repository = RidesRepository(apiService)
    }

    @Test
    fun getRideSummary_returnsGroupedDashboardData() = runTest {
        val userId = "user123"

        val dtoMap = mapOf(
            "ride1" to DashboardDTO(
                rideID = "ride1",
                rideDistance = 150.0,
                isGroupRide = true,
                startLocation = "Kochi",
                endLocation = "Munnar",
                endRideDate = 1719878400000L // July 2024
            )
        )

        whenever(apiService.getRideSummary(userId))
            .thenReturn(APIResult.Success(dtoMap))

        val result = repository.getRideSummary(userId)

        assertTrue(result is APIResult.Success)

        val data = (result as APIResult.Success).data

        assertEquals(1, data.size)

        val monthGroup = data.first()

        assertEquals(1, monthGroup.perMonthData.size)

        val ride = monthGroup.perMonthData.first()

        assertEquals("ride1", ride.ridesID)
        assertEquals(150.0, ride.rideDistance ?: 0.0, 0.0)
        assertEquals("Kochi", ride.startLocation)
        assertEquals("Munnar", ride.endLocation)

        verify(apiService).getRideSummary(userId)
    }

    @Test
    fun getRideSummary_returnsFailure() = runTest {
        val userId = "user123"

        val dtoMap = mapOf(
            "ride1" to DashboardDTO(
                rideID = "ride1",
                rideDistance = 150.0,
                isGroupRide = true,
                startLocation = "Kochi",
                endLocation = "Munnar",
                endRideDate = 1719878400000L // July 2024
            )
        )

        whenever(apiService.getRideSummary(userId))
            .thenReturn(APIResult.Error(Exception("Error")))

        val result = repository.getRideSummary(userId)

        assertTrue(result is APIResult.Error)

        val data = (result as APIResult.Error).exception

        assertEquals("Error", data.message)



        verify(apiService).getRideSummary(userId)
    }
    @Test
    fun toDashboardDomain_groupsRidesByMonth() {

        val input = mapOf(
            "ride1" to DashboardDTO(
                rideID = "ride1",
                rideDistance = 100.0,
                endRideDate = 1719878400000L
            ),
            "ride2" to DashboardDTO(
                rideID = "ride2",
                rideDistance = 200.0,
                endRideDate = 1719964800000L
            )
        )

        val result = RidesRepository(mock()).run {
            input.toDashboardDomain()
        }

        assertEquals(1, result.size)

        val dashboard = result.first()

        assertEquals(2, dashboard.perMonthData.size)

        assertEquals("ride1", dashboard.perMonthData[0].ridesID)
        assertEquals("ride2", dashboard.perMonthData[1].ridesID)
    }
    @Test
    fun getRideSummary_returnsNull_whenApiReturnsNull() = runTest {
        whenever(apiService.getRideSummary("user123"))
            .thenReturn(null)

        val result = repository.getRideSummary("user123")

        assertNull(result)
    }
}