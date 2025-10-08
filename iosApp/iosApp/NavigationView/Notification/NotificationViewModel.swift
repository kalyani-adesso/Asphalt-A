//
//  NotificationViewModel.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 08/10/25.
//

import SwiftUI
import Shared

struct NotificationMessage: Identifiable {
    var id: UUID = UUID()
    var title: String
    var message: String
    var time: String
    var image: Image?
}

class NotificationViewModel: ObservableObject {
    @Published var notifications: [NotificationMessage] = []
    @Published var title: String = ""

    func fetchNotifications() {
        notifications = [
            NotificationMessage(title: AppStrings.Notification.rideReminder.localized, message: "Your ride “ Morning city ride ‘’ starts in 30 minutes", time: "5 minutes ago", image: AppIcon.Notification.rideReminder),
            NotificationMessage(title: AppStrings.Notification.newRiderJoined.localized, message: "Sooraj joined to ‘’ Weekend Adventure’’ ride", time: "1 hour ago", image:AppIcon.Notification.newRider)
        ]
        
    }
}
