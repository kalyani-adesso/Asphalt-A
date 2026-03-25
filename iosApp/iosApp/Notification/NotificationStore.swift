//
//  NotificationStore.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 24/03/26.
//

import Foundation

enum AppNotificationType: String, Codable {
    case rideReminder
    case newRiderJoined
    case rideUpdate
    case message
}

struct StoredNotification: Codable, Identifiable {
    let id: UUID
    let title: String
    let message: String
    let timestamp: Date
    let type: AppNotificationType

    init(id: UUID = UUID(), title: String, message: String, timestamp: Date = Date(), type: AppNotificationType) {
        self.id = id
        self.title = title
        self.message = message
        self.timestamp = timestamp
        self.type = type
    }
}

@MainActor
final class NotificationStore {
    static let shared = NotificationStore()

    private let key = "com.adesso.rider.club.notifications.v1"
    private let maxCount = 100

    private init() {}

    func all() -> [StoredNotification] {
        guard
            let data = UserDefaults.standard.data(forKey: key),
            let decoded = try? JSONDecoder().decode([StoredNotification].self, from: data)
        else {
            return []
        }
        return decoded.sorted(by: { $0.timestamp > $1.timestamp })
    }

    func add(title: String, message: String, type: AppNotificationType) {
        var current = all()
        current.insert(StoredNotification(title: title, message: message, type: type), at: 0)
        if current.count > maxCount {
            current = Array(current.prefix(maxCount))
        }
        if let data = try? JSONEncoder().encode(current) {
            UserDefaults.standard.set(data, forKey: key)
        }
    }

    func unreadCount() -> Int {
        all().count
    }
}
