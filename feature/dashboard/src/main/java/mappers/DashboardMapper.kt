package mappers

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
import com.asphalt.dashboard.data.RideStatDataUIModel
import com.asphalt.dashboard.sealedclasses.RideGraphLegend
import com.asphalt.dashboard.sealedclasses.RideStatType

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
            RideStatDataUIModel(RideStatType.TotalKms, this?.totalDistance?.toInt() ?: 0),
            RideStatDataUIModel(RideStatType.Locations, this?.uniqueEndLocations ?: 0)
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

