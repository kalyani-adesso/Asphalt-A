//
//  UpcomingRideViewModel.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 16/10/25.
//

import Foundation
import SwiftUI
import Combine
import shared

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
    let id : String
    var title: String
    let routeStart: String
    let routeEnd: String
    var status: RideStatus
    var rideViewAction: RideViewAction
    var rideAction: RideAction
    let date: String
    let riderCount: Int
}


class UpcomingRideViewModel: ObservableObject {
    @Published var rides: [RideModel] = []
    @Published var rideStatus: [RideAction]  = [.upcoming, .history, .invities]
    @Published var rideViewActions: [RideViewAction]  = [.checkResponse, .viewDetails, .shareExperience]
    @Published var upcomingRides: [RideModel] = []
    @Published var historyRides: [RideModel] = []
    @Published var inviteRides: [RideModel] = []
    @Published var isRideLoading = false
    private var rideAPIService: RidesApIService
    private var rideRepository: RidesRepository
    
    
    init() {
        
        rideAPIService = RidesApiServiceImpl(client: KtorClient())
        rideRepository = RidesRepository(apiService: rideAPIService)
    }
    
    // MARK: - Get all rides
    @MainActor
    func fetchAllRides() async {
        do {
            self.isRideLoading = true
            let result = try await rideRepository.getAllRide()
            
            guard let success = result as? APIResultSuccess<AnyObject>,
                  let rideArray = success.data as? [RidesData] else {
                print("Error parsing rides")
                return
            }
            
            let currentUserID = MBUserDefaults.userIdStatic ?? ""
            var filteredRides: [RideModel] = []
            
            for ride in rideArray {
                guard let startEpoch = ride.startDate else { continue }
                let startDate = Date(timeIntervalSince1970: Double(startEpoch.int64Value) / 1000)
                let dateString = formatDate(startDate)
                let participantCount = ride.participants.count
                let isParticipant = ride.participants.contains { $0.userId == currentUserID }
                let myInviteStatus = ride.participants.first(where: { $0.userId == currentUserID })?.inviteStatus
                
                var rideAction: RideAction
                var rideViewAction: RideViewAction
                var rideStatus: RideStatus
                
                if ride.createdBy == currentUserID {
                    // Upcoming rides I created
                    if startDate >= Calendar.current.startOfDay(for: Date()) {
                        rideStatus = participantCount > 0 ? .queue : .upcoming
                        rideViewAction = participantCount > 0 ? .checkResponse : .viewDetails
                        rideAction = .upcoming
                    } else {
                        // History of rides I created
                        rideStatus = .complete
                        rideAction = .history
                        rideViewAction = .shareExperience
                    }
                } else if isParticipant, let inviteStatus = myInviteStatus, inviteStatus == 0 {
                    // invites
                    rideAction = .invities
                    rideStatus = .invite
                    rideViewAction = .decline
                } else if isParticipant {
                    // History of rides I participated in
                    rideAction = .history
                    rideStatus = .complete
                    rideViewAction = .shareExperience
                } else {
                    // Skip rides that I have not created
                    continue
                }
                
                let mapped = RideModel(
                    id: ride.ridesID ?? UUID().uuidString,
                    title: ride.rideTitle ?? "",
                    routeStart: ride.startLocation ?? "",
                    routeEnd: ride.endLocation ?? "",
                    status: rideStatus,
                    rideViewAction: rideViewAction,
                    rideAction: rideAction,
                    date: dateString,
                    riderCount: participantCount
                )
                
                filteredRides.append(mapped)
            }
         
            await MainActor.run {
                self.rides = filteredRides
                self.isRideLoading = false
            }
            print("rides \(self.rides)")
        } catch {
            print("Error fetching rides:", error)
            self.isRideLoading = false
        }
    }


    func formatDate(_ date: Date) -> String { let formatter = DateFormatter()
        formatter.dateFormat = "E, MMM dd yyyy - hh:mm a"
        formatter.locale = Locale(identifier: "en_US_POSIX")
        return formatter.string(from: date) }
    
    
}
