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

enum RideViewAction: String {
    case checkResponse = "Check Response"
    case viewDetails = "View Details"
    case shareExperience = "Share Experience"
    case decline = "Decline"
}

enum RideStatus: String {
    case upcoming = "Upcoming"
    case history = "History"
    case invites = "Invites"
    case queue = "Queue"
}

enum RideAction {
    case upcoming
    case history
    case invites
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
    @Published var rideStatus: [RideAction]  = [.upcoming, .history, .invites]
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
                print("Error")
                return
            }
            
            let currentUserID = MBUserDefaults.userIdStatic ?? ""
            var upcoming: [RideModel] = []
            var invites: [RideModel] = []
            var history: [RideModel] = []
            
            for ride in rideArray {
                guard let startEpoch = ride.startDate else { continue }

                let startDate = Date(timeIntervalSince1970: Double(startEpoch.int64Value) / 1000)
                let dateString = formatDate(startDate)
                let participantCount = ride.participants.count
                
                let isParticipant = ride.participants.contains { $0.userId == currentUserID }
                let myInviteStatus = ride.participants.first(where: { $0.userId == currentUserID })?.inviteStatus

                var mapped = RideModel(
                    id: ride.ridesID ?? "",
                    title: ride.rideTitle ?? "",
                    routeStart: ride.startLocation ?? "",
                    routeEnd: ride.endLocation ?? "",
                    status: .upcoming,
                    rideViewAction: .checkResponse,
                    rideAction: .upcoming,
                    date: dateString,
                    riderCount: participantCount
                )

                // Upcoming
                if (ride.createdBy == currentUserID) && startDate >= Calendar.current.startOfDay(for: Date()) {
                    if ride.participants.count > 0 {
                        mapped.status = .queue
                        mapped.rideViewAction = .checkResponse
                    }
                    else{
                        mapped.status = .upcoming
                        mapped.rideViewAction = .viewDetails
                    }
                    mapped.rideAction = .upcoming
                    upcoming.append(mapped)
                }
                
                // Invites
                if ride.createdBy != currentUserID, isParticipant, let inviteStatus = myInviteStatus, inviteStatus == 0 {
                    mapped.status = .invites
                    mapped.rideAction = .invites
                    mapped.rideViewAction = .decline
                    invites.append(mapped)
                }

                // History
                if isParticipant, startDate < Calendar.current.startOfDay(for: Date()) || ride.rideType == "completed" {
                    mapped.status = .history
                    mapped.rideAction = .history
                    mapped.rideViewAction = .shareExperience
                    history.append(mapped)
                }
            }
            await MainActor.run {
                self.upcomingRides = upcoming
                self.inviteRides = invites
                self.historyRides = history
                self.isRideLoading = false
            }
        } catch {
            print("Error :", error)
        }
    }

    func formatDate(_ date: Date) -> String { let formatter = DateFormatter()
        formatter.dateFormat = "E, MMM dd yyyy - hh:mm a"
        formatter.locale = Locale(identifier: "en_US_POSIX")
        return formatter.string(from: date) }
}
