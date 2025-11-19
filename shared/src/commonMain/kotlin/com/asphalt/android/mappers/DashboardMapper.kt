package com.asphalt.android.mappers

import com.asphalt.android.model.dashboard.DashboardDTO
import com.asphalt.android.model.dashboard.DashboardDomain
import com.asphalt.android.model.dashboard.PerMonthRideDataDomain
import com.asphalt.android.model.dashboard.RideDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


fun Map<String, DashboardDTO>?.toPerMonthRideDataDomain(): List<PerMonthRideDataDomain> {
    return this?.map { (id, rowData) ->
        PerMonthRideDataDomain(
            ridesID = id,
            rideDistance = rowData.rideDistance,
            isGroupRide = rowData.isGroupRide,
            startLocation = rowData.startLocation,
            endLocation = rowData.endLocation,
            isOrganiserGroupRide = rowData.isOrganiserGroupRide,
            isParticipantGroupRide = rowData.isParticipantGroupRide,
            endRideDate = rowData.endRideDate
        )
    } ?: emptyList()
}


@OptIn(ExperimentalTime::class)
fun List<PerMonthRideDataDomain>.mapAndGroupMonthData(
    timeZone: TimeZone
): List<DashboardDomain> {

    val processedRides = this.mapNotNull { ride ->

        val dateString = ride.endRideDate
        if (dateString.isNullOrBlank()) {
            return@mapNotNull null
        }

        val instant = try {
            val timeInMillis = dateString.toLong()

            Instant.fromEpochMilliseconds(timeInMillis)
        } catch (e: Exception) {
            println("Error parsing date: $dateString for ride ID ${ride.ridesID}. Skipping. ${e.message}")
            return@mapNotNull null
        }

        val localDateTime = instant.toLocalDateTime(timeZone)
        val rideDateKey = RideDate(
            month = localDateTime.month.number,
            year = localDateTime.year
        )

        Pair(rideDateKey, ride)
    }

    return processedRides
        .groupBy { it.first }

        .map { (rideDateKey, rideDataPairs) ->
            DashboardDomain(
                monthYear = rideDateKey,
                perMonthData = rideDataPairs.map { it.second }
            )
        }
        .sortedWith(compareByDescending<DashboardDomain> { it.monthYear.year }
            .thenByDescending { it.monthYear.month })
}