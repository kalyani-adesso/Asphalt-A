//
//  ConnectedRideSessionVM.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 24/03/26.
//

import Foundation

@MainActor
final class ConnectedRideSessionVM {
    private let core: ConnectedRideViewModel

    init(core: ConnectedRideViewModel) {
        self.core = core
    }

    func joinRide(rideId: String, userId: String, currentLat: Double, currentLong: Double, speed: Double) {
        core.joinRide(rideId: rideId, userId: userId, currentLat: currentLat, currentLong: currentLong, speed: speed)
    }

    func sendHeartbeatIfNeeded(rideId: String, userId: String, currentLat: Double, currentLong: Double, speed: Double) {
        core.sendHeartbeatIfNeeded(rideId: rideId, userId: userId, currentLat: currentLat, currentLong: currentLong, speed: speed)
    }

    func endRide(rideId: String, completion: @escaping (Bool) -> Void) {
        core.endRide(rideId: rideId, completion: completion)
    }

    func endRideSummary(ride: JoinRideModel, userID: String, travelledDistanceKm: Double, completion: @escaping (Bool) -> Void) {
        core.endRideSummary(ride: ride, userID: userID, travelledDistanceKm: travelledDistanceKm, completion: completion)
    }

    func onLocationUpdate(lat: Double?, long: Double?, speed: Double?) {
        core.onLocationUpdate(lat: lat, long: long, speed: speed)
    }
}
