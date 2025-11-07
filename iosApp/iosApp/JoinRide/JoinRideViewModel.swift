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

            let filteredRideArray = rideArray.filter {
                $0.createdBy == MBUserDefaults.userIdStatic ||
                ($0.participants.first(where: { $0.userId == MBUserDefaults.userIdStatic })?.inviteStatus == 1)
            }
            
            var joinRideModels: [JoinRideModel] = []
            
            for ride in filteredRideArray {
                guard let startEpoch = ride.startDate else { continue }
                let startDate = Date(timeIntervalSince1970: Double(truncating: startEpoch) / 1000)
                let dateString = self.formatDate(startDate)
                
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
                        endLong: ride.endLongitude, rideJoined: rideJoinedStatus
                        
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

    func getRideInvites(rideId:String, userId:String, inviteStatus:Int32) {
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
}
