package com.asphalt.dashboard.repository

import com.asphalt.dashboard.data.DashboardRideInvite
import com.asphalt.dashboard.data.dummyRideInvites
import kotlinx.coroutines.delay

class DashboardRideInviteRepo {
    suspend fun getDashboardRideInvites(): List<DashboardRideInvite>{
        delay(200)
        return dummyRideInvites
    }
}