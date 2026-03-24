//
//  NavigationSliderViewModel.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 06/10/25.
//

import SwiftUI
import Combine
import shared
import FirebaseAuth

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
   var createRideVM = CreateRideViewModel()
    init() {
        loadData()
    }

    /// Resolves active joined ride on push (see ConnectedRideSidebarDestinationView) so loader → map matches Home Join Ride.
    private var connectedRideDestination: AnyView {
        AnyView(ConnectedRideSidebarDestinationView())
    }
    
    private func loadData() {
        sections = [
            MenuItemModel(icon: AppIcon.NavigationSlider.connectedRide, iconColor: AppColor.black, title: AppStrings.NavigationSlider.connectedRide, destination: connectedRideDestination),
            MenuItemModel(icon: AppIcon.NavigationSlider.marketPlace, iconColor: AppColor.black, title: AppStrings.NavigationSlider.marketplace, destination: AnyView(HomeView())),
            MenuItemModel(icon: AppIcon.NavigationSlider.message, iconColor: AppColor.black, title: AppStrings.NavigationSlider.message, destination: AnyView(
                           MessagesListView(viewModel: MessagesViewModel(
                               currentUserId: MBUserDefaults.userIdStatic ?? "", recipientId: "",
                               chatType: .private,
                           ))
                       )),
            
            MenuItemModel(icon: AppIcon.NavigationSlider.settings, iconColor: AppColor.black, title: AppStrings.NavigationSlider.settings, destination: AnyView(CreateRideView())),
            MenuItemModel(icon: AppIcon.NavigationSlider.referFriend, iconColor: AppColor.black, title: AppStrings.NavigationSlider.referFriend, destination: AnyView(HomeView())),
            MenuItemModel(icon: AppIcon.NavigationSlider.logout, iconColor: AppColor.red, title: AppStrings.NavigationSlider.logout, destination: AnyView(SignInView()))
        ]
    }
    
    func logout(completeion: @escaping () -> Void) {
        AuthenticatorImpl().logout(completionHandler: { sucess, error in
            // Always clear Firebase Auth session and local data, even if there's an error
            do {
                try Auth.auth().signOut()
            } catch {
                print("Firebase Auth sign out error: \(error)")
            }
            
            // Clear local session so user is taken back to SignIn
            UserDefaults.standard.removeObject(forKey: AppStrings.userdefaultKeys.rememberMeData.rawValue)
            MBUserDefaults.userIdStatic = nil
            MBUserDefaults.userNameStatic = nil
            // Keep hasShownLoginSuccessStatic so that login success is only shown on fresh install
            MBUserDefaults.rideIdStatic = nil
            // Do not clear isRideJoinedID on logout — it is cleared when user ends the ride; if they log back in before ending, they should still be redirected to the active ride.
            if let error = error {
                print("Logout error: \(error)")
            }
            
            completeion()
        })
    }
}

