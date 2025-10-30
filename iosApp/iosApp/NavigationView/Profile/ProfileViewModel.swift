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
    let type: String
    let bikeId: String
}

@MainActor
final class ProfileViewModel: ObservableObject {
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
    
    private var api: FirebaseApi
    private var viewModel: ProfileKMPViewModel
    
    init() {
        api = FirebaseApi()
        viewModel = ProfileKMPViewModel(api: api)
        loadData()
    }

    private func loadData() {
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
                    ProfileItemModel(icon: AppIcon.Profile.path, iconColor: AppColor.skyBlue, title: "25 Rides", subtitle: AppStrings.Profile.totalRides, destination: AnyView(HomeView())),
                    ProfileItemModel(icon: AppIcon.Profile.pin, iconColor: AppColor.yellow, title: "12 Cities", subtitle: AppStrings.Profile.placesExplored, destination: AnyView(HomeView())),
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
    
    func validateProfile(fullName:String, email:String,phoneNumber:String,emargencyContact:String, DL:String) -> Bool {
        return fullName.isEmpty && email.isEmpty && phoneNumber.isEmpty && emargencyContact.isEmpty && DL.isEmpty
    }
    
    func validateMake(make:String,moodel:String) -> Bool {
        return make.isEmpty && moodel.isEmpty
    }
    
    func getBikeType(model:String,make:String,type:String,bikeId:String) {
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
            if let profile = try await viewModel.getProfile(userId: userId) {
                self.profileName = profile.userName
                self.email = profile.email
                self.phoneNumber = profile.phoneNumber
                self.role = (Bool(profile.isMechanic) ?? false) ? "Mechanic" : ""
            }
        } catch {
            print("Error fetching profile: \(error)")
        }
        
        Task {
            await self.fetchBikes(userId: userId)
        }
    }
    
    func fetchBikes(userId: String) async {
        do {
            let bikesArray = try await viewModel.getBikes(userId: userId)
            await MainActor.run {
                self.selectedBikeType.removeAll()
            }
            for echBike in bikesArray {
                getBikeType(model: echBike.model, make: echBike.make, type: echBike.bikeType, bikeId: echBike.bikeId)
            }
        } catch {
            print("Error fetching bikes: \(error)")
        }
    }
    
    func addNewBike(userId: String, model: String, make: String, type: String) async {
        let bikeInfo = BikeInfo(bikeId: "", bikeType: type, make: make, model: model)
        do {
            try await viewModel.addBike(userId: userId, bike: bikeInfo)
            await fetchBikes(userId: userId)
        } catch {
            print("Error adding bike: \(error)")
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
        let profileInfo = ProfileInfo(
            userName: userName,
            email: email,
            phoneNumber: phoneNumber,
            emergencyContact: emergencyContact,
            drivingLicense: drivingLicense,
            isMechanic: "\(isMachanic)",
            bikes: nil
        )
        
        viewModel.createOrEditProfile(
            userId: userId,
            profile: profileInfo,
            isEdit: true,
            email: email,
            userName: userName
        ) { error in
            if let error = error {
                print("Error editing profile: \(error)")
            } else {
                print("Profile updated successfully.")
            }
        }
    }
    
    func deleteBike(userId: String, bikeId: String) async {
        Task {
            do {
                try await viewModel.deleteBike(userId: userId, bikeId: bikeId)
                await fetchBikes(userId: userId)
            } catch {
                print("Error deleting bike: \(error)")
            }
        }
    }
}
