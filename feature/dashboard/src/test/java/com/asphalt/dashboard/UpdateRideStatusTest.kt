package com.asphalt.dashboard

import com.asphalt.android.constants.APIConstants
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.rides.UserInvites
import com.asphalt.android.network.rides.RidesApIService
import com.asphalt.android.repository.rides.RidesRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class UpdateRideStatusTest {

    @Mock
    lateinit var apiService: RidesApIService

    private lateinit var repository: RidesRepository

    @Before
    fun setup() {
        repository = RidesRepository(apiService)
    }

    @Test
    fun `changeRideInviteStatus should send accepted status`() = runBlocking {
        // Arrange
        val rideId = "ride123"
        val userId = "user123"

        whenever(
            apiService.changeRideInviteStatus(
                eq(rideId),
                eq(userId),
                any()
            )
        ).thenReturn(APIResult.Success(Unit))

        // Act
        val result = repository.changeRideInviteStatus(
            rideID = rideId,
            currentUid = userId,
            inviteStatus = APIConstants.RIDE_ACCEPTED
        )

        // Assert
        assertTrue(result is APIResult.Success)

        val captor = argumentCaptor<UserInvites>()

        verify(apiService).changeRideInviteStatus(
            eq(rideId),
            eq(userId),
            captor.capture()
        )

        assertEquals(
            APIConstants.RIDE_ACCEPTED,
            captor.firstValue.acceptInvite
        )
    }

    @Test
    fun `changeRideInviteStatus should send declined status`() = runBlocking {
        // Arrange
        val rideId = "ride123"
        val userId = "user123"

        whenever(
            apiService.changeRideInviteStatus(
                eq(rideId),
                eq(userId),
                any()
            )
        ).thenReturn(APIResult.Success(Unit))

        // Act
        val result = repository.changeRideInviteStatus(
            rideID = rideId,
            currentUid = userId,
            inviteStatus = APIConstants.RIDE_DECLINED
        )

        // Assert
        assertTrue(result is APIResult.Success)

        val captor = argumentCaptor<UserInvites>()

        verify(apiService).changeRideInviteStatus(
            eq(rideId),
            eq(userId),
            captor.capture()
        )

        assertEquals(
            APIConstants.RIDE_DECLINED,
            captor.firstValue.acceptInvite
        )
    }
}