package com.asphalt.dashboard.repository

import com.asphalt.dashboard.data.RidePerMonthDto
import com.asphalt.dashboard.data.RideStatData
import com.asphalt.dashboard.data.dummyRideDataList
import com.asphalt.dashboard.sealedclasses.RideStatType
import kotlinx.coroutines.delay

class PerMonthRideStatsRepo {
    suspend fun getRideStats(month: Int, year: Int): List<RideStatData> {
        val perMonthData = dummyRideDataList.find {
            it.rideDate.month == month && it.rideDate.year == year
        }
        delay(30)
        return perMonthData.toDomain()
    }

    private fun RidePerMonthDto?.toDomain(): List<RideStatData> {
        return with(this?.perMonthRideStatistics) {
            listOf(
                RideStatData(
                    RideStatType.TotalRides, this?.totalRides ?: 0
                ),
                RideStatData(RideStatType.TotalKms, this?.totalKms ?: 0),
                RideStatData(RideStatType.Locations, this?.locations ?: 0)
            )
        }
    }
}