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
    case viewDetails = "View Details"
    case viewPhotos = "View Photos"
    case addPhotos = "Add Photos"
    case share = "Share"
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
    let startTime: String?
    let endTime: String?
    let ratings: Int?
    let participants: [ParticipantData]?
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

@MainActor
class UpcomingRideViewModel: ObservableObject {
    @Published var rides: [RideModel] = []
    @Published var rideStatus: [RideAction]  = [.upcoming, .history, .invities]
    @Published var rideViewActions: [RideViewAction]  = [ .viewDetails, .share]
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
     var rideRepository: RidesRepository
    private let userRepo: UserRepository
    @Published  var participants: [Participant] = []
    @Published var rideDetails: [RideDetailsModel] = []
    @Published var rideFromDeepLink: RideModel? = nil
    @Published var isUploading:Bool = false
    @Published var joinRideModel = JoinRideModel(userId: "", rideId: "", title: "", organizer: "", description: "", route: "", distance: "", date: "", ridersCount: "", maxRiders: "", riderImage: "", contactNumber: "", startLat: 0.0, startLong: 0.0, endLat: 0.0, endLong: 0.0, rideJoined: false, participants: [], hasAssemblyPoint: false, assemblyLat: nil as Double?, assemblyLon: nil as Double?)
    init() {
        rideAPIService = RidesApiServiceImpl(client: KtorClient())
        rideRepository = RidesRepository(apiService: rideAPIService)
        let userApiService = UserAPIServiceImpl(client: KtorClient())
        self.userRepo = UserRepository(apiService: userApiService)
    }
    
    // MARK: - Get all rides
    @MainActor
    func fetchAllRides() async {
        isRideLoading = true
        defer { isRideLoading = false }
        
        do {
            // Fetch rides from repository
            let result = try await rideRepository.getAllRide()
            
            guard let success = result as? APIResultSuccess<AnyObject>,
                         let rideArrayAny = success.data as? [AnyObject] else {
                       print("Unexpected rides result:", result)
                       return
                   }

            
            let rideArray: [RidesData] = rideArrayAny.compactMap { $0 as? RidesData }
            
            //  Early exit if empty
            guard !rideArray.isEmpty else {
                self.upcomingRides = []
                self.historyRides = []
                self.inviteRides = []
                self.upcomingInvitesRide = []
                self.rides = []
                print("No rides available")
                return
            }
            
            let currentUserID = MBUserDefaults.userIdStatic ?? ""
            let todayStart = Calendar.current.startOfDay(for: Date())
            
            //  Map RidesData -> RideModel
            let rides: [RideModel] = rideArray.compactMap {  ride -> RideModel?  in
                guard let startEpoch = ride.startDate,
                      let endEpoch = ride.endDate else { return nil }
                
                let startDate = Date(timeIntervalSince1970: Double(truncating: startEpoch) / 1000)
                let endDate = Date(timeIntervalSince1970: Double(truncating: endEpoch) / 1000)
                
                let now = Date()
                let isPastRide = endDate < now
               let isFutureRide = Calendar.current.isDate(startDate, inSameDayAs: now) || startDate > now

                let isCompletedRide = ride.rideStatus == 4
                let myInviteStatus = ride.participants.first(where: { $0.userId == currentUserID })?.inviteStatus
                let isCreator = ride.createdBy == currentUserID

                // Decide if this ride should even be shown
                var shouldInclude = false

                // HISTORY
                if isPastRide || isCompletedRide || myInviteStatus == 4 {
                    shouldInclude = true
                }

                // FUTURE / TODAY
                else if isFutureRide {
                    if isCreator {
                        shouldInclude = [0,1,3].contains(ride.rideStatus)
                    } else {
                        shouldInclude = [0,1,3].contains(myInviteStatus ?? -1)
                    }
                }

                if !shouldInclude { return nil }
                
                // Participants
                let participants = ride.participants
                let participantCount = participants.filter { $0.userId != ride.createdBy }.count
                let participantAcceptedCount = participants.filter { [1,3].contains($0.inviteStatus) }.count
                
                // Determine rideAction, rideStatus, rideViewAction
                var rideAction: RideAction
                var rideStatus: RideStatus
                var rideViewAction: RideViewAction
                
                let imageCount = Int(ride.imageCount ?? 0)
                let hasPhotos = imageCount > 0
                
                if isCreator {
                    if ride.rideStatus == 4 || isPastRide {
                        rideAction = .history
                        rideStatus = .complete
                        rideViewAction = hasPhotos ? .viewPhotos : .addPhotos
                    } else {
                        rideAction = .upcoming
                        rideStatus = .upcoming
                        rideViewAction = .viewDetails
                    }
                } else if let status = myInviteStatus {
                    
                    if status == 4 || isPastRide {
                        rideAction = .history
                        rideStatus = .complete
                        rideViewAction = hasPhotos ? .viewPhotos : .addPhotos
                    }
                    else if status == 0 {
                        rideAction = .invities
                        rideStatus = .invite
                        rideViewAction = .decline
                    }
                    else if status == 1 || status == 3 {
                        rideAction = .upcoming
                        rideStatus = .upcoming
                        rideViewAction = .viewDetails
                    }
                    else {
                        return nil
                    }
                } else {
                    return nil
                }
                
                // Dates & times using static formatters
                let startText = Self.dateFormatter.string(from: startDate)
                let endText = Self.dateFormatter.string(from: endDate)
                let dateString = "\(startText) - \(endText)"
                let startTime = Self.timeFormatter.string(from: startDate)
                let endTime = Self.timeFormatter.string(from: endDate)
                
                // Ratings for current user
                let myRating: Int? = ride.ratings.first(where: { $0.userId == currentUserID }).map { Int($0.stars) }
                
                return RideModel(
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
                    hasPhotos: imageCount > 0,
                    startDate: startDate,
                    participantAcceptedCount: participantAcceptedCount,
                    startTime: startTime,
                    endTime: endTime,
                    ratings: myRating,
                    participants: participants
                )
            }
            
            //  Separate rides into sections
            self.upcomingRides = rides.filter { $0.rideAction == .upcoming }.sorted { $0.startDate < $1.startDate }
            self.historyRides = rides.filter { $0.rideAction == .history }.sorted { $0.startDate > $1.startDate }
            self.inviteRides = rides.filter { $0.rideAction == .invities }.sorted { $0.startDate < $1.startDate }
            
            self.upcomingInvitesRide = upcomingRides.filter { $0.createdBy == currentUserID } + inviteRides
            self.rides = upcomingRides + historyRides + inviteRides
            
            print("Fetched rides count:", self.rides.count)
            
        } catch {
            print("Error fetching rides:", error)
            self.upcomingRides = []
            self.historyRides = []
            self.inviteRides = []
            self.upcomingInvitesRide = []
            self.rides = []
        }
    }

    // MARK: - Reusable DateFormatters
    
    
    func formatDate(_ date: Date) -> String {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "en_US_POSIX")
        formatter.dateFormat = "E, MMM dd"
        return formatter.string(from: date)
    }
    func formatTime(_ date: Date) -> String {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "en_US_POSIX")
        formatter.dateFormat = "hh:mm a"
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
                await self.fetchAllRides()
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
    
    @MainActor
    func deleteRide(rideId: String)  async {
        self.isRideLoading = true
        do {
            let result = try await rideRepository.deleteRide(
                rideId: rideId
            )
          
            if result is APIResultSuccess<GenericResponse> {
                print(" deleted ride")
                await self.fetchAllRides()
            }
        } catch {
            print(" Exception: \(error.localizedDescription)")
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
        let ride: RidesData? = await withCheckedContinuation { continuation in
            rideRepository.getSingeRide(rideID: rideId) { result, _ in
                guard
                    let success = result as? APIResultSuccess<AnyObject>,
                    let ride = success.data as? RidesData
                else {
                    continuation.resume(returning: nil)
                    return
                }
                continuation.resume(returning: ride)
            }
        }
        guard let ride, let startEpoch = ride.startDate else {
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
                    participants: finalParticipants,
                    hasAssemblyPoint: ride.hasAssemblyPoint,
                    assemblyLat: ride.hasAssemblyPoint ? ride.assemblyLat : nil as Double?,
                    assemblyLon: ride.hasAssemblyPoint ? ride.assemblyLon : nil as Double?
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
    
    /// Call after getSingleRide to set rideFromDeepLink for navigation (e.g. from deep link).
    func setRideFromDeepLinkIfPossible() {
        if let model = buildRideModelFromCurrentJoinRide() {
            rideFromDeepLink = model
        }
    }
    
    /// Build a RideModel from current joinRideModel/rideDetails (e.g. after getSingleRide for deep link).
    func buildRideModelFromCurrentJoinRide() -> RideModel? {
        let j = joinRideModel
        guard !j.rideId.isEmpty else { return nil }
        let parts = j.route.split(separator: "-").map { String($0).trimmingCharacters(in: .whitespaces) }
        let routeStart = parts.first ?? ""
        let routeEnd = parts.dropFirst().joined(separator: "-").trimmingCharacters(in: .whitespaces)
        let confirmedCount = rideDetails.filter { $0.status == "confirmed" }.count
        return RideModel(
            id: j.rideId,
            title: j.title,
            routeStart: routeStart,
            routeEnd: routeEnd,
            status: .upcoming,
            rideViewAction: .viewDetails,
            rideAction: .upcoming,
            date: j.date,
            riderCount: Int(j.ridersCount) ?? 0,
            createdBy: j.userId,
            hasPhotos: false,
            startDate: Date(),
            participantAcceptedCount: confirmedCount,
            startTime: nil,
            endTime: nil,
            ratings: nil,
participants: []
        )
    }
}

extension UpcomingRideViewModel {
    static let dateFormatter: DateFormatter = {
           let f = DateFormatter()
           f.locale = Locale(identifier: "en_US_POSIX")
           f.dateFormat = "E, MMM dd"
           return f
       }()
       
       static let timeFormatter: DateFormatter = {
           let f = DateFormatter()
           f.locale = Locale(identifier: "en_US_POSIX")
           f.dateFormat = "hh:mm a"
           return f
       }()
    /// - Parameter showGlobalProgress: When `false`, skips toggling `isUploading` (e.g. caller shows inline skeleton on the ride-photos popup).
    @MainActor
    func uploadImages(images:[UIImage], rideId:String, showGlobalProgress: Bool = true) async throws -> String {
        print("Images Count:\(images.count)")
        var encoadedImages:[String] = []
        for eachImage in images {
            if let encodedImage = encodeImageToBase64(image: eachImage) {
                encoadedImages.append(encodedImage)
            }
        }
        if showGlobalProgress {
            isUploading = true
        }
        return try await withCheckedThrowingContinuation { (continuation: CheckedContinuation<String, Error>) in
            rideRepository.uploadImage(rideId: rideId, images: encoadedImages, completionHandler: { result, error in
                if let error = error {
                    Task { @MainActor in
                        print("Error uploading image: \(error)")
                        if showGlobalProgress {
                            self.isUploading = false
                        }
                        continuation.resume(throwing: error)
                    }
                } else {
                    Task { @MainActor in
                        print("Image uploaded successfully:\(images.count)")
                        if showGlobalProgress {
                            self.isUploading = false
                        }
                        self.rideRepository.updateImageCount(count : Int32(images.count) , rideId: rideId)
                        continuation.resume(returning: "success")
                    }
                }
            })
        }
    }
    
    func encodeImageToBase64(image: UIImage) -> String? {
        guard let imageData = image.jpegData(compressionQuality: 0.1) else { return nil}
        let data = NSData(data: imageData)
        return data.base64EncodedString()
    }
    
    func decodeBase64ToImage(base64: String) -> UIImage? {
        var s = base64.trimmingCharacters(in: .whitespacesAndNewlines)
        // Handle common "data:image/...;base64," prefix.
        if let comma = s.firstIndex(of: ","),
           s[..<comma].lowercased().contains("base64") {
            s = String(s[s.index(after: comma)...])
        }
        // Some backends send URL-safe base64.
        s = s.replacingOccurrences(of: "-", with: "+")
            .replacingOccurrences(of: "_", with: "/")
        // Fix missing padding.
        let remainder = s.count % 4
        if remainder != 0 {
            s.append(String(repeating: "=", count: 4 - remainder))
        }
        guard let data = Data(base64Encoded: s, options: [.ignoreUnknownCharacters]) else { return nil }
        return UIImage(data: data)
    }
    
    @MainActor
    
    func deleteRidePhoto(rideId: String, photoId: String,currentCount: Int) async throws {
        return try await withCheckedThrowingContinuation { continuation in
            rideRepository.deleteImage(rideId: rideId, imageId: photoId, completionHandler: { result, error in
                if let error = error {
                    print("Error deleting photo: \(error)")
                    continuation.resume(throwing: error)
                } else {
                    Task { @MainActor in
                        self.rideRepository.updateImageCount(count: -1, rideId: rideId)
                        print("Photo deleted successfully")
                        continuation.resume()
                    }
                }
            })
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

