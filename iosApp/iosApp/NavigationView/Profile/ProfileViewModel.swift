//
//  ProfileViewModel.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 09/10/25.
//

import Foundation
import SwiftUI

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
    let index: Int
}

@MainActor
final class ProfileViewModel: ObservableObject {
    @Published var profileName = "Aromal Sijulal"
    @Published var bikeType = "Adventure Bike"
    @Published var role = "Mechanic"
    @Published var profileImage = AppImage.Welcome.bg
    @Published var sections: [ProfileSection] = []
    @Published var selectBikeType: [SelectBikeType] = []
    let vehicleArray: [AppStrings.VehicleType] = AppStrings.VehicleType.allCases
    @Published var selectedBikeType: [SelectedBikeType] = []
    
    init() {
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
                    ProfileItemModel(icon: AppIcon.Profile.pin, iconColor: AppColor.yellow, title: "12 Cities", subtitle: AppStrings.Profile.placesExplored, destination: AnyView(ConnectedRideScreen())),
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
    
    func getBikeType(model:String,make:String,type:String) {
        selectedBikeType.append(SelectedBikeType(model: model, make: make, type: type, index: selectBikeType.count - 1))
    }
    
    func deleteSelectedBikeType(at index: Int) {
        selectedBikeType.remove(at: index)
    }
}
