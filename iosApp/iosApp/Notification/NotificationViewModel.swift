//
//  NotificationViewModel.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 08/10/25.
//

import SwiftUI
import shared

struct NotificationMessage: Identifiable {
    var id: UUID = UUID()
    var title: String
    var message: String
    var time: String
    var image: Image?
}

@MainActor
class NotificationViewModel: ObservableObject {
    @Published var notifications: [NotificationMessage] = []
    @Published var title: String = ""

    func fetchNotifications() {
        let stored = NotificationStore.shared.all()
        if stored.isEmpty {
            notifications = [
                NotificationMessage(
                    title: AppStrings.Notification.rideReminder.localized,
                    message: "Your ride \"Morning city ride\" starts in 30 minutes",
                    time: "5 minutes ago",
                    image: AppIcon.Notification.rideReminder
                ),
                NotificationMessage(
                    title: AppStrings.Notification.newRiderJoined.localized,
                    message: "Sooraj joined \"Weekend Adventure\" ride",
                    time: "1 hour ago",
                    image: AppIcon.Notification.newRider
                )
            ]
            return
        }

        notifications = stored.map { item in
            NotificationMessage(
                id: item.id,
                title: item.title,
                message: item.message,
                time: Self.relativeTime(from: item.timestamp),
                image: icon(for: item.type)
            )
        }
    }

    private func icon(for type: AppNotificationType) -> Image? {
        switch type {
        case .rideReminder:
            return AppIcon.Notification.rideReminder
        case .newRiderJoined:
            return AppIcon.Notification.newRider
        case .rideUpdate:
            return AppIcon.Notification.rideReminder
        case .message:
            return AppIcon.Notification.newRider
        }
    }

    private static func relativeTime(from date: Date) -> String {
        let seconds = Int(Date().timeIntervalSince(date))
        if seconds < 60 { return "just now" }
        let minutes = seconds / 60
        if minutes < 60 { return "\(minutes) minute\(minutes == 1 ? "" : "s") ago" }
        let hours = minutes / 60
        if hours < 24 { return "\(hours) hour\(hours == 1 ? "" : "s") ago" }
        let days = hours / 24
        return "\(days) day\(days == 1 ? "" : "s") ago"
    }
}
