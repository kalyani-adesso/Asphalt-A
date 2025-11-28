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
    let currentLat:Double
    let currentLong:Double
    let rideId:String
    let receiverId:String
}

struct MessageUIModel: Identifiable {
    let id: String
    let senderId: String
    let senderName: String
    let message: String
    let timestamp: String
    let isCurrentUser: Bool
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

struct ConnectedRideMessage: Identifiable {
    let id = UUID()
    let message: String
    let senderName: String
    let date: Date
}

final class ConnectedRideViewModel: ObservableObject {
    @Published var rideCompleteModel: [RideCompleteModel] = []
    @Published var activeRider: [Rider] = [Rider(name: "Aromal", speed: 55, status: .active, timeSinceUpdate: "Tracking", contactNumber: "",currentLat: 0.0,currentLong: 0.0,rideId: "",receiverId: "")]
    @Published var groupRiders: [Rider] = []
    @Published var isGroupNavigationActive: Bool = true
    @Published var ongoingRideId = ""
    private var rideAPIService: RidesApIService
    private var rideRepository: RidesRepository
    private var userAPIService: UserAPIService
    private var userRepository: UserRepository
    var ongoingRideTimer:Timer?
    private var previousRidersDict: [String: Rider] = [:]
    @Published var showPopup: Bool = false
    @Published var popupTitle: String = ""
    @Published var messageIndex:Int = 0
    @Published var chatMessages: [MessageUIModel] = []
    var rider : Rider {
        groupRiders[messageIndex]
    }
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
    
    func  getRideCompleteDetails(duration:String,distance:String,riders:String) {
        rideCompleteModel =   [
            RideCompleteModel(iconName: AppIcon.ConnectedRide.rideDuration, value: duration, label: "Duration"),
            RideCompleteModel(iconName: AppIcon.ConnectedRide.rideDistance, value: distance, label: "KM"),
            RideCompleteModel(iconName: AppIcon.ConnectedRide.riders, value: riders, label: "Riders")
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
        
        let status = getRideStatus()

        let connectedRideRoot = ConnectedRideRoot(
            rideID: rideId,
            userID: userId,
            currentLat: KotlinDouble(value: currentLat),
            currentLong: KotlinDouble(value: currentLong),
            speedInKph: KotlinDouble(value: speed), status: getRideStatus().rawValue,
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
        let status = getRideStatus()
        let connectedRideRoot = ConnectedRideRoot(
            rideID: rideId,
            userID: userId,
            currentLat: KotlinDouble(value: currentLat),
            currentLong: KotlinDouble(value: currentLong),
            speedInKph: KotlinDouble(value: speed), status: getRideStatus().rawValue ,
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
                            var ridersDict: [String: Rider] = [:]  // Dictionary to track unique riders by userID
                            let filteredRides = ongoingRides.filter { $0.userID != MBUserDefaults.userIdStatic }
                            
                            for ongoingRide in filteredRides {
                                _ = self.getRideStatus()
                                let timeSinceUpdate = self.formatTime(from: ongoingRide.dateTime)
                                let userDetails = await self.getAllUsers(createdBy: ongoingRide.userID)
                                let rider = Rider(
                                    name: userDetails?.0 ?? "",
                                    speed: Int(ongoingRide.speedInKph),
                                    status: RiderStatus(rawValue: ongoingRide.status) ?? .stopped,
                                    timeSinceUpdate: timeSinceUpdate,
                                    contactNumber: userDetails?.1 ?? "",
                                    currentLat: ongoingRide.currentLat,
                                    currentLong: ongoingRide.currentLong,
                                    rideId: ongoingRide.rideID,
                                    receiverId: ongoingRide.userID
                                    
                                )
                                
                                // Update or add to dictionary (ensures uniqueness by userID)
                                ridersDict[ongoingRide.userID] = rider
                            }
                            
                            // Convert dictionary values to array
                            let updatedRiders = Array(ridersDict.values)
                            
                            DispatchQueue.main.async {
                                if self.groupRiders.count > 0 {
                                    self.groupRiders.removeAll()
                                }
                                
                                self.detectRiderChanges(newRiders: updatedRiders)
                                
                                self.groupRiders = updatedRiders
                                
                                self.previousRidersDict = Dictionary(uniqueKeysWithValues: updatedRiders.map { ($0.contactNumber, $0) })

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
    
    func detectRiderChanges(newRiders: [Rider]) {
        let newDict = Dictionary(uniqueKeysWithValues: newRiders.map { ($0.contactNumber, $0) })
        let oldDict = previousRidersDict

        //Detect NEW riders joining
        for (id, newRider) in newDict where oldDict[id] == nil {
            showPopup(title: "\(newRider.name) joined the ride")
        }

        //Detect riders who left / ended ride
        for (id, oldRider) in oldDict where newDict[id] == nil {
            showPopup(title: "\(oldRider.name) ended the ride")
        }

        //Detect STATUS change
        for (id, newRider) in newDict {
            if let old = oldDict[id], old.status != newRider.status {
                showPopup(title: "\(newRider.name) has been \(newRider.status) for a while")
            }
        }
    }

    func showPopup(title: String) {
        self.popupTitle = title
        self.showPopup = true
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
            self.showPopup = false
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
    
    func rateYourRide(ratings:Int,comments:String) {
        rideRepository.rateYourRide(rideId: ongoingRideId, userId: MBUserDefaults.userIdStatic ?? "", stars: Int32(ratings), comments: comments) { result, error in
            if let error = error {
                print("Error while rating your ride \(error)")
            } else {
                print("Sucesss!")
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

    
    func stopOngoingRideTimer() {
        ongoingRideTimer?.invalidate()
        ongoingRideTimer = nil
    }
    
    func endRideSummary(ride: JoinRideModel, userID: String) {
        
        let isParticipant = ride.participants?.contains { $0.userId == userID } ?? false
        
        let dto = DashboardDTO(
              rideID: ride.rideId,
              rideDistance: KotlinDouble(value: Double(ride.distance.replacingOccurrences(of: " km", with: "")) ?? 0),
              isGroupRide: KotlinBoolean(bool: (Int(ride.maxRiders) ?? 0) > 0),
              startLocation: ride.route.split(separator: "-").first?.trimmingCharacters(in: .whitespaces) ?? "",
              endLocation: ride.route.split(separator: "-").last?.trimmingCharacters(in: .whitespaces) ?? "",
              isOrganiserGroupRide: KotlinBoolean(bool: userID == ride.userId),
              isParticipantGroupRide: KotlinBoolean(bool: isParticipant),
              endRideDate: KotlinLong(value: Int64(Date().timeIntervalSince1970 * 1000))
        )
        
        rideRepository.endRideSummary(userID: userID, endRide: dto){ result, error in
            if let result = result as? APIResultSuccess<ConnectedRideDTO> {
                _ = result.data
               
            } else if let error = error {
                print("Error joining ride:", error.localizedDescription)
            } else {
                print("Result is nil")
            }
        }
    }
    
    func sendMessage(senderName:String,receiverName:String,senderId:String,receiverId:String,message:String,rideId:String) {
        let dateTimeMillis = Int64(Date().timeIntervalSince1970 * 1000)
        let messageRoot = MessageRoot(senderID: senderId, senderName: senderName, receiverID: receiverId, receiverName: receiverName, message: message, onGoingRideID: rideId, timeStamp: KotlinLong(value: dateTimeMillis), isRideOnGoing: KotlinBoolean(bool: true) )
        rideRepository.sendMessage(message:messageRoot , completionHandler: {result, error in
            if let error = error {
                print("Error joining ride:", error.localizedDescription)
            } else {
                print("Message sent successfully")
            }
        })
    }

    func receiveMessage(rideId: String) async {
        do {
            let flow = try await MessageImpl().receiveMessage(rideId: rideId)

            try await flow.collect(
                collector: ReceiveMessageCollector(
                    onValue: { kmpMessages in
                        Task { @MainActor in
                            self.chatMessages = kmpMessages.filter({$0.receiverID == MBUserDefaults.userIdStatic && $0.isRideOnGoing == true }).map { item in
                                MessageUIModel(
                                    id: item.id,
                                    senderId: item.senderID,
                                    senderName: item.senderName,
                                    message: item.message,
                                    timestamp: self.formatTime(from: item.timeStamp),
                                    isCurrentUser: item.senderID == MBUserDefaults.userIdStatic
                                )
                            }
                        }
                    },
                    onError: { error in
                        print("Receive Message Error:", error.localizedDescription)
                    }
                )
            )

        } catch {
            print("Outer Receive Message Error:", error.localizedDescription)
        }
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

class ReceiveMessageCollector: Kotlinx_coroutines_coreFlowCollector {
    let onValue: ([MessageDTO]) -> Void
    let onError: (Error) -> Void
    
    init(onValue: @escaping ([MessageDTO]) -> Void, onError: @escaping (Error) -> Void) {
        self.onValue = onValue
        self.onError = onError
    }
    
    func emit(value: Any?, completionHandler: @escaping ((any Error)?) -> Void) {
        if let apiResult = value as? APIResultSuccess<AnyObject>,
           let messages = apiResult.data as? [MessageDTO] {
            onValue(messages)
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
