package com.asphalt.profile.repositories

import com.asphalt.profile.data.StatsData
import com.asphalt.profile.data.statsData
import kotlinx.coroutines.delay

class TotalStatsRepo {
    suspend fun getStats(): StatsData{
        delay(200)
        return statsData
    }
}