//
//  NavigationSliderViewModel.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 06/10/25.
//

import SwiftUI
import Combine

struct MenuSection: Identifiable {
    let id = UUID()
    let title: String
    let items: [MenuItemModel]
}

struct MenuItemModel: Identifiable {
    let id = UUID()
    let icon: Image
    let iconColor: Color
    let title: String
    let subtitle: String
    let destination: AnyView
}

@MainActor
final class NavigationSliderViewModel: ObservableObject {
    @Published var profileName = "Aromal Sijulal"
    @Published var bikeType = "Adventure Bike"
    @Published var role = "Mechanic"
    @Published var profileImage = AppImage.Welcome.bg
    @Published var sections: [MenuSection] = []
    
    init() {
        loadData()
    }
    
    private func loadData() {
        sections = [
            MenuSection(
                title: "Main",
                items: [
                    MenuItemModel(icon: AppIcon.NavigationSlider.home, iconColor: AppColor.purple, title: "Home", subtitle: "Your riding dashboard", destination: AnyView(HomeScreen())),
                    MenuItemModel(icon: AppIcon.NavigationSlider.profile, iconColor: AppColor.celticBlue, title: "Profile", subtitle: "Manage your info", destination: AnyView(ProfileScreen())),
                    MenuItemModel(icon: AppIcon.NavigationSlider.yourRide, iconColor: AppColor.orange, title: "Your Rides", subtitle: "Your ride history", destination: AnyView(YourRideScreen())),
                    MenuItemModel(icon: AppIcon.NavigationSlider.createRide, iconColor: AppColor.green, title: "Create Ride", subtitle: "Plan new adventure", destination: AnyView(CreateRideScreen()))
                ]
            ),
            MenuSection(
                title: "Community",
                items: [
                    MenuItemModel(icon: AppIcon.NavigationSlider.connectedRide, iconColor: AppColor.yellow, title: "Connected Ride", subtitle: "Join group rides", destination: AnyView(ConnectedRideScreen())),
                    MenuItemModel(icon: AppIcon.NavigationSlider.queries, iconColor: AppColor.skyBlue, title: "Queries", subtitle: "Ask & answer", destination: AnyView(HomeScreen()))
                ]
            ),
            MenuSection(
                title: "Learning",
                items: [
                    MenuItemModel(icon: AppIcon.NavigationSlider.knowledgeCircle, iconColor: AppColor.red, title: "Knowledge Circle", subtitle: "Learn road signs", destination: AnyView(HomeScreen())),
                    MenuItemModel(icon: AppIcon.NavigationSlider.motoQuiz, iconColor: AppColor.navyBlue, title: "Moto Quiz", subtitle: "Test your skills", destination: AnyView(HomeScreen())),
                    MenuItemModel(icon: AppIcon.NavigationSlider.preRideCheck, iconColor: AppColor.lime, title: "Pre Ride Check", subtitle: "Ask & answer", destination: AnyView(HomeScreen()))
                ]
            ),
            MenuSection(
                title: "More",
                items: [
                    MenuItemModel(icon: AppIcon.NavigationSlider.marketPlace, iconColor: AppColor.pink, title: "Marketplace", subtitle: "Buy and sell gears", destination: AnyView(HomeScreen())),
                    MenuItemModel(icon: AppIcon.NavigationSlider.settings, iconColor: AppColor.charcol, title: "Settings", subtitle: "App preferences", destination: AnyView(HomeScreen()))
                ]
            )
        ]
    }
}
