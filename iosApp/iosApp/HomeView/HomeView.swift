//
//  HomeView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import SwiftUI

struct HomeView: View {
    @StateObject var home =  HomeViewModel()
    @StateObject var viewModel  =  UpcomingRideViewModel()
    @StateObject var profileVM = ProfileViewModel()
    @StateObject var createRideVM = CreateRideViewModel()
    @State private var currentDate = Date()
    @State private var activeChatHostName: String? = nil
    var body: some View {
        ZStack(alignment: .top) {
            ScrollView {
                VStack(spacing: 15){
                    TopNavBar(viewModel: profileVM)
                    ActionButtonView(viewModel: createRideVM, upcomingRideViewModel: viewModel, homeViewModel: home)
                    DashboardView()
                    UpcomingRidesView(home: home, viewModel:viewModel){ hostName in
                        withAnimation(.easeInOut) {
                            activeChatHostName = hostName
                        }
                    }
                    JourneyCardView()
                    PlacesVisitedView()
                }
                .padding()
            }
            if  createRideVM.isRideLoading {
                ProgressViewReusable(title: "Loading ...")
            }
            if let hostName = activeChatHostName {
                chatOverlay(hostName: hostName)
            }
        }
        .task {
            
            viewModel.isRideLoading = true
            async let rides = viewModel.fetchAllUsers()
            async let allRides = viewModel.fetchAllRides()
            let month = Calendar.current.component(.month, from: currentDate)
            let year = Calendar.current.component(.year, from: currentDate)
            async let stats =  home.updateStatsFor(month: month, year: year)
            _ = await (rides, allRides, stats)
            viewModel.isRideLoading = false
        }
        .task {
            await profileVM.fetchProfile(userId: MBUserDefaults.userIdStatic ?? "")
        }
        .refreshable {
            await viewModel.fetchAllRides()
            await viewModel.fetchAllUsers()
        }
    }
    private func chatOverlay(hostName: String) -> some View {
        ZStack {
            Color.black.opacity(0.45)
                .ignoresSafeArea()
                .onTapGesture {
                    withAnimation(.easeInOut) {
                        activeChatHostName = nil
                    }
                }

            VStack(spacing: 0) {
                HStack(spacing: 12) {
                    ZStack(alignment: .bottomTrailing) {
                        profileVM.profileImage
                            .frame(width: 37, height: 37)
                            .clipShape(Circle())
                            .overlay(
                                RoundedRectangle(cornerRadius: 32.5)
                                    .stroke(AppColor.green, lineWidth: 2.5)
                            )
                            Circle()
                                .fill(Color.green)
                                .frame(width: 13, height: 13)
                                .offset(x: 2, y: 2)
                                .overlay(
                                    Circle()
                                        .offset(x: 2, y: 2)
                                        .stroke(Color.white, lineWidth: 1.5)
                                )
                    }


                    VStack(alignment: .leading, spacing: 2) {
                        Text(hostName)
                            .font(KlavikaFont.bold.font(size: 16))
                            .foregroundColor(AppColor.white)
                    }

                    Spacer()
                    Button {
                                withAnimation(.easeInOut) {
                                    activeChatHostName = nil
                                }
                            } label: {
                                Image(systemName: "xmark")
                                    .foregroundColor(.white)
                                    .padding(8)
                            }
                }
                .padding(.horizontal, 16)
                   .padding(.vertical, 12)
                   .background(AppColor.celticBlue)
                ChatDetailView(
                    chatName: hostName,
                    isGroup: false,
                    isOverlay: true
                )
            }
            .frame(
                width: UIScreen.main.bounds.width - 32,
                height: UIScreen.main.bounds.height * 0.6
            )
            .background(AppColor.backgroundLight)
            .cornerRadius(22)
            .shadow(color: .black.opacity(0.25), radius: 20, x: 0, y: 8)
            .transition(.scale.combined(with: .opacity))
        }
    }

}
