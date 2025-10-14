package com.asphalt.dashboard.repository

import com.asphalt.dashboard.data.AdventureJourneyDto
import com.asphalt.dashboard.data.JourneyData
import com.asphalt.dashboard.data.adventureJourneyDummyDataList
import com.asphalt.dashboard.sealedclasses.RideGraphLegend
import kotlinx.coroutines.delay

class AdventureJourneyRepo {
    suspend fun fetchAdventureData(id: Int): List<JourneyData> {
        val choiceData =
            adventureJourneyDummyDataList.find { choiceData -> choiceData.choiceId == id }
        delay(200)
        return choiceData?.data.toDomain()
    }

    private fun AdventureJourneyDto?.toDomain(): List<JourneyData> {
        return listOf(
            JourneyData(RideGraphLegend.TotalRides, this?.totalRides?.toFloat() ?: 0f),
            JourneyData(
                RideGraphLegend.PlacesExplored,
                this?.placesExplored?.toFloat() ?: 0f
            ),
            JourneyData(RideGraphLegend.RideGroups, this?.rideGroups?.toFloat() ?: 0f),
            JourneyData(RideGraphLegend.RideInvites, this?.rideInvites?.toFloat() ?: 0f),
        )
    }
}


