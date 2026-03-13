//
//  NavigationSlideBar.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 04/10/25.
//

import SwiftUI

struct NavigationSlideBar: View {
    @StateObject private var viewModel = NavigationSliderViewModel()
    @Environment(\.dismiss) var dismiss
    @EnvironmentObject private var home: HomeViewModel
    @EnvironmentObject private var upcomingRide: UpcomingRideViewModel
    @State var showHome: Bool = false
    var body: some View {
        AppToolBar(showBack: false){
            VStack {
                List(viewModel.sections, id: \.self) { item in
                    MenuItemRow(viewModel: viewModel, item: item, home: home, upcomingRide: upcomingRide)
                        .padding(.vertical, 5)
                        .listRowSeparator(.hidden)
                        .listRowBackground(AppColor.listGray)
                }
                .scrollContentBackground(.hidden)
                .listStyle(.plain)
                .cornerRadius(10)
                .padding(16)
            }
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarBackButtonHidden(true)
        }
    }
}

struct MenuItemRow: View {
    let viewModel: NavigationSliderViewModel
    let item: MenuItemModel
    let home: HomeViewModel
    let upcomingRide: UpcomingRideViewModel
    @State var itemIsSelected: Bool = false
    @State private var showComingSoonAlert: Bool = false
    @State private var logoutAlert: Bool = false
    // Use the same key as MBUserDefaults / iOSApp for login state
    @AppStorage(AppStrings.userdefaultKeys.rememberMeData.rawValue)
    private var isLoggedIn: Bool = false

    private var isComingSoonItem: Bool {
        item.title == AppStrings.NavigationSlider.marketplace ||
        item.title == AppStrings.NavigationSlider.settings ||
        item.title == AppStrings.NavigationSlider.referFriend
    }

    var body: some View {
        HStack(spacing: 8) {
            item.icon
                .resizable()
                .frame(width: 24, height: 24)
            Text(item.title)
                .font(KlavikaFont.medium.font(size: 16))
                .foregroundColor(item.iconColor)
            Spacer()
            Image(systemName: "chevron.right")
                .font(KlavikaFont.medium.font(size: 16))
                .foregroundColor(item.iconColor)
                .frame(width: 24, height: 24)
                .padding(.trailing, 8)
        }
        .modifier(logoutSection(title: item.title))
        .contentShape(Rectangle())
        .onTapGesture {
            if item.title == AppStrings.NavigationSlider.logout {
                viewModel.logout {
                    isLoggedIn = false
                    logoutAlert = true
                }
            } else if isComingSoonItem {
                showComingSoonAlert = true
            } else {
                itemIsSelected = true
            }
        }
        .alert("Coming Soon", isPresented: $showComingSoonAlert) {
            Button("OK", role: .cancel) { }
        } message: {
            Text("This feature will be available soon.")
        }
        .alert("Logout", isPresented: $logoutAlert) {
            Button("Yes", role: .destructive) {
                itemIsSelected = true
            }
            Button("No", role: .cancel) {
               }
        } message: {
            Text("Are you sure you want to log out?")
        }
        .navigationDestination(isPresented: $itemIsSelected, destination: {
            item.destination
                .environmentObject(home)
                .environmentObject(upcomingRide)
                .environmentObject(viewModel.createRideVM)
        })
    }
}

struct logoutSection:ViewModifier {
    var title: String
    func body(content: Content) -> some View {
        if title == AppStrings.NavigationSlider.logout {
            VStack(spacing: 0) {
                Divider()
                    .frame(maxWidth: .infinity)
                    .padding(.top, -9)
                content
            }
        } else {
            content
        }
    }
}

#Preview {
    NavigationSlideBar()
}
