//
//  ConnectedRideViewModel.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 18/10/25.
//

import Foundation
import SwiftUI
import MapKit
import shared

struct RideCompleteModel: Identifiable {
    let id = UUID()
    let iconName: Image
    let value: String
    let label: String
}

// MARK: - Model Layer

enum RiderStatus: String {
    case active = "Active"
    case connected = "Connected"
    case delayed = "Delayed"
    case stopped = "Stopped"
}

enum TrackingStatus:String {
    case Live
    case Paused
}

struct Rider: Identifiable {
    let id = UUID()
    let name: String
    let speed: Int // Kph
    let status: RiderStatus
    let timeSinceUpdate: String
}

struct RideStop: Identifiable {
    let id = UUID()
    let name: String
    let coordinate: CLLocationCoordinate2D
}

enum MapType: String, CaseIterable, Hashable {
    case standard
    case hybrid
    case imagery
}

final class ConnectedRideViewModel: ObservableObject {
    @Published var rideCompleteModel: [RideCompleteModel] = []
    @Published var activeRider: [Rider] = [Rider(name: "Aromal", speed: 55, status: .active, timeSinceUpdate: "Tracking")]
    @Published var groupRiders: [Rider] = []
    @Published var isGroupNavigationActive: Bool = true
    private var ongoingRideId = ""
    private var rideAPIService: RidesApIService
    private var rideRepository: RidesRepository
    private var userAPIService: UserAPIService
    private var userRepository: UserRepository
    init () {
        rideAPIService = RidesApiServiceImpl(client: KtorClient())
        rideRepository = RidesRepository(apiService: rideAPIService)
        self.ongoingRideId = MBUserDefaults.isRideJoinedID ?? ""
        userAPIService = UserAPIServiceImpl(client: KtorClient())
        userRepository = UserRepository(apiService: userAPIService)
        loadData()
    }
    
    @Published var selectedType: MapType = .standard
    
    @available(iOS 17.0, *)
    var currentMapStyle: MapStyle {
        switch selectedType {
        case .standard: return .standard
        case .hybrid: return .hybrid
        case .imagery: return .imagery
        }
    }
    
    func loadData() {
        rideCompleteModel =   [
            RideCompleteModel(iconName: AppIcon.ConnectedRide.rideDuration, value: "09:30:23", label: "Duration"),
            RideCompleteModel(iconName: AppIcon.ConnectedRide.rideDistance, value: "78.5", label: "KM"),
            RideCompleteModel(iconName: AppIcon.ConnectedRide.riders, value: "3", label: "Riders")
        ]
    }
    func endRide() {
        print("Ride ended.")
        // Example of a state change that updates the view:
        self.groupRiders = []
        self.isGroupNavigationActive = false
        self.activeRider = []
    }
    
    /// Toggles the tracking status for the active rider.
    func toggleTracking() {
        // In a real app, this would send an API request
        //            let newSpeed = activeRider.speed > 0 ? 0 : 55
        //            let newStatus: RiderStatus = newSpeed > 0 ? .active : .stopped
        //
        //            print("Tracking toggled. New speed: \(newSpeed) Kph")
    }
    
    /// Sends an emergency SOS signal.
    func sendEmergencySOS() {
        // Logic for sending location/alert
        print("!!! EMERGENCY SOS SENT !!!")
    }
    
    /// Requests to share the user's live location with external contacts.
    func shareLocation() {
        // Logic for initiating location sharing
        print("Live location sharing initiated.")
    }
    
    /// Finds a specific rider by name (example of a helper method).
    func getRiderStatus(byName name: String) -> RiderStatus? {
        return groupRiders.first(where: { $0.name == name })?.status
    }
}

//MARK: Connected ride view model
extension ConnectedRideViewModel {
    func joinRide(rideId: String, userId: String, currentLat: Double, currentLong: Double, speed: Double)  {
        let dateTimeMillis = Int64(Date().timeIntervalSince1970 * 1000)
        
        let connectedRideRoot = ConnectedRideRoot(
            rideID: rideId,
            userID: userId,
            currentLat: KotlinDouble(value: currentLat),
            currentLong: KotlinDouble(value: currentLong),
            speedInKph: KotlinDouble(value: speed), status: "Stopped",
            dateTime: KotlinLong(value: dateTimeMillis),
            isRejoined: KotlinBoolean(value: false)
        )
        
        rideRepository.joinRide(joinRide: connectedRideRoot) { result, error in
            if let result = result as? APIResultSuccess<ConnectedRideDTO> {
                let ride = result.data
                //TODO: Check here -
                MBUserDefaults.isRideJoinedID = ride?.rideJoinedID ?? ""
                self.ongoingRideId = MBUserDefaults.isRideJoinedID ?? ""
            } else if let error = error {
                print("Error joining ride:", error.localizedDescription)
            } else {
                print("Result is nil or uninitialized")
            }
        }
    }
    
    func reJoinRide(rideId: String, userId: String, currentLat: Double, currentLong: Double, speed: Double) {
        let dateTimeMillis = Int64(Date().timeIntervalSince1970 * 1000)
        
        let connectedRideRoot = ConnectedRideRoot(
            rideID: rideId,
            userID: userId,
            currentLat: KotlinDouble(value: currentLat),
            currentLong: KotlinDouble(value: currentLong),
            speedInKph: KotlinDouble(value: speed), status: "Stopped",
            dateTime: KotlinLong(value: dateTimeMillis),
            isRejoined: KotlinBoolean(value: true)
        )
        
        rideRepository.reJoinRide(rejoinRide:connectedRideRoot,ongoingRideId: ongoingRideId){ result, error in
            if let error = error {
                print("Failed to join ride: \(error.localizedDescription)")
            } else {
                print("Successfully joined ride")
            }
        }
    }
    
    func getOnGoingRides(rideId:String,userId:String) async {
        await rideRepository.getOngoingRides(rideId: rideId, completionHandler: { result, error in
            if let result = result as? APIResultSuccess<AnyObject>,
               let ongoingRides = result.data as? [ConnectedRideDTO] {
                Task {
                    let userName = await self.getAllUsers(createdBy: ongoingRides.first(where: {$0.userID == userId })?.userID ?? "")
                    self.groupRiders = ongoingRides.filter{$0.userID != MBUserDefaults.userIdStatic}.map { ride in
                        Rider(
                            name: ride.userID,
                            speed: Int(ride.speedInKph),
                            status: self.getStatus(from: ride),
                            timeSinceUpdate: self.formatTime(from: ride.dateTime)
                        )
                    }
                }
            }
        })
    }
    
    func getAllUsers(createdBy: String) async  -> (String, String)? {
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
    
    func endRide(rideId: String) {
        let rideJoinedId = MBUserDefaults.isRideJoinedID ?? ""
        rideRepository.endRide(rideId: rideId, rideJoinedId:rideJoinedId) { result, error in
            if let error = error {
                print("Failed to end ride: \(error.localizedDescription)")
            } else {
                print("Successfully ended ride")
            }
        }
    }
    
    func getStatus(from ride: ConnectedRideDTO) -> RiderStatus {
        if ride.isRejoined {
            return.delayed
        } else if ride.speedInKph > 0 {
            return .connected
        } else {
            return .stopped
        }
    }

    func formatTime(from timestamp: Int64) -> String {
        let date = Date(timeIntervalSince1970: TimeInterval(timestamp) / 1000)
        let diff = Int(Date().timeIntervalSince(date))
        
        if diff < 60 {
            return "\(diff)s ago"
        } else if diff < 3600 {
            return "\(diff / 60)m ago"
        } else {
            return "\(diff / 3600)h ago"
        }
    }
}
