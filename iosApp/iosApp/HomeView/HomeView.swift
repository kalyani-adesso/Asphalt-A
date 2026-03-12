//
//  HomeView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import SwiftUI

struct ActiveChat {
    let id: String
    let name: String
    let chatType: MessagesViewModel.ChatType
    let memberList: [String]?
    let rideTitle: String?
    let rideId: String?
}

struct HomeView: View {
    @EnvironmentObject var home: HomeViewModel
    @EnvironmentObject var viewModel : UpcomingRideViewModel
    @StateObject var profileVM = ProfileViewModel()
    @StateObject var createRideVM = CreateRideViewModel()
    @State private var currentDate = Date()
    @State private var activeChat: ActiveChat? = nil
    @State private var chatVM: MessagesViewModel?
    var body: some View {
        ZStack(alignment: .top) {
            ScrollView {
                VStack(spacing: 15){
                    TopNavBar(viewModel: profileVM)
                    ActionButtonView(viewModel: createRideVM, upcomingRideViewModel: viewModel, homeViewModel: home)
                    DashboardView()
                    UpcomingRidesView(home: home , viewModel: viewModel){ rideId in
                        guard let ride = viewModel.upcomingInvitesRide.first(where: { $0.id == rideId }) else {
                            return
                        }
                        
                        let currentUserID = MBUserDefaults.userIdStatic ?? ""
                        print("my id from home :\(currentUserID)")
                        
                        var chatType: MessagesViewModel.ChatType
                        var chatName: String
                        var rideTitle: String? = nil
                        
                        if ride.createdBy == currentUserID {
                            //  GROUP CHAT
                            
                            chatType = .group
                            rideTitle = ride.title
                            chatName = ride.title
                            
                            Task {
                                let members = (ride.participants ?? []).map { $0.userId }
                                var allMembers = members
                                if !allMembers.contains(ride.createdBy) {
                                    allMembers.append(ride.createdBy)
                                }
                                
                                await MainActor.run {
                                    withAnimation(.easeInOut) {
                                        chatVM = MessagesViewModel(
                                                currentUserId: currentUserID,
                                                recipientId: ride.createdBy,
                                                chatType: chatType,
                                                memberList: allMembers,
                                                rideTitle: rideTitle,
                                                rideId: ride.id
                                            )
                                        activeChat = ActiveChat(
                                            id: ride.id,
                                            name: chatName,
                                            chatType: chatType,
                                            memberList: allMembers,
                                            rideTitle: rideTitle, rideId: ride.id
                                        )
                                    }
                                }
                            }
                            
                        } else {
                            
                            chatType = .private
                            rideTitle = ride.title
                            chatName = viewModel.usersById[ride.createdBy] ?? "Unknown"
                            let members = [currentUserID, ride.createdBy].sorted()
                            let privateChatId = [currentUserID, ride.createdBy].sorted().joined(separator: "_")
                            withAnimation(.easeInOut) {
                                chatVM = MessagesViewModel(
                                        currentUserId: currentUserID,
                                        recipientId: ride.createdBy,
                                        chatType: chatType
                                       
                                    )
                                activeChat = ActiveChat(
                                    id: privateChatId,
                                    name: chatName,
                                    chatType: chatType,
                                    memberList: members,
                                    rideTitle: rideTitle, rideId: ride.id
                                )
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
            if let chat = activeChat, let vm = chatVM {
                ChatOverlayView(
                    chat: chat,
                    viewModel: vm
                ) {
                    activeChat = nil
                }
            }
        }
        .task {
            viewModel.isRideLoading = true
            
            async let rides: () = viewModel.fetchAllUsers()
            async let allRides: () = viewModel.fetchAllRides()
            let month = Calendar.current.component(.month, from: currentDate)
            let year = Calendar.current.component(.year, from: currentDate)
            async let stats: () =  home.updateStatsFor(month: month, year: year)
            
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
    
}

#Preview {
    HomeView()
        .environmentObject(HomeViewModel())
        .environmentObject(UpcomingRideViewModel())
}
