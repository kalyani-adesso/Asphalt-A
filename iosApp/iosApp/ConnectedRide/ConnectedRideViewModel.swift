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
import Combine

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
    let contactNumber: String
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
    @Published var activeRider: [Rider] = [Rider(name: "Aromal", speed: 55, status: .active, timeSinceUpdate: "Tracking", contactNumber: "")]
    @Published var groupRiders: [Rider] = []
    @Published var isGroupNavigationActive: Bool = true
    private var ongoingRideId = ""
    private var rideAPIService: RidesApIService
    private var rideRepository: RidesRepository
    private var userAPIService: UserAPIService
    private var userRepository: UserRepository
    private var ongoingRideTimer: Timer?
    
    var lastLat: Double?
    var lastLong: Double?
    var lastSpeed: Double = 0.0
    var lastUpdateTime: TimeInterval = Date().timeIntervalSince1970
    var lastMovementTime: TimeInterval = Date().timeIntervalSince1970
    
    init () {
        rideAPIService = RidesApiServiceImpl(client: KtorClient())
        rideRepository = RidesRepository(apiService: rideAPIService)
        self.ongoingRideId = MBUserDefaults.isRideJoinedID ?? ""
        userAPIService = UserAPIServiceImpl(client: KtorClient())
        userRepository = UserRepository(apiService: userAPIService)
        loadData()
        startOngoingRideTimer()
    }
    deinit {
         stopOngoingRideTimer()
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
    
    func updateOrganizerStatus(rideId:String) {
        rideRepository.updateOrganizerStatus(rideId: rideId, rideStatus: 4, completionHandler: { status, error in
            if let error = error {
                print("Failed to update ride status: \(error.localizedDescription)")
            } else {
                print("Successfully updated ride status")
            }
        })
    }
    
    @MainActor
    func getOnGoingRides(rideId: String) async {
        do {
            let flow = try await ConnectedRideImpl().getOngoingRides(rideId: rideId)

            try await flow.collect(
                collector: ConnectedRideCollector(
                    onValue: { ongoingRides in
                        
                        Task { 
                            var updatedRiders: [Rider] = []
                            let filteredRides = ongoingRides.filter { $0.userID != MBUserDefaults.userIdStatic }
                            
                            for ongoingRide in filteredRides {
                                let status = self.getRideStatus()
                                let timeSinceUpdate = self.formatTime(from: ongoingRide.dateTime)
                                let userDetails = await self.getAllUsers(createdBy: ongoingRide.userID)
                                let rider = Rider(
                                    name: userDetails?.0 ?? "",
                                    speed: Int(ongoingRide.speedInKph),
                                    status: status,
                                    timeSinceUpdate: timeSinceUpdate,
                                    contactNumber: userDetails?.1 ?? ""
                                )
                                updatedRiders.append(rider)
                            }
                            
                            DispatchQueue.main.async {
                                self.groupRiders = updatedRiders
                            }
                        }
                    },
                    onError: { error in
                        print("Error: \(error.localizedDescription)")
                    }
                ),
                completionHandler: { error in
                    if let error = error {
                        print("Collection failed: \(error.localizedDescription)")
                    } else {
                        print("Collection finished")
                    }
                }
            )

        } catch {
            print("Outer error: \(error.localizedDescription)")
        }
    }

    func getAllUsersName() async -> [String: (name: String, contact: String)] {
        await withCheckedContinuation { continuation in
            userRepository.getAllUsers { result, error in
                if let success = result as? APIResultSuccess<AnyObject>,
                   let domainList = success.data as? [UserDomain] {
                    
                    var usersDict: [String: (String, String)] = [:]
                    for user in domainList {
                        usersDict[user.uid] = (user.name, user.contactNumber)
                    }
                    continuation.resume(returning: usersDict)
                } else {
                    continuation.resume(returning: [:])
                }
            }
        }
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
        
    func onLocationUpdate(lat: Double?, long: Double?, speed: Double?) {
        let now = Date().timeIntervalSince1970
        
        guard let lat = lat, let long = long else { return }
        
        // Save update time (we ARE getting updates)
        lastUpdateTime = now
        
        // Detect movement (lat/long changed)
        if lastLat != lat || lastLong != long {
            lastMovementTime = now    // movement detected
        }
        
        lastLat = lat
        lastLong = long
        lastSpeed = speed ?? 0.0
    }
    
    func getRideStatus() -> RiderStatus {
        let now = Date().timeIntervalSince1970
        let timeSinceUpdate = now - lastUpdateTime
        let timeSinceMovement = now - lastMovementTime
        
        if timeSinceUpdate > 120 && timeSinceMovement > 120 && lastSpeed == 0 {
            return .stopped
        }
        
        // CONNECTED (movement + continuous updates)
        if timeSinceUpdate < 120 && lastSpeed > 0 {
            return .connected
        }
        
        // DELAYED (break in update < 2 min OR speed = 0)
        return .delayed
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
    func startOngoingRideTimer() {
        // Invalidate any existing timer
        ongoingRideTimer?.invalidate()
        // Schedule the timer to trigger every 15 minutes (900 seconds)
        ongoingRideTimer = Timer.scheduledTimer(withTimeInterval: 900, repeats: true) { [weak self] _ in
            guard let self = self else { return }
            
            Task {
                await self.getOnGoingRides(
                    rideId: self.ongoingRideId
                )
            }
        }
    }
    
    func stopOngoingRideTimer() {
        ongoingRideTimer?.invalidate()
        ongoingRideTimer = nil
    }
}

class ConnectedRideCollector: Kotlinx_coroutines_coreFlowCollector {
    let onValue: ([ConnectedRideDTO]) -> Void
    let onError: (Error) -> Void

    init(onValue: @escaping ([ConnectedRideDTO]) -> Void, onError: @escaping (Error) -> Void) {
        self.onValue = onValue
        self.onError = onError
    }

    func emit(value: Any?, completionHandler: @escaping (Error?) -> Void) {
        if let apiResult = value as? APIResultSuccess<AnyObject>,
           let rides = apiResult.data as? [ConnectedRideDTO] {
            onValue(rides)
            completionHandler(nil)
        } else if let apiError = value as? APIResultError {
            onError(apiError.exception as! any Error)
            completionHandler(nil)
        } else {
            onError(NSError(domain: "UnknownResult", code: 0, userInfo: nil))
            completionHandler(nil)
        }
    }
}
