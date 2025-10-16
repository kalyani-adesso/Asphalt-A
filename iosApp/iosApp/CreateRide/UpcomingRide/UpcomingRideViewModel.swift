//
//  UpcomingRideViewModel.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 16/10/25.
//

import Foundation
import SwiftUI
import Combine

enum RideStatus: String {
    case upcoming = "Upcoming"
    case queue = "Queue"
    case completed = "Completed"
}

struct RideModel: Identifiable {
    let id = UUID()
    let title: String
    let routeStart: String
    let routeEnd: String
    let status: RideStatus
    let date: Date
    let riderCount: Int
}

// Sample data array
class RideViewModel: ObservableObject {
    @Published var rides: [RideModel] = []
    
    init() {
        loadRides()
    }
    
    func loadRides() {
        let formatter = ISO8601DateFormatter()
        rides = [
            RideModel(title: "Weekend Coast Ride", routeStart: "Kochi", routeEnd: "Kanyakumari", status: .upcoming, date: formatter.date(from: "2025-10-21T09:00:00Z") ?? Date(), riderCount: 3),
            RideModel(title: "Mountain Ride", routeStart: "Kochi", routeEnd: "Idukki", status: .queue, date: formatter.date(from: "2025-10-21T09:00:00Z") ?? Date(), riderCount: 3),
            RideModel(title: "Weekend Coast Ride", routeStart: "Kochi", routeEnd: "Kanyakumari", status: .completed, date: formatter.date(from: "2025-09-21T09:00:00Z") ?? Date(), riderCount: 3)
        ]
    }
}
