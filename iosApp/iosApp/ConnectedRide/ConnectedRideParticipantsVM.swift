//
//  ConnectedRideParticipantsVM.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 24/03/26.
//

import Foundation

@MainActor
final class ConnectedRideParticipantsVM {
    private let core: ConnectedRideViewModel

    init(core: ConnectedRideViewModel) {
        self.core = core
    }

    func getOnGoingRides(rideId: String) async {
        await core.getOnGoingRides(rideId: rideId)
    }

    func formatTime(from timestamp: Int64) -> String {
        core.formatTime(from: timestamp)
    }

    func sendEmergencySOS() {
        core.sendEmergencySOS()
    }

    func shareLocation() {
        core.shareLocation()
    }
}
