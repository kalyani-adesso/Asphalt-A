//
//  NavigationSliderViewModel.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 06/10/25.
//

import SwiftUI
import Combine
import shared

struct MenuItemModel: Identifiable, Hashable {
    let id = UUID()
    let icon: Image
    let iconColor: Color
    let title: String
    let destination: AnyView

    static func == (lhs: MenuItemModel, rhs: MenuItemModel) -> Bool {
        lhs.id == rhs.id
    }

    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }
}

@MainActor
final class NavigationSliderViewModel: ObservableObject {
    @Published var sections: [MenuItemModel] = []
    
    init() {
        loadData()
    }
    
    private func loadData() {
        sections = [
            MenuItemModel(icon: AppIcon.NavigationSlider.connectedRide, iconColor: AppColor.black, title: AppStrings.NavigationSlider.connectedRide, destination: AnyView(ConnectedRideView(notificationTitle: AppStrings.ConnectedRide.rideCompleted, title: AppStrings.ConnectedRide.startRideTitle, subTitle: AppStrings.ConnectedRide.startRideSubtitle, model:JoinRideModel(
                title: "Weekend Coast Ride",
                organizer: "Sooraj",
                description: "Join us for a beautiful sunrise ride along the coastal highway",
                route: "Kochi - Kanyakumari",
                distance: "280km",
                date: "Sun, Oct 21",
                ridersCount: "3",
                maxRiders: "8",
                riderImage: "rider_avatar", contactNumber: ""
            )))),
            MenuItemModel(icon: AppIcon.NavigationSlider.marketPlace, iconColor: AppColor.black, title: AppStrings.NavigationSlider.marketplace, destination: AnyView(UpcomingRideView())),
            MenuItemModel(icon: AppIcon.NavigationSlider.settings, iconColor: AppColor.black, title: AppStrings.NavigationSlider.settings, destination: AnyView(CreateRideView())),
            MenuItemModel(icon: AppIcon.NavigationSlider.referFriend, iconColor: AppColor.black, title: AppStrings.NavigationSlider.referFriend, destination: AnyView(HomeView())),
            MenuItemModel(icon: AppIcon.NavigationSlider.logout, iconColor: AppColor.red, title: AppStrings.NavigationSlider.logout, destination: AnyView(SignInView()))
        ]
    }
    
    func logout(completeion: @escaping () -> Void) {
        AuthenticatorImpl().logout(completionHandler: { sucess, error in
            if let error = error {
                print("Error: \(error)")
            } else {
                completeion()
            }
        })
    }
}

