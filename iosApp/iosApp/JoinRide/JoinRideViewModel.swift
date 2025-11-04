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

struct JoinRideModel: Identifiable {
    let id = UUID()
    let title: String
    let organizer: String
    let description: String
    let route: String
    let distance: String
    let date: String
    let ridersCount: String
    let maxRiders: String
    let riderImage: String
}

@MainActor
final class JoinRideViewModel: ObservableObject {
    @Published var rides: [JoinRideModel] = []
    private var rideAPIService: RidesApIService
    private var rideRepository: RidesRepository
    @Published var filteredRides: [JoinRideModel] = []
    @Published var searchQuery: String = "" {
           didSet {
               searchRides()
           }
       }
    
    init() {
        rideAPIService = RidesApiServiceImpl(client: KtorClient())
        rideRepository = RidesRepository(apiService: rideAPIService)
    }
    
    private func loadSampleRides() {
        rides = []
    }
}

//MARK: - Join ride API integration -
extension JoinRideViewModel {
    func getRides() {
        rideRepository.getAllRide { result, error in
            guard let success = result as? APIResultSuccess<AnyObject>,
                  let rideArray = success.data as? [RidesData] else {
                print(" Failed to fetch rides: \(error?.localizedDescription ?? "Unknown error")")
                return
            }
            let joinRideModels: [JoinRideModel] = rideArray.compactMap { ride in
                guard let startEpoch = ride.startDate else { return nil }
                let startDate = Date(timeIntervalSince1970: Double(truncating: startEpoch) / 1000)
                let dateString = self.formatDate(startDate)
              
                let acceptedCount = ride.participants.filter { $0.inviteStatus == 1 }.count
                
                return JoinRideModel(
                    title: ride.rideTitle ?? "",
                    organizer: ride.createdBy ?? "",
                    description: ride.description_ ?? "",
                    route: "\(ride.startLocation ?? "") - \(ride.endLocation ?? "")",
                    distance:"\(Int(ride.rideDistance)) km",
                    date: dateString,
                    ridersCount: "0",
                    maxRiders: "\(acceptedCount)",
                    riderImage: "rider_avatar"
                )
            }
            
            DispatchQueue.main.async {
                self.rides = joinRideModels
                self.filteredRides = joinRideModels
            }
        }
    }
    
    func updateRideJoinStatus(rideId: String, userID: String, joinCount: Int32) {
//        rideRepository.updateRides(rideID: rideId, userID: userID, joinCount: joinCount, completionHandler: { result, error in
//            if let error = error {
//                print("Failed to update ride status: \(error.localizedDescription)")
//            } else {
//                print("Rides updated.")
//            }
//        })
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
