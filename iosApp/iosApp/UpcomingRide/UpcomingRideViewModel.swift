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
    let createdBy: String
    var hasPhotos: Bool = false
    let startDate: Date
    let participantAcceptedCount: Int
}


class UpcomingRideViewModel: ObservableObject {
    @Published var rides: [RideModel] = []
    @Published var rideStatus: [RideAction]  = [.upcoming, .history, .invities]
    @Published var rideViewActions: [RideViewAction]  = [.checkResponse, .viewDetails, .shareExperience]
    @Published var upcomingRides: [RideModel] = []
    @Published var historyRides: [RideModel] = []
    @Published var inviteRides: [RideModel] = []
    @Published var usersById: [String: String] = [:]
    @Published var isRideLoading = false
    @Published var userName: String = ""
    @Published var selectedTab: RideAction = .upcoming
    @Published var hasPhotos: Bool = false
    private var rideAPIService: RidesApIService
    private var rideRepository: RidesRepository
    private let userRepo: UserRepository
    
    init() {
        rideAPIService = RidesApiServiceImpl(client: KtorClient())
        rideRepository = RidesRepository(apiService: rideAPIService)
        let userApiService = UserAPIServiceImpl(client: KtorClient())
        self.userRepo = UserRepository(apiService: userApiService)
    }
    
    // MARK: - Get all rides
    @MainActor
    func fetchAllRides() async {
        do {
            isRideLoading = true
            
            let result = try await rideRepository.getAllRide()
            
            guard let success = result as? APIResultSuccess<AnyObject>,
                  let rideArray = success.data as? [RidesData] else {
                print("Error parsing rides")
                isRideLoading = false
                return
            }
            
            let currentUserID = MBUserDefaults.userIdStatic ?? ""
            
            var upcoming: [RideModel] = []
            var history: [RideModel] = []
            var invites: [RideModel] = []
            
            for ride in rideArray {
                guard let startEpoch = ride.startDate else { continue }
                
                let startDate = Date(timeIntervalSince1970: Double(startEpoch.int64Value) / 1000)
                let now = Date()
                let dateString = formatDate(startDate)
                let participantCount = ride.participants.count
                let isParticipant = ride.participants.contains { $0.userId == currentUserID }
                let participantAcceptedCount = ride.participants.filter { $0.inviteStatus == 1 }.count
                let myInviteStatus = ride.participants.first(where: { $0.userId == currentUserID })?.inviteStatus
            
                
                var rideAction: RideAction
                var rideViewAction: RideViewAction
                var rideStatus: RideStatus
                
                if startDate <= now {
                    // If user was invited but never accepted, skip adding to history
                    if isParticipant, let inviteStatus = myInviteStatus, inviteStatus != 1 {
                        continue
                    }
                    
                    rideAction = .history
                    rideStatus = .complete
                    rideViewAction = .shareExperience
                }
                
                else {
                    if ride.createdBy == currentUserID {
                        //upcoming Ride that I have created
                        if startDate >= now {
                            rideStatus = participantCount > 0 ? .queue : .upcoming
                            rideViewAction = participantCount > 0 ? .checkResponse : .viewDetails
                            rideAction = .upcoming
                        } else {
                            rideStatus = .complete
                            rideAction = .history
                            rideViewAction = .shareExperience
                        }
                        //Invites
                    } else if isParticipant, let inviteStatus = myInviteStatus, inviteStatus == 0 {
                        if startDate >= now {
                            rideAction = .invities
                            rideStatus = .invite
                            rideViewAction = .decline
                        } else {
                            continue 
                        }
                        
                        //accepted invites
                    } else if isParticipant, let inviteStatus = myInviteStatus, inviteStatus == 1 {
                        rideAction = .upcoming
                        rideStatus = .upcoming
                        rideViewAction = .viewDetails
                        //history
                    } else if isParticipant {
                        rideAction = .history
                        rideStatus = .complete
                        rideViewAction = .shareExperience
                    } else {
                        continue
                    }
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
                    riderCount: participantCount,
                    createdBy: ride.createdBy ?? "",
                    startDate: startDate,
                    participantAcceptedCount: participantAcceptedCount
                )
                
                switch rideAction {
                case .upcoming: upcoming.append(mapped)
                case .history: history.append(mapped)
                case .invities: invites.append(mapped)
                }
            }
            
            upcoming.sort { $0.startDate < $1.startDate }
            upcomingRides = upcoming
            history.sort { $0.startDate > $1.startDate }
            historyRides = history
            invites.sort { $0.startDate < $1.startDate }
            inviteRides = invites
            
            self.rides = upcomingRides + historyRides + inviteRides
            
            isRideLoading = false
            
        } catch {
            print("Error fetching rides:", error)
            isRideLoading = false
        }
    }
    
    
    func formatDate(_ date: Date) -> String { let formatter = DateFormatter()
        formatter.dateFormat = "E, MMM dd yyyy - hh:mm a"
        formatter.locale = Locale(identifier: "en_US_POSIX")
        return formatter.string(from: date)
    }
    
    // MARK: - Change invite status- API
    
    @MainActor
    func changeRideInviteStatus(rideId: String, accepted: Bool) async {
        do {
            isRideLoading = true
            
            let result = try await rideRepository.changeRideInviteStatus(
                rideID: rideId,
                currentUid: MBUserDefaults.userIdStatic ?? "",
                inviteStatus: accepted ? 1 : 2
            )
            
            guard result is APIResultSuccess<GenericResponse> else {
                return
            }
            
            print("Invite Responded successfully!")
            
            if let index = inviteRides.firstIndex(where: { $0.id == rideId }) {
                var ride = inviteRides[index]
                inviteRides.remove(at: index)
                
                if accepted {
                    ride.rideAction = .upcoming
                    ride.status = .upcoming
                    ride.rideViewAction = .viewDetails
                    upcomingRides.append(ride)
                    
                    selectedTab = .upcoming
                }
            }
            
            Task {
                try? await Task.sleep(nanoseconds: 700_000_000)
                await fetchAllRides()
            }
            
            
        } catch {
            print("Error:", error)
            isRideLoading = false
        }
    }
    
    
    
    // MARK: - Fetch Users
    @MainActor
    func fetchAllUsers() async {
        do {
            let result = try await userRepo.getAllUsers()
            
            if let success = result as? APIResultSuccess<AnyObject>,
               let users = success.data as? [UserDomain] {
                var dict: [String: String] = [:]
                for user in users {
                    dict[user.uid] = user.name
                }
                self.usersById = dict
            } else {
                print("Unexpected user data type")
            }
        } catch {
            print("Error fetching users:", error)
        }
    }
    
    
}
