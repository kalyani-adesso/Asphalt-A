//
//  UpcomingRideViewModel.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 16/10/25.
//

import Foundation
import SwiftUI
import Combine

enum RideAction: String {
    case upcoming = "Upcoming"
    case history = "History"
    case invities = "Invities"
}

enum RideViewAction: String {
    case checkResponse = "Check Response"
    case viewDetails = "View Details"
    case shareExperience = "Share Experience"
    case decline = "Decline"
}

enum RideStatus: String {
    case upcoming = "Upcoming"
    case queue = "Queue"
    case complete = "Completed"
    case invite = "Invite"
}

struct RideModel: Identifiable,Hashable {
    let id = UUID()
    var title: String
    let routeStart: String
    let routeEnd: String
    let status: RideStatus
    let rideViewAction: RideViewAction
    let rideAction: RideAction
    let date: String
    let riderCount: Int
}

class UpcomingRideViewModel: ObservableObject {
    @Published var rides: [RideModel] = []
    @Published var rideStatus: [RideAction]  = [.upcoming, .history, .invities]
    @Published var rideViewActions: [RideViewAction]  = [.checkResponse, .viewDetails, .shareExperience]
    init() {
        loadRides()
    }
    
    func loadRides() {
        rides = [
            RideModel(title: "Weekend Coast Ride", routeStart: "Kochi", routeEnd: "Kanyakumari", status: .upcoming, rideViewAction: .viewDetails, rideAction: .upcoming, date:formattedRideDate(from:"2025-10-21T09:00:00Z" ), riderCount: 3),
            RideModel(title: "Mountain Ride", routeStart: "Kochi", routeEnd: "Idukki", status: .queue, rideViewAction: .checkResponse, rideAction: .upcoming, date: formattedRideDate(from:"2025-10-21T09:00:00Z"), riderCount: 3),
            RideModel(title: "Weekend Coast Ride", routeStart: "Kochi", routeEnd: "Kanyakumari", status: .complete, rideViewAction: .shareExperience, rideAction: .history, date: formattedRideDate(from:"2025-09-21T09:00:00Z"), riderCount: 3),
            RideModel(title: "Invite from Suraj Rajan", routeStart: "Kochi", routeEnd: "Kanyakumari", status: .invite, rideViewAction: .decline, rideAction: .invities, date: formattedRideDate(from:"2025-10-17T09:00:00Z"), riderCount: 3)
        ]
    }
    
    func formattedRideDate(from isoString: String) -> String {
        let isoFormatter = ISO8601DateFormatter()
        guard let date = isoFormatter.date(from: isoString) else { return "" }
        
        let displayFormatter = DateFormatter()
        displayFormatter.dateFormat = "E, MMM dd - hh:mm a"
        displayFormatter.locale = Locale(identifier: "en_US_POSIX")
        return displayFormatter.string(from: date)
    }
}
