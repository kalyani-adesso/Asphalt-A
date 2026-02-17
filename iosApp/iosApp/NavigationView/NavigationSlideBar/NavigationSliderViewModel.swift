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
   private var createRideVM = CreateRideViewModel()
    init()  {
        Task {
            await createRideVM.getActiveJoinedRide()
            await MainActor.run {
                loadData()
            }
        }
    }
    
    private var connectedRideDestination: AnyView {
        if createRideVM.activeRide?.rideJoined == true,
           let ride = createRideVM.activeRide {
            return AnyView(
                ConnectedRideView(
                    notificationTitle: AppStrings.JoinRide.rideActive,
                    title: AppStrings.ConnectedRide.startRideTitle,
                    subTitle: AppStrings.ConnectedRide.startRideSubtitle,
                    model: ride,
                    rideCompleteModel: []
                )
            )
        } else {
            return AnyView(JoinRideView())
        }
    }
    
    private func loadData() {
        sections = [
            MenuItemModel(icon: AppIcon.NavigationSlider.connectedRide, iconColor: AppColor.black, title: AppStrings.NavigationSlider.connectedRide, destination: connectedRideDestination),
            MenuItemModel(icon: AppIcon.NavigationSlider.marketPlace, iconColor: AppColor.black, title: AppStrings.NavigationSlider.marketplace, destination: AnyView(HomeView())),
            MenuItemModel(icon: AppIcon.NavigationSlider.message, iconColor: AppColor.black, title: AppStrings.NavigationSlider.message, destination: AnyView(MessagesListView())),
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

