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


@MainActor
final class ProfileViewModel: ObservableObject {
    @Published var profileName = "Aromal Sijulal"
    @Published var bikeType = "Adventure Bike"
    @Published var role = "Mechanic"
    @Published var profileImage = AppImage.Welcome.bg
    @Published var sections: [ProfileSection] = []
    @Published var selectBikeType: [SelectBikeType] = []
    
    
    init() {
        loadData()
    }
    
    private func loadData() {
        sections = [
            ProfileSection(
                section: 0, title: AppStrings.Profile.yourVehicles,
                subtitle: AppStrings.Profile.description, icon: AppIcon.Profile.vehicle,
                
                items: [
                    ProfileItemModel(icon: AppIcon.NavigationSlider.home, iconColor: AppColor.purple, title: AppStrings.Profile.noVehicles, subtitle: AppStrings.Profile.addPrompt, destination: AnyView(SelectYourRideView())),
                ]
            ),
            ProfileSection(
                section: 1, title: AppStrings.Profile.totalStats,
                subtitle: "",
                icon: Image(""),
                
                items: [
                    ProfileItemModel(icon: AppIcon.Profile.distance, iconColor: AppColor.yellow, title: "2,847 km", subtitle: AppStrings.Profile.distanceCovered, destination: AnyView(ConnectedRideScreen())),
                    ProfileItemModel(icon: AppIcon.Profile.path, iconColor: AppColor.skyBlue, title: "25 Rides", subtitle: AppStrings.Profile.totalRides, destination: AnyView(HomeView())),
                    ProfileItemModel(icon: AppIcon.Profile.pin, iconColor: AppColor.yellow, title: "12 Cities", subtitle: AppStrings.Profile.placesExplored, destination: AnyView(ConnectedRideScreen())),
                    ProfileItemModel(icon: AppIcon.Profile.elevation, iconColor: AppColor.skyBlue, title: AppStrings.Profile.elevationGain, subtitle: "12,450 m", destination: AnyView(HomeView()))
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
}
