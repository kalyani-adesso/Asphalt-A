package com.asphalt.dashboard.repository

import com.asphalt.dashboard.data.AdventureJourneyDto
import com.asphalt.dashboard.data.JourneyDataUIModel
import com.asphalt.dashboard.data.adventureJourneyDummyDataList
import com.asphalt.dashboard.sealedclasses.RideGraphLegend
import kotlinx.coroutines.delay

class AdventureJourneyRepo {
    suspend fun fetchAdventureData(id: Int): List<JourneyDataUIModel> {
        val choiceData =
            adventureJourneyDummyDataList.find { choiceData -> choiceData.choiceId == id }
        delay(200)
        return choiceData?.data.toDomain()
    }

    private fun AdventureJourneyDto?.toDomain(): List<JourneyDataUIModel> {
        return listOf(
            JourneyDataUIModel(RideGraphLegend.TotalRides, this?.totalRides?.toFloat() ?: 0f),
            JourneyDataUIModel(
                RideGraphLegend.PlacesExplored,
                this?.placesExplored?.toFloat() ?: 0f
            ),
            JourneyDataUIModel(RideGraphLegend.RideGroups, this?.rideGroups?.toFloat() ?: 0f),
            JourneyDataUIModel(RideGraphLegend.RideInvites, this?.rideInvites?.toFloat() ?: 0f),
        )
    }
}


