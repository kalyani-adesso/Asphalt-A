//
//  ConnectedRideMessagingVM.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 24/03/26.
//

import Foundation

@MainActor
final class ConnectedRideMessagingVM {
    private let core: ConnectedRideViewModel

    init(core: ConnectedRideViewModel) {
        self.core = core
    }

    func sendMessage(senderName: String, receiverName: String, senderId: String, receiverId: String, message: String, rideId: String) {
        core.sendMessage(
            senderName: senderName,
            receiverName: receiverName,
            senderId: senderId,
            receiverId: receiverId,
            message: message,
            rideId: rideId
        )
    }

    func receiveMessage(rideId: String) async {
        await core.receiveMessage(rideId: rideId)
    }
}
