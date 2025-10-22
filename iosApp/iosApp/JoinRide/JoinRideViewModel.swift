//
//  JoinRideViewModel.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 17/10/25.
//

import Foundation
import SwiftUI
import Combine

struct JoinRideModel: Identifiable {
    let id = UUID()
    let title: String
    let organizer: String
    let description: String
    let route: String
    let distance: String
    let date: String
    let time: String
    let ridersCount: String
    let maxRiders: String
    let riderImage: String
}

@MainActor
final class JoinRideViewModel: ObservableObject {
    @Published var rides: [JoinRideModel] = []
    
    init() {
        loadSampleRides()
    }
    
    private func loadSampleRides() {
        rides = [
            JoinRideModel(
                title: "Weekend Coast Ride",
                organizer: "Sooraj",
                description: "Join us for a beautiful sunrise ride along the coastal highway",
                route: "Kochi - Kanyakumari",
                distance: "280km",
                date: "Sun, Oct 21",
                time: "09:00 AM",
                ridersCount: "3",
                maxRiders: "8",
                riderImage: "rider_avatar"
            ),
            JoinRideModel(
                title: "Weekend Coast Ride",
                organizer: "Sooraj",
                description: "Join us for a beautiful sunrise ride along the coastal highway",
                route: "Kochi - Kanyakumari",
                distance: "280km",
                date: "Sun, Oct 21",
                time: "09:00 AM",
                ridersCount: "3",
                maxRiders: "8",
                riderImage: "rider_avatar"
            )
        ]
    }
}
