//
//  JoinRideViewModel.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 17/10/25.
//

import Foundation
import SwiftUI
import Combine
import shared

struct JoinRideModel: Identifiable,Hashable {
    let id = UUID()
    let userId: String
    let rideId: String
    let title: String
    let organizer: String
    let description: String
    let route: String
    let distance: String
    let date: String
    let ridersCount: String
    let maxRiders: String
    let riderImage: String
    let contactNumber: String
    let startLat:Double
    let startLong:Double
    let endLat:Double
    let endLong:Double
    let rideJoined:Bool
    let participants: [ParticipantData]? 
}

@MainActor
final class JoinRideViewModel: ObservableObject {
    @Published var rides: [JoinRideModel] = []
    private var rideAPIService: RidesApIService
    private var rideRepository: RidesRepository
    @Published var filteredRides: [JoinRideModel] = []
    private var userAPIService: UserAPIService
    private var userRepository: UserRepository
    private var currentUserId = MBUserDefaults.userIdStatic ?? ""
    @Published var isRideLoading = false
    @Published var showRideAlreadyActivePopup = false
    @Published var searchQuery: String = "" {
        didSet {
            searchRides()
        }
    }
    
    init() {
        rideAPIService = RidesApiServiceImpl(client: KtorClient())
        rideRepository = RidesRepository(apiService: rideAPIService)
        
        userAPIService = UserAPIServiceImpl(client: KtorClient())
        userRepository = UserRepository(apiService: userAPIService)
    }
    
    func callToRider(contactNumber:String) {
        if #available(iOS 10.0, *) {
            UIApplication.shared.open(URL(string: "tel://\(contactNumber)")!)
        } else {
            print("App version older than iOS 10.0")
        }
    }
}

//MARK: - Join ride API integration -
extension JoinRideViewModel {
    // MARK: - Async getRides
    func getRides() async {
        do {
            self.isRideLoading = true
            let rideArray = try await getAllRidesAsync()

            let filteredRideArray = rideArray.filter { ride in
                guard let startEpoch = ride.startDate else { return false }
                let startDate = Date(timeIntervalSince1970: Double(truncating: startEpoch) / 1000)
                // 1. Ignore past rides
                guard startDate >= Calendar.current.startOfDay(for: Date()) else { return false }
                // 2. Determine current user role
                let isCreator = ride.createdBy == currentUserId
                let participantRecord = ride.participants.first { $0.userId == currentUserId }
                let isParticipant = participantRecord != nil
                // 3. Hide ended rides
                if isCreator, ride.rideStatus == 4 { return false }
                if isParticipant, participantRecord?.inviteStatus == 4 { return false }
                // 4. Show only creator or participant rides
                return isCreator || isParticipant
            }
            
            
            var joinRideModels: [JoinRideModel] = []
            
            for ride in filteredRideArray {
                guard let startEpoch = ride.startDate else { continue }
                let startDate = Date(timeIntervalSince1970: Double(truncating: startEpoch) / 1000)
                let dateString = self.formatDate(startDate)
                let participants = ride.participants
                
                // Fetch user name asynchronously
                let userName = await self.getAllUsers(createdBy: ride.createdBy ?? "")
                let joinedCount = ride.participants.filter { $0.inviteStatus == 3 }.count
                let rideJoinedStatus = ride.participants.first(where: {$0.userId == (MBUserDefaults.userIdStatic ?? "") })?.inviteStatus == 3
                if startDate >= Calendar.current.startOfDay(for: Date()) {
                    let model = JoinRideModel(
                        userId:ride.createdBy ?? "",
                        rideId: ride.ridesID ?? "",
                        title: ride.rideTitle ?? "",
                        organizer: userName?.0 ?? "",
                        description: ride.description_ ?? "",
                        route: "\(ride.startLocation ?? "") - \(ride.endLocation ?? "")",
                        distance: "\(Int(ride.rideDistance)) km",
                        date: dateString,
                        ridersCount: "\(joinedCount)",
                        maxRiders: "\(ride.participants.count)",
                        riderImage: "rider_avatar",
                        contactNumber: userName?.1 ?? "",
                        startLat: ride.startLatitude,
                        startLong: ride.startLongitude,
                        endLat: ride.endLatitude,
                        endLong: ride.endLongitude,
                        rideJoined: rideJoinedStatus,
                        participants: participants
                        
                    )
                    joinRideModels.append(model)
                }
            }
            
            await MainActor.run {
                self.rides = joinRideModels
                self.filteredRides = joinRideModels
                self.isRideLoading = false
            }
            
        } catch {
            self.isRideLoading = false
            print("Failed to fetch rides: \(error.localizedDescription)")
        }
    }
    
    private func getAllRidesAsync() async throws -> [RidesData] {
        try await withCheckedThrowingContinuation { continuation in
            rideRepository.getAllRide { result, error in
                if let success = result as? APIResultSuccess<AnyObject>,
                   let rideArray = success.data as? [RidesData] {
                    continuation.resume(returning: rideArray)
                } else if let error = error {
                    continuation.resume(throwing: error)
                } else {
                    continuation.resume(returning: [])
                }
            }
        }
    }
    
    func getAllUsers(createdBy: String) async -> (String, String)? {
        await withCheckedContinuation { continuation in
            userRepository.getAllUsers { result, error in
                if let success = result as? APIResultSuccess<AnyObject>,
                   let domainList = success.data as? [UserDomain],
                   let matchedUser = domainList.first(where: { $0.uid == createdBy }) {

                    let userName = matchedUser.name
                    let contactNumber = matchedUser.contactNumber
                    continuation.resume(returning: (userName, contactNumber))
                } else {
                    continuation.resume(returning: nil)
                }
            }
        }
    }

    func changeRideInviteStatus(rideId:String, userId:String, inviteStatus:Int32) {
        rideRepository.changeRideInviteStatus(
            rideID: rideId,
            currentUid: userId,
            inviteStatus: inviteStatus
        ) { result, error in
            if let error = error {
                print("Failed to update joined riders count: \(error.localizedDescription)")
            } else {
                print("Joined riders count updated successfully.")
            }
        }
    }
    
    func formatDate(_ date: Date) -> String { let formatter = DateFormatter()
        formatter.dateFormat = "E, MMM dd yyyy - hh:mm a"
        formatter.locale = Locale(identifier: "en_US_POSIX")
        return formatter.string(from: date) }
    
    func searchRides() {
        let query = searchQuery.lowercased().trimmingCharacters(in: .whitespacesAndNewlines)
        guard !query.isEmpty else {
            filteredRides = rides
            return
        }
        
        filteredRides = rides.filter { ride in
            ride.title.lowercased().contains(query) ||
            ride.organizer.lowercased().contains(query) ||
            ride.route.lowercased().contains(query) ||
            ride.description.lowercased().contains(query) ||
            ride.route.lowercased().contains(query)
        }
    }
    // MARK: - Join Flow
        func handleJoin(for ride: JoinRideModel) async -> JoinRideModel? {
            if ride.rideJoined { return ride }

            if let active = await getUserActiveRide() {
                if active.ridesID != ride.rideId {
                    showRideAlreadyActivePopup = true
                    return nil
                }
            }

            await joinRide(ride)
            return ride
        }

        func joinRide(_ ride: JoinRideModel) async {
            let uid = currentUserId

            if ride.userId == uid {
                print("\(ride.rideId) -> rideStatus = 3")
                // rideRepository.changeRideStatus(...)
            } else {
                changeRideInviteStatus(rideId: ride.rideId, userId: uid, inviteStatus: 3)
            }
        }

        func endActiveRide() async {
            guard let active = await getUserActiveRide() else { return }
            let uid = currentUserId

            if active.createdBy == uid {
                print("\(active.ridesID ?? "") -> rideStatus = 4")
            } else {
                changeRideInviteStatus(
                    rideId: active.ridesID ?? "",
                    userId: uid,
                    inviteStatus: 4
                )
            }

            showRideAlreadyActivePopup = false
        }

        func getUserActiveRide() async -> RidesData? {
            do {
                let result = try await rideRepository.getAllRide()
                guard
                    let success = result as? APIResultSuccess<AnyObject>,
                    let rides = success.data as? [RidesData]
                else { return nil }

                let uid = currentUserId
                let today = Calendar.current.startOfDay(for: Date())

                return rides.first { ride in
                    guard let epoch = ride.startDate else { return false }
                    let date = Date(timeIntervalSince1970: Double(truncating: epoch) / 1000)
                    if date < today { return false }

                    let isCreator = ride.createdBy == uid && ride.rideStatus == 3
                    let isParticipant = ride.participants.contains {
                        $0.userId == uid && $0.inviteStatus == 3
                    }

                    return isCreator || isParticipant
                }

            } catch {
                return nil
            }
        }


}
