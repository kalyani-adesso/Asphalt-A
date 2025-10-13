package com.asphalt.dashboard.repository

import com.asphalt.dashboard.data.PlacesVisitedGraphData
import com.asphalt.dashboard.data.PlacesVisitedGraphDto
import com.asphalt.dashboard.data.placesVisitedDummyData
import kotlinx.coroutines.delay
import java.text.DateFormatSymbols

class PlaceVisitedGraphRepo {
    suspend fun fetchPlaceVisitedGraphData(
        startMonthYear: Pair<Int, Int>,
        endMonthYear: Pair<Int, Int>
    ): List<PlacesVisitedGraphData>? {
        val (startMonth, startYear) = startMonthYear
        val (endMonth, endYear) = endMonthYear

        delay(200)
        return placesVisitedDummyData.filter { item ->
            val itemValue = item.year * 12 + item.month
            val startValue = startYear * 12 + startMonth
            val endValue = endYear * 12 + endMonth
            itemValue in startValue..endValue
        }.toDomain()

    }
    private fun List<PlacesVisitedGraphDto>?.toDomain(): List<PlacesVisitedGraphData>? {
        return this?.map { item ->
            val monthName = DateFormatSymbols().shortMonths[item.month - 1]
            PlacesVisitedGraphData(
                month = "$monthName",
                count = item.count
            )
        }
    }


}


