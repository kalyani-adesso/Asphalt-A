package com.asphalt.dashboard.mappers

import com.asphalt.android.helpers.UserDataHelper
import com.asphalt.android.model.UserDomain
import com.asphalt.android.model.dashboard.DashboardDomain
import com.asphalt.android.model.dashboard.PerMonthRideDataDomain
import com.asphalt.android.model.rides.RideInvitesDomain
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.utils.Utils
import com.asphalt.dashboard.data.AggregatedRideMetrics
import com.asphalt.dashboard.data.DashboardRideInviteUIModel
import com.asphalt.dashboard.data.DashboardSummaryUI
import com.asphalt.dashboard.data.JourneyDataUIModel
import com.asphalt.dashboard.data.PlacesVisitedGraphData
import com.asphalt.dashboard.data.PlacesVisitedGraphUIModel
import com.asphalt.dashboard.data.RideStatDataUIModel
import com.asphalt.dashboard.sealedclasses.RideGraphLegend
import com.asphalt.dashboard.sealedclasses.RideStatType
import java.text.DateFormatSymbols
import java.util.Calendar

fun List<RideInvitesDomain>.toDashBoardInvites(
    userList: List<UserDomain>,
): List<DashboardRideInviteUIModel> {
    return with(this) {
        map { data ->
            data.toDashBoardInviteUIModel(userList)
        }
    }
}

fun RideInvitesDomain.toDashBoardInviteUIModel(
    userList: List<UserDomain>,
): DashboardRideInviteUIModel {
    return with(this) {
        DashboardRideInviteUIModel(
            rideID = rideID,
            inviterName = inviter.run {
                UserDataHelper.getUserDataFromCurrentList(
                    userList,
                    this
                )
            }?.name ?: Constants.UNKNOWN_USER,
            inviterProfilePicUrl = UserDataHelper.getUserDataFromCurrentList(
                userList,
                inviter
            )?.profilePic.orEmpty(),
            startPoint = startLocation,
            destination = destination,
            dateTime = Utils.formatDateMillisToISO(startDateTime),
            inviteesProfilePicUrls = acceptedParticipants.map {
                val profilePicUrl: String? =
                    UserDataHelper.getUserDataFromCurrentList(userList, it)?.profilePic
                profilePicUrl.orEmpty()

            },
            rideTitle = rideTitle,
            isOrganiser = isOrganiser
        )
    }
}

fun List<DashboardDomain>.toDashboardSummaryUI(): List<DashboardSummaryUI> {

    return this.map { domain ->
        val rides = domain.perMonthData
        val aggregateMetrics = rides.aggregateMetrics()
        val totalDistance = aggregateMetrics.totalDistance
        val totalParticipantGroupRides = aggregateMetrics.totalParticipantGroupRides
        val totalOrganiserGroupRides = aggregateMetrics.totalOrganiserGroupRides
        val uniqueEndLocations = aggregateMetrics.uniqueEndLocations

        DashboardSummaryUI(
            monthYear = domain.monthYear,
            totalRides = rides.size,
            totalDistance = totalDistance,
            totalParticipantGroupRides = totalParticipantGroupRides,
            totalOrganiserGroupRides = totalOrganiserGroupRides,
            uniqueEndLocations = uniqueEndLocations
        )
    }
        .sortedWith(compareByDescending<DashboardSummaryUI> { it.monthYear.year }
            .thenByDescending { it.monthYear.month })
}

fun DashboardSummaryUI?.toRideStatUiModel(): List<RideStatDataUIModel> {
    return with(this) {
        listOf(
            RideStatDataUIModel(
                RideStatType.TotalRides, this?.totalRides ?: 0
            ),
            RideStatDataUIModel(RideStatType.Locations, this?.uniqueEndLocations ?: 0),

            RideStatDataUIModel(RideStatType.TotalKms, this?.totalDistance?.toInt() ?: 0),
        )
    }
}

fun List<DashboardDomain>.toJourneyDataUIModel(): List<JourneyDataUIModel> {

    val allRides = this.flatMap { it.perMonthData }
    val aggregateMetrics = allRides.aggregateMetrics()
    val totalRides = aggregateMetrics.totalRides
    val totalParticipantGroupRides = aggregateMetrics.totalParticipantGroupRides
    val totalOrganiserGroupRides = aggregateMetrics.totalOrganiserGroupRides
    val uniqueEndLocations = aggregateMetrics.uniqueEndLocations
    return listOf(
        JourneyDataUIModel(RideGraphLegend.TotalRides, totalRides.toFloat()),
        JourneyDataUIModel(
            RideGraphLegend.PlacesExplored,
            uniqueEndLocations.toFloat()
        ),
        JourneyDataUIModel(RideGraphLegend.RideGroups, totalParticipantGroupRides.toFloat()),
        JourneyDataUIModel(RideGraphLegend.RideInvites, totalOrganiserGroupRides.toFloat()),
    )
}

fun List<PerMonthRideDataDomain>.aggregateMetrics(): AggregatedRideMetrics {
    val rides = this

    val totalDistance = rides.sumOf { it.rideDistance ?: 0.0 }
    val totalParticipantGroupRides = rides.count { it.isParticipantGroupRide == true }
    val totalOrganiserGroupRides = rides.count { it.isOrganiserGroupRide == true }

    val uniqueEndLocations = rides
        .mapNotNull { it.endLocation }
        .filter { it.isNotBlank() }
        .distinct()
        .count()

    return AggregatedRideMetrics(
        totalRides = rides.size,
        totalDistance = totalDistance,
        totalParticipantGroupRides = totalParticipantGroupRides,
        totalOrganiserGroupRides = totalOrganiserGroupRides,
        uniqueEndLocations = uniqueEndLocations
    )
}

fun List<DashboardDomain>.fetchPlaceVisitedGraphData(
    startMonthYear: Pair<Int, Int>,
    endMonthYear: Pair<Int, Int>
): List<PlacesVisitedGraphData> {

    val (startMonth, startYear) = startMonthYear
    val (endMonth, endYear) = endMonthYear

    val startValue = startYear * 12 + startMonth
    val endValue = endYear * 12 + endMonth

    val existingDataMap = this
        .filter { domain ->
            val domainValue = domain.monthYear.year * 12 + domain.monthYear.month
            domainValue in startValue..endValue
        }
        .associateBy { domain -> domain.monthYear.year * 12 + domain.monthYear.month }

    val finalGraphData = mutableListOf<PlacesVisitedGraphData>()

    val calendar = Calendar.getInstance()
    calendar.set(startYear, startMonth - 1, 1)

    val endCalendar = Calendar.getInstance()
    endCalendar.set(endYear, endMonth - 1, 1)

    while (calendar.timeInMillis <= endCalendar.timeInMillis) {
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)
        val currentValue = currentYear * 12 + currentMonth

        val domain = existingDataMap[currentValue]

        val placesCount = domain?.perMonthData?.aggregateMetrics()?.uniqueEndLocations ?: 0

        finalGraphData.add(
            PlacesVisitedGraphData(
                month = currentMonth,
                year = currentYear,
                count = placesCount
            )
        )

        calendar.add(Calendar.MONTH, 1)
    }

    return finalGraphData
}

fun List<PlacesVisitedGraphData>.toPlaceVisitedGraphUIModel(): List<PlacesVisitedGraphUIModel> {
    return this.map { item ->
        val monthName = DateFormatSymbols().shortMonths[item.month - 1]
        PlacesVisitedGraphUIModel(
            month = "$monthName",
            count = item.count
        )
    }
}

