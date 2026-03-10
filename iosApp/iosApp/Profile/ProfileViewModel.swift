//
//  ProfileViewModel.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 09/10/25.
//

import Foundation
import SwiftUI
import shared

struct ProfileSection: Identifiable {
    let id = UUID()
    let section:Int
    let title: String
    let subtitle: String?
    let icon: Image?
    
    let items: [ProfileItemModel]
}

struct ProfileItemModel: Identifiable {
    let id = UUID()
    let icon: Image
    let iconColor: Color
    let title: String
    let subtitle: String
    let destination: AnyView
}

struct SelectBikeType: Identifiable {
    let id = UUID()
    let image: Image
    let title: String
    let subtitle: String
    let currentPage: Int
    let totalPages: Int
}

struct TotalStatistics: Identifiable {
    let id = UUID()
    let imageName: Image
    let title: String
}

struct SelectedBikeType: Identifiable,Hashable {
    let id = UUID()
    let model: String
    let make: String
    let type: Int32
    let bikeId: String
}

@MainActor
class ProfileViewModel: ObservableObject {
    @Published var isLoading = false
    @Published var profileName = "--"
    @Published var email = "--"
    @Published var role = ""
    @Published var phoneNumber = "--"
    @Published var emergencyNumber = "--"
    @Published var drivingLicenseNumber: String = "--"
    @Published var isMechanic: Bool = false
    @Published var profileImage = AppImage.Profile.profile
    @Published var sections: [ProfileSection] = []
    @Published var selectBikeType: [SelectBikeType] = []
    let vehicleArray: [AppStrings.VehicleType] = AppStrings.VehicleType.allCases
    let vehicleImageArray: [Image] = [AppIcon.Profile.sportsBike,
                                      AppIcon.Profile.nakedBike,
                                      AppIcon.Profile.touringBike,
                                      AppIcon.Profile.adventureBike,
                                      AppIcon.Profile.cruiserBike,
                                      AppIcon.Profile.scooter,
                                      AppIcon.Profile.electric]
    
    @Published var selectedBikeType: [SelectedBikeType] = []
    
    /// True while profile (name, email, image) is loading
    /// True while vehicles list is loading (does not block the whole screen)
    @Published var isLoadingBikes = false
    
    private var profileAPIService:ProfileAPIService
    private var profileRepository:ProfileRepository
    private var ktorClient: KtorClient
    private var rideRepository: RidesRepository
    private var rideAPIService: RidesApIService
    @Published var rideCount: Int = 0
    @Published var locationCount: Int = 0
    init() {
        
        ktorClient = KtorClient()
        profileAPIService = ProfileAPIServiceImpl(client: ktorClient)
        profileRepository = ProfileRepository(apiService: profileAPIService)
        
        rideAPIService = RidesApiServiceImpl(client: ktorClient)
        rideRepository = RidesRepository(apiService: rideAPIService)
        
        // Show profile layout immediately with placeholder stats so the view doesn't wait
        buildSections(rideCount: 0, locationCount: 0)
    }
    
    /// Builds sections for the profile list. Call with 0,0 for instant UI; call again when stats are loaded.
    func buildSections(rideCount: Int, locationCount: Int) {
        self.rideCount = rideCount
        self.locationCount = locationCount
        sections = [
            ProfileSection(
                section: 0, title: AppStrings.Profile.yourVehicles,
                subtitle: AppStrings.Profile.description, icon: AppIcon.Profile.vehicle,
                items: [
                    ProfileItemModel(icon: AppIcon.NavigationSlider.home, iconColor: AppColor.purple, title: AppStrings.Profile.noVehicles, subtitle: AppStrings.Profile.addPrompt, destination: AnyView(EmptyView())),
                ]
            ),
            ProfileSection(
                section: 1, title: AppStrings.Profile.totalStats,
                subtitle: "",
                icon: Image(""),
                items: [
                    ProfileItemModel(icon: AppIcon.Profile.path, iconColor: AppColor.skyBlue, title: "\(rideCount) Rides", subtitle: AppStrings.Profile.totalRides, destination: AnyView(HomeView())),
                    ProfileItemModel(icon: AppIcon.Profile.pin, iconColor: AppColor.yellow, title: "\(locationCount) Cities", subtitle: AppStrings.Profile.placesExplored, destination: AnyView(HomeView())),
                ]
            ),
            ProfileSection(
                section: 2, title: AppStrings.Profile.achievements,
                subtitle: AppStrings.Profile.badgesDescription,
                icon: AppIcon.Profile.award,
                items: [
                    ProfileItemModel(icon: AppIcon.Profile.trophy, iconColor: AppColor.red, title: AppStrings.Profile.noBadges, subtitle: AppStrings.Profile.earnBadgeTip, destination: AnyView(HomeView())),
                ]
            )
        ]
    }
    
    /// Fetches ride stats and updates sections. Run in parallel with fetchProfile for faster load.
    func loadData(homeVM: HomeViewModel) async {
        let statisticsDetail = await fetchRideStats()
        buildSections(rideCount: statisticsDetail.completedRides, locationCount: statisticsDetail.citiesVisited)
    }
    
    func validateProfile(fullName:String, email:String,phoneNumber:String,emargencyContact:String, DL:String) -> Bool {
        return fullName.isEmpty && email.isEmpty && phoneNumber.isEmpty && emargencyContact.isEmpty && DL.isEmpty
    }
    
    func validateMake(make:String,moodel:String) -> Bool {
        return make.isEmpty && moodel.isEmpty
    }
    
    func getBikeType(model:String,make:String,type:Int32,bikeId:String) {
        selectedBikeType.append(SelectedBikeType(model: model, make: make, type: type, bikeId: bikeId))
    }
    
    func deleteSelectedBikeType(id: UUID) async {
        if let index = selectedBikeType.firstIndex(where: { $0.id == id }) {
            let bikeId = selectedBikeType[index].bikeId
            await deleteBike(userId: MBUserDefaults.userIdStatic ?? "", bikeId:bikeId)
            selectedBikeType.remove(at: index)
        }
    }
}

//MARK: - Base64 -
extension ProfileViewModel {
    func encodeImageToBase64(image: UIImage) -> String? {
        guard let imageData = image.jpegData(compressionQuality: 0.1) else { return nil}
        let data = NSData(data: imageData)
        return data.base64EncodedString()
    }
    
    func decodeBase64ToImage(base64: String) -> UIImage? {
        guard let data = Data(base64Encoded: base64) else { return nil }
        return UIImage(data: data)
    }
}

// MARK: - Firebase API -

extension ProfileViewModel {
    func fetchProfile(userId: String) async {
        isLoading = true
        do {
            try await withCheckedThrowingContinuation { continuation in
                profileRepository.getProfile(userId: userId) { result, error in
                    if let success = result as? APIResultSuccess<AnyObject>,
                       let domain = success.data as? ProfileDomain {
                        Task { @MainActor in
                            self.profileName = domain.userName
                            self.email = domain.email
                            self.phoneNumber = domain.phoneNumber
                            self.role = domain.isMechanic ? "Mechanic" : ""
                            self.isMechanic = domain.isMechanic
                            self.emergencyNumber = domain.emergencyContact
                            self.drivingLicenseNumber = domain.drivingLicense
                            let emergency = (domain.emergencyContact).trimmingCharacters(in: .whitespaces)
                            MBUserDefaults.emergencyContactStatic = emergency.isEmpty ? nil : emergency
                            if let domainImage = domain.profilePicUrl as? String,
                             let base64Image = self.decodeBase64ToImage(base64: domainImage) {
                              self.profileImage = Image(uiImage: base64Image)
                          }
                            self.isLoading = false
                            continuation.resume()
                        }
                    } else if let error = error {
                        Task { @MainActor in
                            self.isLoading = false
                            continuation.resume(throwing: error)
                        }
                    } else {
                        Task { @MainActor in
                            self.isLoading = false
                            continuation.resume(throwing: NSError(domain: "UnknownError", code: -1))
                        }
                    }
                }
            }
        } catch {
            self.isLoading = false
            print("Error fetching profile: \(error)")
        }
        await fetchBikes(userId: MBUserDefaults.userIdStatic ?? "")
    }
    
    func fetchBikes(userId: String) async {
        isLoadingBikes = true
        do {
            try await withCheckedThrowingContinuation { continuation in
                profileRepository.getBikes(userId: userId) { [self] result, error in
                    if let success = result as? APIResultSuccess<AnyObject>,
                       let domainArray = success.data as? [BikeDomain] {
                        Task { @MainActor in
                            self.selectedBikeType.removeAll()
                            for eachBike in domainArray {
                                self.getBikeType(
                                    model: eachBike.model,
                                    make: eachBike.make,
                                    type: eachBike.type,
                                    bikeId: eachBike.bikeId
                                )
                            }
                            self.isLoadingBikes = false
                             continuation.resume()
                        }
                     
                    } else if let error = error {
                        Task { @MainActor in
                            self.isLoadingBikes = false
                            continuation.resume(throwing: error)
                            
                        }
                    } else {
                        Task { @MainActor in
                            self.isLoadingBikes = false
                            continuation.resume(throwing: NSError(domain: "UnknownError", code: -1))
                        }
                    }
                }
            }
        } catch {
            self.isLoadingBikes = false
            print("Error fetching bikes: \(error)")
        }
    }

    func addNewBike(userId: String, model: String, make: String, type:Int32) async {
        do {
            try await profileRepository.addBike(userId: userId, bikeType: type, make: make, model: model)
            print("Bike added successfully")
            await fetchBikes(userId: userId)
        } catch {
            print("Error: \(error)")
        }
    }

    func editProfile(
        userId: String,
        userName: String,
        email: String,
        phoneNumber: String,
        emergencyContact: String,
        drivingLicense: String,
        isMachanic: Bool,
        profileUIImage: UIImage,
        onSuccess: (() -> Void)? = nil
    ) {
        
        let base64Image = encodeImageToBase64(image: profileUIImage) ?? ""
        
        profileRepository.editProfile(userId: userId, userName: userName, email: email, contactNumber: phoneNumber, emergencyContact: emergencyContact, drivingLicense: drivingLicense, isMechanic: isMachanic, profileImage:base64Image , completionHandler: { [weak self] result,error  in
            
            if let error = error {
                print("Error editing profile: \(error)")
            } else {
                print("Profile updated successfully.")
                MBUserDefaults.emergencyContactStatic = emergencyContact.isEmpty ? nil : emergencyContact
                Task { @MainActor in
                    await self?.fetchProfile(userId: userId)
                    onSuccess?()
                }
            }
            
            if let result = result {
                print("Result: \(result)")
            }
        })
        
    }
    
    func deleteBike(userId: String, bikeId: String) async {
        isLoadingBikes = true
        do {
            try await profileRepository.deleteBike(userId: userId, bikeId: bikeId)
        } catch {
            print("Error deleting bike: \(error)")
        }
        isLoadingBikes = false
    }
    
    func fetchRideStats() async -> (completedRides: Int, citiesVisited: Int) {
        do {
            let result = try await rideRepository.getAllRide()
            
            guard let success = result as? APIResultSuccess<AnyObject>,
                  let rideArray = success.data as? [RidesData] else {
                print("Error parsing rides")
                return (0, 0)
            }
            
            let currentUserID = MBUserDefaults.userIdStatic ?? ""
            guard !currentUserID.isEmpty else {
                return (0, 0)
            }
            
            var completedRidesCount = 0
            var visitedCities = Set<String>()
            
            for ride in rideArray {
                let isCreator = (ride.createdBy ?? "") == currentUserID
                let isParticipant = ride.participants.contains { ($0.userId ?? "") == currentUserID }
                
                let statusValue: Int = {
                    if let n = ride.rideStatus as? NSNumber { return n.intValue }
                    if let i = ride.rideStatus as? Int { return i }
                    if let i32 = ride.rideStatus as? Int32 { return Int(i32) }
                    return 0
                }()
                
                guard statusValue == 4, isCreator || isParticipant else { continue }
                
                completedRidesCount += 1
                
                if let start = ride.startLocation?.trimmingCharacters(in: .whitespaces), !start.isEmpty {
                    visitedCities.insert(start)
                }
                if let end = ride.endLocation?.trimmingCharacters(in: .whitespaces), !end.isEmpty {
                    visitedCities.insert(end)
                }
            }
            
            return (completedRidesCount, visitedCities.count)
            
        } catch {
            print("Error fetching rides:", error)
            return (0, 0)
        }
    }
}

