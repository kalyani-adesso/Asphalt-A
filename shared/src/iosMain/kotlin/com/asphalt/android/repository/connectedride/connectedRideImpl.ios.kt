package com.asphalt.android.repository.connectedride

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.connectedride.ConnectedRideDTO
import com.asphalt.android.model.connectedride.ConnectedRideRoot

import cocoapods.FirebaseDatabase.FIRDatabase
import cocoapods.FirebaseDatabase.FIRDataSnapshot
import cocoapods.FirebaseDatabase.FIRDataEventType
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import platform.Foundation.NSDictionary
import platform.Foundation.NSNumber
import platform.Foundation.allObjects
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.channels.awaitClose


actual class ConnectedRideImpl {

    @OptIn(ExperimentalForeignApi::class)
   suspend actual fun getOngoingRides(rideId: String): Flow<APIResult<List<ConnectedRideDTO>>> = callbackFlow {

        val dbRef = FIRDatabase.database().reference()
            .child("ongoing_ride")
            .child(rideId)

        val listener = dbRef.observeEventType(
            FIRDataEventType.FIRDataEventTypeValue,
            withBlock = { snapshot ->

                if (snapshot == null || !snapshot.exists()) {
                    trySend(APIResult.Success(emptyList()))
                    return@observeEventType
                }

                val dtoList = mutableListOf<ConnectedRideDTO>()

                val childrenArray = snapshot.children.allObjects as? List<FIRDataSnapshot> ?: emptyList()

                for (child in childrenArray) {
                    val joinedId = child.key ?: continue
                    val dict = child.value as? NSDictionary ?: continue
                    val root = dict.toConnectedRideRoot()

                    dtoList.add(
                        ConnectedRideDTO(
                            rideJoinedID = joinedId,
                            rideID = root.rideID ?: "",
                            userID = root.userID ?: "",
                            currentLat = root.currentLat ?: 0.0,
                            currentLong = root.currentLong ?: 0.0,
                            speedInKph = root.speedInKph ?: 0.0,
                            status = root.status ?: "",
                            dateTime = root.dateTime ?: 0L,
                            isRejoined = root.isRejoined ?: false
                        )
                    )
                }

                trySend(APIResult.Success(dtoList))
            },
            withCancelBlock = { error ->
                trySend(APIResult.Error(Exception(error?.localizedDescription ?: "Firebase error")))
            }
        )

        awaitClose {
            dbRef.removeObserverWithHandle(listener)
        }
    }
}

fun NSDictionary.toConnectedRideRoot(): ConnectedRideRoot {
    return ConnectedRideRoot(
        rideID = this.objectForKey("rideID") as? String,
        userID = this.objectForKey("userID") as? String,
        currentLat = (this.objectForKey("currentLat") as? NSNumber)?.doubleValue,
        currentLong = (this.objectForKey("currentLong") as? NSNumber)?.doubleValue,
        speedInKph = (this.objectForKey("speedInKph") as? NSNumber)?.doubleValue,
        status = this.objectForKey("status") as? String,
        dateTime = (this.objectForKey("dateTime") as? NSNumber)?.longValue,
        isRejoined = this.objectForKey("isRejoined") as? Boolean
    )
}
