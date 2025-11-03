package mappers

import com.asphalt.android.helpers.UserDataHelper
import com.asphalt.android.model.UserDomain
import com.asphalt.android.model.rides.RideInvitesDomain
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.utils.Utils
import com.asphalt.dashboard.data.DashboardRideInviteUIModel

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

