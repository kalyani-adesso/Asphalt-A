//
//  BottomNavBar.swift
//  iosApp
//
//  Created by Lavanya Selvan on 09/10/25.
//

import SwiftUI

struct BottomNavBar: View {
    @State private var selectedTab = 0
    @Namespace private var animation
    @StateObject private var homeViewModel = HomeViewModel()
    @StateObject private var upcomingRideViewModel = UpcomingRideViewModel()
    @State var showNotification: Bool = false
    @State var showSlideBar: Bool = false
    
    
    var body: some View {
        NavigationStack {
            VStack(spacing: 0) {
                ZStack {
                    switch selectedTab {
                    case 0:
                        HomeView()
                            .environmentObject(homeViewModel)
                            .environmentObject(upcomingRideViewModel)
                    case 1:
                        UpcomingRideView(onBackToHome: { selectedTab = 0 })
                            .environmentObject(upcomingRideViewModel)
                            .environmentObject(homeViewModel)
                    case 2:
                        QueriesView(onBackToHome: { selectedTab = 0 })
                    case 3:
                        ProfileScreen(onBackToHome: { selectedTab = 0 })
                    default:
                        HomeView()
                            .environmentObject(homeViewModel)
                            .environmentObject(upcomingRideViewModel)
                    }
                }
                .frame(maxHeight: .infinity)
                .animation(.easeInOut(duration: 0.25), value: selectedTab)
                HStack {
                    tabItem(index: 0, label: "Home", systemIcon: "house")
                    Spacer()
                    tabItem(index: 1, label: "Rides", customIcon: AppIcon.Home.rides  .renderingMode(.template))
                    Spacer()
                    tabItem(index: 2, label: "Queries", systemIcon: "bubble.left")
                    Spacer()
                    tabItem(index: 3, label: "Profile", systemIcon: "person")
                }
                .padding(.horizontal, 40)
                .padding(.vertical, 10)
                .background(Color.white)
                .overlay(
                    Rectangle()
                        .frame(height: 0.5)
                        .foregroundColor(.gray.opacity(0.2)),
                    alignment: .top
                )
            }
            .ignoresSafeArea(edges: .bottom)
           
            .toolbar {
                ToolbarItemGroup(placement: .navigationBarLeading) {
                        if selectedTab != 0 {
                            Button {
                                selectedTab = 0
                            } label: {
                                AppIcon.CreateRide.backButton
                            }
                        }
                    }
                ToolbarItemGroup(placement: .navigationBarTrailing) {
                    Button {
                        self.showNotification = true
                    } label: {
                        ZStack(alignment: .topTrailing) {
                            Image(systemName: "bell")
                                .font(.system(size: 15))
                                .foregroundColor(AppColor.celticBlue)
                            Circle()
                                .fill(Color.red)
                                .frame(width: 8, height: 8)
                                .offset(x: -2, y: 1)
                        }
                    }
                    Button(action: {
                        self.showSlideBar = true
                    }) {
                        AppIcon.Home.navigation
                    }
                }
            }
            .navigationDestination(isPresented: $showSlideBar, destination: {
                NavigationSlideBar()
            })
            .navigationDestination(isPresented: $showNotification, destination: {
                NotificationView()
            })
            .navigationBarBackButtonHidden(true)
        }
    }
    
    // MARK: - Tab Item View
    @ViewBuilder
    func tabItem(index: Int, label: String, systemIcon: String? = nil, customIcon: Image? = nil) -> some View {
        VStack(spacing: 4) {
            if let systemIcon {
                Image(systemName: systemIcon)
                    .font(.system(size: 18))
                    .foregroundColor(selectedTab == index ? AppColor.white: AppColor.stoneGray)
            } else if let customIcon {
                customIcon
                    .resizable()
                    .scaledToFit()
                    .frame(width: 20, height: 20)
                    .foregroundColor(selectedTab == index ? AppColor.white: AppColor.stoneGray)
            }
            
            Text(label)
                .font(.system(size: 10))
                .foregroundColor(selectedTab == index ? AppColor.white: AppColor.stoneGray)
            
        }
        .frame(width: 62, height: 50)
        .background(
            RoundedRectangle(cornerRadius: 15)
                .fill(selectedTab == index ? AppColor.celticBlue: Color.clear)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 15)
                .stroke(selectedTab == index ? AppColor.celticBlue : Color.clear, lineWidth: 1)
        )
        .onTapGesture {
            
            selectedTab = index
            
        }
    }
}


#Preview {
    BottomNavBar()
}

