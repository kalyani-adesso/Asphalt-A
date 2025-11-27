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
    case pending = "Pending"
    case declined = "Declined"
    case confirmed = "Confirmed"
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

struct RideDetailsModel: Identifiable,Hashable {
    var id = UUID()
    let userId:String
    let userName:String
    let status:String
    let pendingCount:Int
    let declinedCount:Int
    let confirmedCount:Int
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
    @Published var upcomingInvitesRide: [RideModel] = []
    private var rideAPIService: RidesApIService
    private var rideRepository: RidesRepository
    private let userRepo: UserRepository
    @Published  var participants: [Participant] = []
    @Published var rideDetails: [RideDetailsModel] = []
    @Published var joinRideModel = JoinRideModel(userId: "", rideId: "", title: "", organizer: "", description: "", route: "", distance: "", date: "", ridersCount: "", maxRiders: "", riderImage: "", contactNumber: "", startLat: 0.0, startLong: 0.0, endLat: 0.0, endLong: 0.0, rideJoined: false, participants: [])
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
            let now = Date()
            
            var upcoming: [RideModel] = []
            var history: [RideModel] = []
            var invites: [RideModel] = []
            
            for ride in rideArray {
                guard let startEpoch = ride.startDate else { continue }
                
                let startDate = Date(timeIntervalSince1970: Double(startEpoch.int64Value) / 1000)
                if startDate.addingTimeInterval(60) < now { continue }
                let dateString = formatDate(startDate)
                let participantsExcludingCreator = ride.participants.filter { $0.userId != ride.createdBy }
                let participantCount = participantsExcludingCreator.count
                let isParticipant = ride.participants.contains { $0.userId == currentUserID }
                let participantAcceptedCount = ride.participants.filter { $0.inviteStatus == 1 }.count
                let myInviteStatus = ride.participants.first(where: { $0.userId == currentUserID })?.inviteStatus
                
                
                var rideAction: RideAction
                var rideViewAction: RideViewAction
                var rideStatus: RideStatus
                
                
                // MARK: - Creator Logic
                if ride.createdBy == currentUserID {
                    
                    // Creator ride status 3 = joined = upcoming
                    if ride.rideStatus == 3 {
                        rideAction = .upcoming
                        rideStatus = ride.participants.allSatisfy { [1,3].contains($0.inviteStatus) } ? .upcoming : .queue
                        rideViewAction = ride.participants.allSatisfy { [1,3].contains($0.inviteStatus) } ? .viewDetails : .checkResponse
                    }
                    
                    // Creator ride status 4 = ended = history
                    else if ride.rideStatus == 4 {
                        rideAction = .history
                        rideStatus = .complete
                        rideViewAction = .shareExperience
                    }
                    
                    // Default creator: future ride
                    else {
                        rideAction = .upcoming
                        rideStatus = ride.participants.allSatisfy { [1,3].contains($0.inviteStatus) } ? .upcoming : .queue
                        rideViewAction = ride.participants.allSatisfy { [1,3].contains($0.inviteStatus) } ? .viewDetails : .checkResponse
                    }
                }
                
                // MARK: - Participant Logic
                else if let myStatus = myInviteStatus {
                    
                    switch myStatus {
                    case 0:
                        rideAction = .invities
                        rideStatus = .invite
                        rideViewAction = .decline
                        
                    case 1, 3 :
                        rideAction = .upcoming
                        rideStatus = ride.participants.allSatisfy { [1,3].contains($0.inviteStatus) } ? .upcoming : .queue
                        rideViewAction = ride.participants.allSatisfy { [1,3].contains($0.inviteStatus) } ? .viewDetails : .checkResponse
                        
                    case 2:
                        continue  // hide
                        
                    case 4:
                        rideAction = .history
                        rideStatus = .complete
                        rideViewAction = .shareExperience
                        
                    default:
                        continue
                    }
                }
                
                // Not creator and not participant
                else {
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
    
    func getAllUsers(createdBy: String) async -> (String,String)? {
        
        await withCheckedContinuation { continuation in
            userRepo.getAllUsers { result, error in
                if let success = result as? APIResultSuccess<AnyObject>,
                   let domainList = success.data as? [UserDomain],
                   let matchedUser = domainList.first(where: { $0.uid == createdBy }) {
                    
                    let userName = matchedUser.name
                    let contactNumber = matchedUser.contactNumber
                    continuation.resume(returning:( userName,contactNumber))
                } else {
                    continuation.resume(returning: nil)
                }
            }
        }
    }
    
    @MainActor
    func getSingleRide(rideId: String) async {
        self.isRideLoading = true

        rideRepository.getSingeRide(rideID: rideId) { result, error in

            guard
                let success = result as? APIResultSuccess<AnyObject>,
                let ride = success.data as? RidesData,
                let startEpoch = ride.startDate
            else {
                self.isRideLoading = false
                return
            }

            let currentUserId = MBUserDefaults.userIdStatic ?? ""
            let startDate = Date(timeIntervalSince1970: Double(truncating: startEpoch) / 1000)
            let dateString = self.formatDate(startDate)

            // -------------------------
            // MARK: Build Final Participants List
            // -------------------------
            var finalParticipants = ride.participants

            // Add creator if not in the list
            if !finalParticipants.contains(where: { $0.userId == ride.createdBy }) {
                finalParticipants.append(
                    ParticipantData(userId: (ride.createdBy ?? MBUserDefaults.userIdStatic) ?? "", inviteStatus: 3)
                )
            }

            // Count statuses
            let pendingCount = finalParticipants.filter { $0.inviteStatus == 0 && $0.userId != currentUserId }.count
            let declinedCount = finalParticipants.filter { $0.inviteStatus == 2 && $0.userId != currentUserId }.count
            let confirmedCount = finalParticipants.filter { [1,3].contains($0.inviteStatus) && $0.userId != currentUserId }.count

            // Joined or not
            let rideJoinedStatus = finalParticipants.contains { $0.userId == currentUserId && $0.inviteStatus == 3 }

            Task {
                // Fetch creator details
                let creatorDetails = await self.getAllUsers(createdBy: ride.createdBy ?? "")
                let creatorName = creatorDetails?.0 ?? ""
                let creatorPhone = creatorDetails?.1 ?? ""

                // -------------------------
                // MARK: Create Ride Model
                // -------------------------
                let model = JoinRideModel(
                    userId: ride.createdBy ?? "",
                    rideId: ride.ridesID ?? "",
                    title: ride.rideTitle ?? "",
                    organizer: creatorName,
                    description: ride.description_ ?? "",
                    route: "\(ride.startLocation ?? "") - \(ride.endLocation ?? "")",
                    distance: "\(Int(ride.rideDistance)) km",
                    date: dateString,
                    ridersCount: "\(confirmedCount)",
                    maxRiders: "\(finalParticipants.count)",
                    riderImage: "rider_avatar",
                    contactNumber: creatorPhone,
                    startLat: ride.startLatitude,
                    startLong: ride.startLongitude,
                    endLat: ride.endLatitude,
                    endLong: ride.endLongitude,
                    rideJoined: rideJoinedStatus,
                    participants: finalParticipants
                )

                // -------------------------
                // MARK: Prepare RideDetails
                // -------------------------
                let rideDetails = await finalParticipants.asyncMap { participant in
                    let userInfo = await self.getAllUsers(createdBy: participant.userId)
                    let name = userInfo?.0 ?? ""

                    let status: String = {
                        if participant.userId == ride.createdBy { return "Ride Creator" }
                        switch participant.inviteStatus {
                        case 0: return "waiting for response"
                        case 1, 3: return "confirmed"
                        case 2: return "declined"
                        case 4: return "completed"
                        default: return "unknown"
                        }
                    }()

                    return RideDetailsModel(
                        userId: participant.userId,
                        userName: name,
                        status: status,
                        pendingCount: pendingCount,
                        declinedCount: declinedCount,
                        confirmedCount: confirmedCount
                    )
                }

                // Sort creator first
                let sortedDetails = rideDetails.sorted { a, b in
                    if a.userId == ride.createdBy { return true }
                    if b.userId == ride.createdBy { return false }
                    if a.userId == currentUserId { return true }
                    if b.userId == currentUserId { return false }
                    return false
                }


                // -------------------------
                // MARK: Update UI
                // -------------------------
                await MainActor.run {
                    self.joinRideModel = model
                    self.rideDetails = sortedDetails
                    self.isRideLoading = false
                }
            }
        }
    }


    @MainActor
    func getInvites() async {
        do {
            let result = try await rideRepository.getRideInvites(userID: MBUserDefaults.userIdStatic ?? "")
            if let success = result as? APIResultSuccess<AnyObject>,
               let invites = success.data as? [RideInvitesDomain] {
                let mapped: [RideModel] = invites.compactMap { domain in

                    let millis = domain.startDateTime ?? 0
                    let startDate = Date(timeIntervalSince1970: Double(truncating: millis) / 1000)

                    guard startDate >= Date() else { return nil }

                    return RideModel(
                        id: domain.rideID,
                        title: "Ride Invite",
                        routeStart: domain.startLocation,
                        routeEnd: domain.destination,
                        status: .invite,
                        rideViewAction: .decline,
                        rideAction: .invities,
                        date: formatDate(startDate),
                        riderCount: domain.acceptedParticipants.count,
                        createdBy: domain.inviter,
                        startDate: startDate,
                        participantAcceptedCount: domain.acceptedParticipants.count
                    )
                }
                self.upcomingInvitesRide = mapped
            }
        } catch {
            print("Error fetching users:", error)
        }
    }
}

// Extension to help with async mapping (if not already available)
extension Sequence {
    func asyncMap<T>(_ transform: (Element) async throws -> T) async rethrows -> [T] {
        var values = [T]()
        for element in self {
            try await values.append(transform(element))
        }
        return values
    }
}
extension Date {
    var startOfDay: Date {
        Calendar.current.startOfDay(for: self)
    }
}
