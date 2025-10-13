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
    
    var body: some View {
        VStack(spacing: 0) {
            ZStack {
                switch selectedTab {
                case 0:
                    HomeView()
                        .environmentObject(homeViewModel)
                case 1:
                    YourRideScreen()
                case 2:
                    NavigationSlideBar()
                case 3:
                    ProfileScreen()
                default:
                    HomeView()
                        .environmentObject(homeViewModel)
                }
            }
            .animation(.easeInOut(duration: 0.25), value: selectedTab)
            HStack {
                tabItem(index: 0, label: "Home", systemIcon: "house")
                Spacer()
                tabItem(index: 1, label: "Rides", customIcon: AppIcon.Home.rides)
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

