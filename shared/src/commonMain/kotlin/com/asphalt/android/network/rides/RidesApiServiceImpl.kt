package com.asphalt.android.network.rides

import com.asphalt.android.constants.APIConstants.END_RIDE_SUMMARY_URL
import com.asphalt.android.constants.APIConstants.PARTICIPANTS_URL
import com.asphalt.android.constants.APIConstants.RIDES_URL
import com.asphalt.android.constants.APIConstants.ONGOING_RIDE_URL
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.Dashboard.DashboardDTO
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.connectedride.ConnectedRideRoot
import com.asphalt.android.model.connectedride.FirebasePushResponse
import com.asphalt.android.model.rides.CreateRideRoot
import com.asphalt.android.model.rides.UserInvites
import com.asphalt.android.network.BaseAPIService
import com.asphalt.android.network.KtorClient
import io.ktor.client.call.body

class RidesApiServiceImpl(client: KtorClient) : BaseAPIService(client), RidesApIService {
    override suspend fun createRide(createRideRoot: CreateRideRoot): APIResult<GenericResponse> {
        return safeApiCall {
            post(createRideRoot, RIDES_URL).body()
        }
    }

    override suspend fun getAllRide() : APIResult<Map<String, CreateRideRoot>> {
       return safeApiCall {
            get(url = RIDES_URL).body()
        }
    }

    override suspend fun changeRideInviteStatus(
        rideID: String,
        userID: String,
        userInvites: UserInvites
    ): APIResult<Unit> {
        return safeApiCall {
            patch(userInvites, "$RIDES_URL/$rideID$PARTICIPANTS_URL/$userID").body()
        }
    }

    override suspend fun getSingleRide(rideID: String): APIResult<CreateRideRoot> {
        return safeApiCall {
            get(url = RIDES_URL+"/${rideID}").body()
        }
    }

    override suspend fun joinRide(joinRide: ConnectedRideRoot): APIResult<FirebasePushResponse> {
      return safeApiCall {
          post(joinRide, "$ONGOING_RIDE_URL/${joinRide.rideID}").body()
      }
    }

    override suspend fun rejoinRide(
        connectedRide: ConnectedRideRoot,
        ongoingRideId: String
    ): APIResult<FirebasePushResponse> {
        return safeApiCall {
            patch(connectedRide, "$ONGOING_RIDE_URL/${connectedRide.rideID}/$ongoingRideId").body()
        }
    }

    override suspend fun getJoinedRides(rideId: String): APIResult<Map<String, ConnectedRideRoot>> {
        return safeApiCall {
            get(url = "$ONGOING_RIDE_URL/$rideId").body()
        }
    }

    override suspend fun endRide(rideId: String, rideJoinedId: String): APIResult<Unit> {
        return safeApiCall {
            delete("$ONGOING_RIDE_URL/$rideId/$rideJoinedId").body()
        }
    }

    override suspend fun endRideSummary(userID: String,endRide: DashboardDTO): APIResult<FirebasePushResponse> {
        return safeApiCall {
            post(endRide, "$END_RIDE_SUMMARY_URL/$userID").body()
        }
    }

    override suspend fun getRideSummary(userID: String): APIResult<Map<String, DashboardDTO>> {
        return safeApiCall {
            get(url = "$END_RIDE_SUMMARY_URL/$userID").body()
        }
    }
}
