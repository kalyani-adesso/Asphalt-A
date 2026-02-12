//
//  HomeView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import SwiftUI

struct ActiveChatUser {
    let id: String
    let name: String
}
struct HomeView: View {
    @EnvironmentObject var home: HomeViewModel
    @EnvironmentObject var viewModel : UpcomingRideViewModel
    @EnvironmentObject var message : MessagesViewModel
    @State private var currentDate = Date()
    @State private var activeChatUser: ActiveChatUser? = nil
    @StateObject private var messagesVM = MessagesViewModel(recipientId: "")

    var body: some View {
        ZStack(alignment: .top) {
            ScrollView {
                VStack(spacing: 15){
                    TopNavBar()
                    ActionButtonView()
                    DashboardView()
                    UpcomingRidesView { hostId in
                        if let hostName = viewModel.usersById[hostId] {
                            withAnimation(.easeInOut) {
                                activeChatUser = ActiveChatUser(id: hostId, name: hostName)
                            }
                        }
                    }
                        .environmentObject(home)
                        .environmentObject(viewModel)
                    JourneyCardView()
                    PlacesVisitedView()
                }
                .padding()
            }
            if viewModel.isRideLoading {
                ProgressViewReusable(title: "Loading ...")
            }
            if let user = activeChatUser {
                chatOverlay(user: user)
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
        .refreshable {
            await viewModel.fetchAllRides()
            await viewModel.fetchAllUsers()
        }
    }
    private func chatOverlay(user: ActiveChatUser) -> some View {
       
          ZStack {
            Color.black.opacity(0.45)
                .ignoresSafeArea()
                .onTapGesture {
                    withAnimation(.easeInOut) {
                        activeChatUser = nil
                    }
                }

            VStack(spacing: 0) {
                HStack(spacing: 12) {
                    
                    ZStack(alignment: .bottomTrailing) {
                        AppImage.Profile.profile.resizable()
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
                        Text(user.name)
                            .font(KlavikaFont.bold.font(size: 16))
                            .foregroundColor(AppColor.white)
                    }

                    Spacer()
                    Button {
                                withAnimation(.easeInOut) {
                                    activeChatUser = nil
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
                    viewModel:MessagesViewModel(recipientId: user.id),
                    chatName: user.name,
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

#Preview {
    HomeView()
        .environmentObject(HomeViewModel())
        .environmentObject(UpcomingRideViewModel())
        .environmentObject(MessagesViewModel(recipientId: ""))
}
