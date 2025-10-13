//
//  NavigationSliderViewModel.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 06/10/25.
//

import SwiftUI
import Combine

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
            MenuItemModel(icon: AppIcon.NavigationSlider.connectedRide, iconColor: AppColor.black, title: AppStrings.NavigationSlider.connectedRide, destination: AnyView(ConnectedRideScreen())),
            MenuItemModel(icon: AppIcon.NavigationSlider.knowledgeCircle, iconColor: AppColor.black, title: AppStrings.NavigationSlider.knowledgeCircle, destination: AnyView(HomeView())),
            MenuItemModel(icon: AppIcon.NavigationSlider.marketPlace, iconColor: AppColor.black, title: AppStrings.NavigationSlider.marketplace, destination: AnyView(YourRideScreen())),
            MenuItemModel(icon: AppIcon.NavigationSlider.settings, iconColor: AppColor.black, title: AppStrings.NavigationSlider.settings, destination: AnyView(CreateRideScreen())),
            MenuItemModel(icon: AppIcon.NavigationSlider.referFriend, iconColor: AppColor.black, title: AppStrings.NavigationSlider.referFriend, destination: AnyView(ConnectedRideScreen())),
            MenuItemModel(icon: AppIcon.NavigationSlider.logout, iconColor: AppColor.red, title: AppStrings.NavigationSlider.logout, destination: AnyView(SignInView()))
        ]
    }
}
