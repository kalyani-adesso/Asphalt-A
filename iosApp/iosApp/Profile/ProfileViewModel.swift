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
    @Published var profileImage = AppImage.Welcome.bg
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
    
    private var profileAPIService:ProfileAPIService
    private var profileRepository:ProfileRepository
    private var ktorClient: KtorClient
    init() {
        ktorClient = KtorClient()
        profileAPIService = ProfileAPIServiceImpl(client: ktorClient)
        profileRepository = ProfileRepository(apiService: profileAPIService)
    }
    
    func loadData(homeVM: HomeViewModel) {
        
        
        let totalRides = homeVM.stats.first(where: { $0.title == "Total Rides" })?.value ?? "0"
           let locations = homeVM.stats.first(where: { $0.title == "Locations" })?.value ?? "0"

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
                    ProfileItemModel(icon: AppIcon.Profile.path, iconColor: AppColor.skyBlue, title: "\(totalRides) Rides", subtitle: AppStrings.Profile.totalRides, destination: AnyView(HomeView())),
                    ProfileItemModel(icon: AppIcon.Profile.pin, iconColor: AppColor.yellow, title: "\(locations) Cities", subtitle: AppStrings.Profile.placesExplored, destination: AnyView(HomeView())),
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
    
    func validateProfile(fullName:String, email:String,phoneNumber:String,emargencyContact:String, DL:String, isMachanic:Bool ) -> Bool {
        return fullName.isEmpty && email.isEmpty && phoneNumber.isEmpty && emargencyContact.isEmpty && DL.isEmpty && !isMachanic
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

// MARK: - Firebase API -

extension ProfileViewModel {
    func fetchProfile(userId: String) async {
        do {
            self.isLoading = true
            try await withCheckedThrowingContinuation { continuation in
                profileRepository.getProfile(userId: userId) { result, error in
                    if let success = result as? APIResultSuccess<AnyObject>,
                       let domain = success.data as? ProfileDomain {
                        Task { @MainActor in
                            self.profileName = domain.userName
                            self.email = domain.email
                            self.phoneNumber = domain.phoneNumber
                            self.role = domain.isMechanic ? "Mechanic" : ""
//                           ratingpicker
                            self.isLoading = false
                            continuation.resume()
                        }
                       
                     
                    } else if let error = error {
                        Task { @MainActor in
                            continuation.resume(throwing: error)
                        }
                    } else {
                        Task { @MainActor in
                            continuation.resume(throwing: NSError(domain: "UnknownError", code: -1))
                        }
                    }
                }
            }
        } catch {
            print("Error adding bike: \(error)")
        }
        Task {
           await fetchBikes(userId: MBUserDefaults.userIdStatic ?? "")
        }
    }
    
    func fetchBikes(userId: String) async {
        do {
           isLoading = true
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
                            isLoading = false
                             continuation.resume()
                        }
                     
                    } else if let error = error {
                        Task { @MainActor in
                            continuation.resume(throwing: error)
                            
                        }
                    } else {
                        Task { @MainActor in
                            continuation.resume(throwing: NSError(domain: "UnknownError", code: -1))
                        }
                    }
                }
            }
        } catch {
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
        isMachanic: Bool
    ) {
        
        profileRepository.editProfile(userId: userId, userName: userName, email: email, contactNumber: phoneNumber, emergencyContact: emergencyContact, drivingLicense: drivingLicense, isMechanic: isMachanic, completionHandler: { result,error  in
            
            if let error = error {
                print("Error editing profile: \(error)")
            } else {
                print("Profile updated successfully.")
            }
            
            if let result = result {
                print("Result: \(result)")
            }
        })
        
    }
    
    func deleteBike(userId: String, bikeId: String) async {
        Task {
           isLoading = true
            do {
                try await profileRepository.deleteBike(userId: userId, bikeId: bikeId)
                isLoading = false
            }
        }
    }
}
